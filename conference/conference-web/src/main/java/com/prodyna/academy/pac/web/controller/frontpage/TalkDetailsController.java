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

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.conference.service.TalkService;

@ManagedBean(name = "talkDetailsController")
@SessionScoped
public class TalkDetailsController {
	
	@ManagedProperty(value="#{talkId}")
	private Integer talkId;
	
	public void setTalkId(Integer talkId) {
		this.talkId = talkId;
	}
	
	public Integer getTalkId() {
		return talkId;
	}

	private Talk talk;

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
		talk = talkService.findTalk(Integer.valueOf(string));
	}


}
