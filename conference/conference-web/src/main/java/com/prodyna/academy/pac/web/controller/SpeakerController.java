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
package com.prodyna.academy.pac.web.controller;

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

import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.speaker.service.SpeakerService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
//@Model
@ManagedBean(name = "speakerController")
@ViewScoped
public class SpeakerController {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private SpeakerService speakerService;

	
	private Speaker newSpeaker;

	private HtmlDataTable dataTable;

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	private List<Speaker> speakers = new ArrayList<Speaker>();


	public List<Speaker> getSpeakers() {
		return speakers;
	}


	private void loadSpeakers() {
		speakers = speakerService.findAllSpeakers();
	}

	@Named
	@Produces
	public Speaker getNewSpeaker() {
		return newSpeaker;
	}


	public String createNewSpeaker() throws Exception {
		try {
			 speakerService.createSpeaker(newSpeaker);
			 facesContext.addMessage(null,
			 new FacesMessage(FacesMessage.SEVERITY_INFO, "New speaker created!",
			 "Speaker creation successful"));
			initData();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registration Unsuccessful");
			facesContext.addMessage(null, m);
		}
		return "";
	}

	public void setNewSpeaker(Speaker newSpeaker) {
		this.newSpeaker = newSpeaker;
	}

	@PostConstruct
	public void initData() {
		newSpeaker = createSpeaker();
		loadSpeakers();
	}

	private Speaker createSpeaker() {
		return new Speaker("Name", "Description");
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

	public String saveSpeaker() throws Exception {
		try {

			Speaker Speaker = (Speaker) ((HtmlDataTable) dataTable).getRowData();
			if (Speaker.getId() == null) {
				speakerService.createSpeaker(Speaker);
			} else {
				speakerService.updateSpeaker(Speaker);
			}
			loadSpeakers();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
		return "";
	}

	public String deleteSpeaker() throws Exception {
		try {
			Speaker Speaker = (Speaker) ((HtmlDataTable) dataTable).getRowData();
			speakerService.deleteSpeaker(Speaker.getId());
			loadSpeakers();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
		return "";
	}

}
