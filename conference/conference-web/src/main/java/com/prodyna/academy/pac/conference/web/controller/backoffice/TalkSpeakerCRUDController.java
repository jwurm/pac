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

import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
//@Model
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
	private SpeakerCRUDService speakerService;

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
	private TalkCRUDService talkService;

	private List<Speaker> talkSpeakers;

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
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Speaker assignment failed.");
			facesContext.addMessage(null, m);
		}

	}

	public HtmlDataTable getDataTable() {
		return dataTable;
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

	@PostConstruct
	public void initData() {
		this.speakers = speakerService.getAllSpeakers();
		this.talks = talkService.getAllTalks();
	}

	public void selectTalk(Talk talk) {
		try {
			this.talk = talk;
			talkSpeakers = talkService.getSpeakersByTalk(talk.getId());
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Speaker unassignment failed.");
			facesContext.addMessage(null, m);
		}
	}

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
			String errorMessage = getRootErrorMessage(e);
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
