package com.prodyna.academy.pac.conference.service;

import java.util.List;

import com.prodyna.academy.pac.conference.model.Conference;

public interface ConferenceService {
	
	public Conference getCompleteConference(int id);
	
	public Conference createConference(Conference conference);

	public Conference updateConference(Conference conference);

	public Conference deleteConference(int id);

	public List<Conference> findAllConferences();

}
