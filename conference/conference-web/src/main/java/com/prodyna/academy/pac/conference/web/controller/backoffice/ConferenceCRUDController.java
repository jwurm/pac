package com.prodyna.academy.pac.conference.web.controller.backoffice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.facade.service.ConferenceService;

/**
 * Conference backoffice controller
 * @author Jens Wurm
 *
 */
@ManagedBean(name = "conferenceCRUDController")
@ViewScoped
public class ConferenceCRUDController {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private ConferenceService conferenceService;

	/**
	 * New Conference to be created.
	 */
	private Conference newConference;

	private HtmlDataTable dataTable;

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	private List<Conference> conferences = new ArrayList<Conference>();

	public List<Conference> getConferences() {
		return conferences;
	}


	/**
	 * Loads all conferences
	 */
	private void loadConferences() {
		conferences = conferenceService.getAllConferences();
	}

	@Named("newConference")
	@Produces
	public Conference getNewConference() {
		return newConference;
	}

	public void createNewConference() {
		try {
			conferenceService.createConference(newConference);
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "New conference created!",
					"Conference creation successful"));
			// conferences.add(newConference);
			initData();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registration Unsuccessful");
			facesContext.addMessage(null, m);
		}
	}

	public void setNewConference(Conference newConference) {
		this.newConference = newConference;
	}

	@PostConstruct
	public void initData() {
		newConference = new Conference();
		loadConferences();
	}

	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Registration failed. See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

	public String saveConference() {
		try {

			Conference conference = (Conference) ((HtmlDataTable) dataTable)
					.getRowData();
			if (conference.getId() == null) {
				conferenceService.createConference(conference);
			} else {
				conferenceService.updateConference(conference);
			}
			loadConferences();
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Conference saved.",
					"Conference data saved."));

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
		return "";
	}

	public void deleteConference() {
		try {
			Conference conference = (Conference) ((HtmlDataTable) dataTable)
					.getRowData();
			conferenceService.deleteConference(conference.getId());
			loadConferences();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}
}
