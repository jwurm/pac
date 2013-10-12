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

import org.joda.time.Instant;

import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.conference.service.ConferenceService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
//@Model
@ManagedBean(name = "conferenceController")
@ViewScoped
public class ConferenceController {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private ConferenceService conferenceService;

	
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

	// @PostConstruct

	private void loadConferences() {
		conferences = conferenceService.findAllConferences();
	}

	@Named("newConference")
	@Produces
	public Conference getNewConference() {
		return newConference;
	}

	public String createNewConference() throws Exception {
		try {
			 conferenceService.createConference(newConference);
			 facesContext.addMessage(null,
			 new FacesMessage(FacesMessage.SEVERITY_INFO, "New conference created!",
			 "Conference creation successful"));
			 // conferences.add(newConference);
			initData();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registration Unsuccessful");
			facesContext.addMessage(null, m);
		}
		return "";
	}

	public void setNewConference(Conference newConference) {
		this.newConference = newConference;
	}

	@PostConstruct
	public void initData() {
		newConference = new Conference("name", "desc", new Instant("2014-01-01").toDate(), new Instant("2014-01-01").toDate());
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

	public String saveConference() throws Exception {
		try {

			Conference conference = (Conference) ((HtmlDataTable) dataTable).getRowData();
			if (conference.getId() == null) {
				conferenceService.createConference(conference);
			} else {
				conferenceService.updateConference(conference);
			}
			loadConferences();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
		return "";
	}

	public String deleteConference() throws Exception {
		try {
			Conference conference = (Conference) ((HtmlDataTable) dataTable).getRowData();
			conferenceService.deleteConference(conference.getId());
			loadConferences();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
		return "";
	}
}
