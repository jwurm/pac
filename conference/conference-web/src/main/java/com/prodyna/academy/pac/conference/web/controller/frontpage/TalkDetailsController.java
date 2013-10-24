package com.prodyna.academy.pac.conference.web.controller.frontpage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.talk.model.Talk;

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
		if(string==null){
			return;
		}
		talkId = Integer.valueOf(string);
		talk = talkService.getTalk(talkId);
		List<Speaker> findSpeakersByTalk = talkService.getSpeakersByTalk(talk
				.getId());
		speakers.clear();
		speakers.addAll(findSpeakersByTalk);
	}

}
