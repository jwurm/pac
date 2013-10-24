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

import com.prodyna.academy.pac.conference.facade.service.SpeakerService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

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
