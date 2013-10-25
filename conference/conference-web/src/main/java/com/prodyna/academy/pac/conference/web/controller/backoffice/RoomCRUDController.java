package com.prodyna.academy.pac.conference.web.controller.backoffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.prodyna.academy.pac.conference.facade.service.RoomService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;
import com.prodyna.academy.pac.conference.web.util.RootErrorMessageReader;

/**
 * Room backoffice controller
 * 
 * @author Jens Wurm
 * 
 */
@ManagedBean(name = "roomCRUDController")
@ViewScoped
public class RoomCRUDController {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private RoomService roomService;

	/**
	 * New room to be created
	 */
	private Room newRoom;

	private UICommand updateCommand;

	private HtmlDataTable dataTable;

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	private List<Room> rooms = new ArrayList<Room>();

	public UICommand getUpdateCommand() {
		return updateCommand;
	}

	public void setUpdateCommand(UICommand updateCommand) {
		this.updateCommand = updateCommand;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	/**
	 * Loads all rooms
	 */
	private void loadRooms() {
		rooms = roomService.getAllRooms();
	}

	public Room getNewRoom() {
		return newRoom;
	}

	/**
	 * Saves a new room.
	 */
	public void createNewRoom() {
		try {
			roomService.createRoom(newRoom);
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "New room created!",
					"Room creation successful"));
			initData();

		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registration Unsuccessful");
			facesContext.addMessage(null, m);
		}
	}

	public void setNewRoom(Room newRoom) {
		this.newRoom = newRoom;
	}

	/**
	 * Loads the data for the dialog
	 */
	@PostConstruct
	public void initData() {
		newRoom = new Room();
		loadRooms();
	}

	/**
	 * Saves the room
	 */
	public void saveRoom() {
		try {

			Room room = (Room) ((HtmlDataTable) dataTable).getRowData();
			if (room.getId() == null) {
				roomService.createRoom(room);
			} else {
				roomService.updateRoom(room);
			}
			loadRooms();
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Room saved.",
					"Room data saved."));

		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}

	/**
	 * Deletes the room.
	 */
	public void deleteRoom() {
		try {
			Room room = (Room) ((HtmlDataTable) dataTable).getRowData();
			roomService.deleteRoom(room.getId());
			loadRooms();
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Room deleted.",
					"Room deleted."));
		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}
}
