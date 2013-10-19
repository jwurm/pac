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
package com.prodyna.academy.pac.web.controller.frontpage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.talk.model.Talk;
import com.prodyna.academy.pac.talk.service.TalkService;

@ManagedBean(name = "talkDetailsController")
@ViewScoped
public class TalkDetailsController {

	private Integer talkId;

	private Talk talk;

	private List<Speaker> speakers = new ArrayList<Speaker>();

	public List<Speaker> getSpeakers() {
		return speakers;
	}

	public Talk getTalk() {
		return talk;
	}

	public void setTalk(Talk talk) {
		this.talk = talk;
	}

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private TalkService talkService;

	@PostConstruct
	public void initData() {
		Map<String, String> requestParameterMap = facesContext
				.getExternalContext().getRequestParameterMap();
		String string = requestParameterMap.get("talkId");
		talkId = Integer.valueOf(string);
		talk = talkService.getTalk(talkId);
		List<Speaker> findSpeakersByTalk = talkService.getSpeakersByTalk(talk
				.getId());
		speakers.clear();
		speakers.addAll(findSpeakersByTalk);
	}

}
