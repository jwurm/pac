package com.prodyna.academy.pac.conference.web.controller.backoffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.facade.service.ConferenceService;
import com.prodyna.academy.pac.conference.facade.service.RoomService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.web.util.RootErrorMessageReader;
/**
 * Controller for the talk backoffice dialog
 * @author Jens Wurm
 *
 */
@ManagedBean(name = "talkCRUDController")
@ViewScoped
public class TalkCRUDController {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private TalkService talkService;

	@Inject
	private ConferenceService conferenceService;

	@Inject
	public static RoomService roomService;

	private Talk newTalk;

	private HtmlDataTable dataTable;

	/**
	 * Id of the selected conference
	 */
	@NotNull
	private Integer conferenceId;

	private List<Room> rooms;

	/**
	 * Id of the selected room
	 */
	@NotNull
	private Integer roomId;

	private List<Conference> conferences;

	public List<Conference> getConferences() {
		return conferences;
	}

	public Integer getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(Integer conferenceId) {
		this.conferenceId = conferenceId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	private List<Talk> talks = new ArrayList<Talk>();

	public List<Talk> getTalks() {
		return talks;
	}

	/**
	 * Loads all talks
	 */
	private void loadTalks() {
		talks = talkService.getAllTalks();
		//sort by time
		Collections.sort(talks, new Comparator<Talk>(){

			@Override
			public int compare(Talk o1, Talk o2) {
				return o1.getDatetime().compareTo(o2.getDatetime());
			}
			
		});
	}

	public Talk getNewTalk() {
		return newTalk;
	}

	/**
	 * Saves a new talk
	 */
	public void createNewTalk() {
		try {
			Room room = roomService.getRoom(roomId);
			Conference conference = conferenceService
					.getConference(conferenceId);
			newTalk.setConference(conference);
			newTalk.setRoom(room);
			talkService.createTalk(newTalk);
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "New talk created!",
					"Talk creation successful"));

			initData();
		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Talk creation failed");
			facesContext.addMessage(null, m);
		}
	}

	public void setNewTalk(Talk newTalk) {
		this.newTalk = newTalk;
	}

	/**
	 * Loads the data for the dialog
	 */
	@PostConstruct
	public void initData() {
		loadTalks();
		loadRooms();
		loadConferences();
		newTalk = new Talk();
	}

	private void loadConferences() {
		this.conferences = conferenceService.getAllConferences();
	}

	private void loadRooms() {
		this.rooms = roomService.getAllRooms();

	}

	public List<Room> getRooms() {
		return rooms;
	}

	/**
	 * Saves the selected talk
	 */
	public void saveTalk() {
		try {

			Talk talk = (Talk) ((HtmlDataTable) dataTable).getRowData();
			if (talk.getId() == null) {
				talkService.createTalk(talk);
			} else {
				talkService.updateTalk(talk);
			}
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Talk updated", "The talk has been updated successfully.");
			facesContext.addMessage(null, m);
			initData();

		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}

	/**
	 * Deletes the selected talk
	 */
	public void deleteTalk() {
		try {
			Talk talk = (Talk) ((HtmlDataTable) dataTable).getRowData();
			talkService.deleteTalk(talk.getId());
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Talk deleted.", "The talk has been deleted successfully.");
			facesContext.addMessage(null, m);
			initData();
		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}

	public Room convertRoom(Integer roomId) {
		return roomService.getRoom(roomId);
	}
}
