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
import javax.validation.constraints.NotNull;

import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.conference.service.ConferenceService;
import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.room.service.RoomService;
import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.talk.model.Talk;
import com.prodyna.academy.pac.talk.service.TalkService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
//@Model
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

	@NotNull
	private Integer conferenceId;

	private List<Room> rooms;

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

	private void loadTalks() {
		talks = talkService.getAllTalks();
	}

	public Talk getNewTalk() {
		return newTalk;
	}

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
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Talk creation failed");
			facesContext.addMessage(null, m);
		}
	}

	public void setNewTalk(Talk newTalk) {
		this.newTalk = newTalk;
	}

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
		this.rooms = roomService.getRooms();

	}

	public List<Room> getRooms() {
		return rooms;
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
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}

	public void deleteTalk() {
		try {
			Talk talk = (Talk) ((HtmlDataTable) dataTable).getRowData();
			talkService.deleteTalk(talk.getId());
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Talk deleted.", "The talk has been deleted successfully.");
			facesContext.addMessage(null, m);
			initData();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Update failed.");
			facesContext.addMessage(null, m);
		}
	}

	public Room convertRoom(Integer roomId) {
		return roomService.getRoom(roomId);
	}
}
