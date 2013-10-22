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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkService;

@ManagedBean(name = "speakerDetailsController")
@ViewScoped
public class SpeakerDetailsController {

	private Integer speakerId;

	private Speaker speaker;

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	public void setTalks(List<Talk> talks) {
		this.talks = talks;
	}

	private List<Talk> talks = new ArrayList<Talk>();

	public List<Talk> getTalks() {
		return talks;
	}

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private TalkService talkService;

	@Inject
	private SpeakerService speakerService;

	@PostConstruct
	public void initData() {
		Map<String, String> requestParameterMap = facesContext
				.getExternalContext().getRequestParameterMap();
		String string = requestParameterMap.get("speakerId");
		speakerId = Integer.valueOf(string);
		speaker = speakerService.getSpeaker(speakerId);
		List<Talk> talksList = talkService.getTalksBySpeaker(speaker.getId());
		talks.clear();
		// sort by date
		Collections.sort(talksList, new Comparator<Talk>() {

			@Override
			public int compare(Talk o1, Talk o2) {
				return o1.getDatetime().compareTo(o2.getDatetime());
			}
		});
		talks.addAll(talksList);
	}

}
