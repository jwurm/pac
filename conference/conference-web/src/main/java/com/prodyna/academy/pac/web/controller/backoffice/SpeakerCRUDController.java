/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prodyna.academy.pac.web.controller.backoffice;

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

import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.speaker.service.SpeakerService;

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
			String errorMessage = getRootErrorMessage(e);
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
			String errorMessage = getRootErrorMessage(e);
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

	public List<Speaker> getSpeakers() {
		return speakers;
	}

	@PostConstruct
	public void initData() {
		newSpeaker = new Speaker();
		loadSpeakers();
	}

	/**
	 * Loads all speakers
	 */
	private void loadSpeakers() {
		speakers = speakerService.getSpeakers();
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
			String errorMessage = getRootErrorMessage(e);
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
