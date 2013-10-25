package com.prodyna.academy.pac.conference.web.controller.backoffice;

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

import com.prodyna.academy.pac.conference.facade.service.SpeakerService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.web.util.RootErrorMessageReader;

/**
 * Controller of the talk speaker management
 * 
 * @author Jens Wurm
 * 
 */
@ManagedBean(name = "talkSpeakerCRUDController")
@ViewScoped
public class TalkSpeakerCRUDController {

	private HtmlDataTable dataTable;

	@Inject
	private FacesContext facesContext;

	@Inject
	private Logger log;

	@NotNull
	private Integer speakerId;

	public void setSpeakerId(Integer speakerId) {
		this.speakerId = speakerId;
	}

	public Integer getSpeakerId() {
		return speakerId;
	}

	private List<Speaker> speakers;

	@Inject
	private SpeakerService speakerService;

	private Integer talkId;

	private Talk talk;

	public Talk getTalk() {
		return talk;
	}

	public void setTalk(Talk talk) {
		this.talk = talk;
	}

	private List<Talk> talks;

	@Inject
	private TalkService talkService;

	private List<Speaker> talkSpeakers;

	/**
	 * Assigns a speaker to the selected talk
	 */
	public void assignSpeaker() {
		try {
			Talk talk = talkService.getTalk(talkId);
			Speaker speaker = speakerService.getSpeaker(speakerId);
			if (talk == null) {
				throw new Exception("Talk not found.");
			}
			if (speaker == null) {
				throw new Exception("Speaker not found.");
			}

			talkService.assignSpeaker(talk, speaker);
			talkSpeakers = talkService.getSpeakersByTalk(talk.getId());
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Speaker assigned.",
					"Speaker assigned."));
			initData();

		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Speaker assignment failed.");
			facesContext.addMessage(null, m);
		}

	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public Integer getTalkId() {
		return talkId;
	}

	public List<Speaker> getSpeakers() {
		return speakers;
	}

	public List<Talk> getTalks() {
		return talks;
	}

	public List<Speaker> getTalkSpeakers() {
		return talkSpeakers;
	}

	/**
	 * Initializes the data
	 */
	@PostConstruct
	public void initData() {
		this.speakers = speakerService.getAllSpeakers();
		this.talks = talkService.getAllTalks();
	}

	/**
	 * Marks a talk as selected
	 * @param talk
	 */
	public void selectTalk(Talk talk) {
		try {
			this.talk = talk;
			talkSpeakers = talkService.getSpeakersByTalk(talk.getId());
		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Speaker unassignment failed.");
			facesContext.addMessage(null, m);
		}
	}

	/**
	 * Removes the speaker from the selected talk.
	 */
	public void removeSpeaker() {

		try {

			Speaker speaker = (Speaker) dataTable.getRowData();
			talkService.unassignSpeaker(talk, speaker);
			talkSpeakers = talkService.getSpeakersByTalk(talk.getId());
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Speaker unassigned.",
					"Speaker assigned."));
			initData();

		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Speaker unassignment failed.");
			facesContext.addMessage(null, m);
		}
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public void setSpeakers(List<Speaker> speakers) {
		this.speakers = speakers;
	}

	public void setTalkId(Integer talkId) {
		this.talkId = talkId;
	}

}
