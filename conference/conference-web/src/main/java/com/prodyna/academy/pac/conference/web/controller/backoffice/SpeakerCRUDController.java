package com.prodyna.academy.pac.conference.web.controller.backoffice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.facade.service.SpeakerService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerCRUDService;
import com.prodyna.academy.pac.conference.web.util.RootErrorMessageReader;

/**
 * CRUD Backing bean for the speaker entity
 * 
 * @author Jens Wurm
 * 
 */
@ManagedBean(name = "speakerCRUDController")
@ViewScoped
public class SpeakerCRUDController {

	/**
	 * Displayed data table of speakers
	 */
	private HtmlDataTable dataTable;

	@Inject
	private FacesContext facesContext;

	@Inject
	private Logger log;

	/**
	 * New speaker to be created
	 */
	private Speaker newSpeaker;

	/**
	 * List of existing speakers
	 */
	private List<Speaker> speakers = new ArrayList<Speaker>();

	@Inject
	private SpeakerService speakerService;

	/**
	 * Creates a new speaker from the newSpeaker field.
	 */
	public void createNewSpeaker() {
		try {
			speakerService.createSpeaker(newSpeaker);
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "New speaker created!",
					"Speaker creation successful"));
			initData();

		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Speaker creation failed.");
			facesContext.addMessage(null, m);
		}
	}

	/**
	 * Deletes the currently selected speaker
	 */
	public void deleteSpeaker() {
		try {
			Speaker Speaker = (Speaker) ((HtmlDataTable) dataTable)
					.getRowData();
			speakerService.deleteSpeaker(Speaker.getId());
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Speaker deleted.",
					"Speaker deletion successful."));
			loadSpeakers();
		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public Speaker getNewSpeaker() {
		return newSpeaker;
	}

	public List<Speaker> getSpeakers() {
		return speakers;
	}

	/**
	 * Loads the data for the dialog
	 */
	@PostConstruct
	public void initData() {
		newSpeaker = new Speaker();
		loadSpeakers();
	}

	/**
	 * Loads all speakers
	 */
	private void loadSpeakers() {
		speakers = speakerService.getAllSpeakers();
	}

	/**
	 * Saves the currently selected speaker.
	 */
	public void saveSpeaker() {
		try {

			Speaker Speaker = (Speaker) ((HtmlDataTable) dataTable)
					.getRowData();
			if (Speaker.getId() == null) {
				speakerService.createSpeaker(Speaker);
			} else {
				speakerService.updateSpeaker(Speaker);
			}
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Speaker saved.",
					"Speaker data saved."));
			loadSpeakers();

		} catch (Exception e) {
			String errorMessage = RootErrorMessageReader.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public void setNewSpeaker(Speaker newSpeaker) {
		this.newSpeaker = newSpeaker;
	}

}
