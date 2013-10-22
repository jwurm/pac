package com.prodyna.academy.pac.conference.facade.service;

import java.util.List;

import com.prodyna.academy.pac.conference.conference.model.Conference;

public interface ConferenceServiceFacade {
	

	/**
	 * Finds a conference by Id.
	 * @param conferenceId
	 * @return The Conference or null if none was found.
	 */
	public Conference getConference(int conferenceId);
	
	/**
	 * Creates a conference.
	 * @param conference
	 * @return the freshly created conference with ID
	 */
	public Conference createConference(Conference conference);

	/**
	 * Updates a conference.
	 * @param conference
	 * @return the conference
	 */
	public Conference updateConference(Conference conference);

	/**
	 * Deletes a conference
	 * @param conferenceId
	 * @return the deleted conference.
	 */
	public Conference deleteConference(int conferenceId);

	/**
	 * Finds all conferences.
	 * @return List of conferences
	 */
	public List<Conference> getAllConferences();

}
