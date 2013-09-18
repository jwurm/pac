package com.prodyna.academy.pac.conference.service;

import java.util.List;

import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.room.model.Room;

public interface ConferenceService {
	
	public Conference getCompleteConference(int id);
	
	public Conference createConference(Conference conference);

	public Conference updateConference(Conference conference);

	public void deleteConference(Conference conference);

	public List<Conference> findAllConferences();

}
