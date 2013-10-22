package com.prodyna.academy.pac.conference.facade;

import java.util.List;

import javax.inject.Inject;

import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.conference.service.ConferenceCRUDService;
import com.prodyna.academy.pac.conference.facade.service.ConferenceServiceFacade;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

public class ConferenceServiceFacadeImpl implements ConferenceServiceFacade{
//	@Inject
	private ConferenceCRUDService conferenceService;
//	@Inject
	private TalkCRUDService talkService;
	@Override
	public Conference getConference(int conferenceId) {
		return conferenceService.getConference(conferenceId);
	}
	@Override
	public Conference createConference(Conference conference) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Conference updateConference(Conference conference) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Conference deleteConference(int conferenceId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Conference> getAllConferences() {
		// TODO Auto-generated method stub
		return null;
	}

}
