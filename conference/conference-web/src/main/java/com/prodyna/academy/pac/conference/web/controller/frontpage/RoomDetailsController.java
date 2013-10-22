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
package com.prodyna.academy.pac.conference.web.controller.frontpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkService;

@ManagedBean(name = "roomDetailsController")
@SessionScoped
public class RoomDetailsController {

	private Room room;

	private int roomId;

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private RoomService roomService;

	@Inject
	private TalkService talkService;

	private HtmlDataTable dataTable;

	private List<TalksByDay> talkList = new ArrayList<TalksByDay>();

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public List<TalksByDay> getTalkList() {
		return talkList;
	}

	public void setTalkList(List<TalksByDay> talkList) {
		this.talkList = talkList;
	}

	@PostConstruct
	public void initData() {
		Map<String, String> requestParameterMap = facesContext
				.getExternalContext().getRequestParameterMap();
		String string = requestParameterMap.get("roomId");
		// if(string==null){
		// return;
		// }
		roomId = Integer.valueOf(string);
		room = roomService.getRoom(roomId);
		if (room == null) {
			log.severe("Room not found for id " + roomId);
			return;
		}

		List<Talk> talks = talkService.getByRoom(room.getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// sorted map so that we get the right order simply by using its
		// iterator
		SortedMap<String, TalksByDay> talksByDay = new TreeMap<String, TalksByDay>();
		for (Talk talk : talks) {
			String dateString = sdf.format(talk.getDatetime());
			TalksByDay talksbd = talksByDay.get(dateString);
			if (talksbd == null) {
				talksbd = new TalksByDay(dateString);
				talksByDay.put(dateString, talksbd);
			}
			talksbd.getTalks().add(talk);
		}

		talkList.addAll(talksByDay.values());

	}

}
