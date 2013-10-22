package com.prodyna.academy.pac.conference.speaker.service;

import java.util.List;

import com.prodyna.academy.pac.conference.speaker.model.Speaker;

/**
 * Speaker service interface.
 * 
 * @author Jens Wurm
 * 
 */
public interface SpeakerCRUDService {

	/**
	 * Creates a speaker
	 * 
	 * @param speaker
	 * @return Created speaker with ID
	 */
	public Speaker createSpeaker(Speaker speaker);

	/**
	 * Updates a speaker
	 * 
	 * @param speaker
	 * @return
	 */
	public Speaker updateSpeaker(Speaker speaker);

	/**
	 * Deletes a speaker.
	 * 
	 * @param id
	 * @return the freshly deleted speaker
	 */
	public Speaker deleteSpeaker(int id);

	/**
	 * Finds a speaker by id
	 * 
	 * @param id
	 * @return Speaker
	 */
	public Speaker getSpeaker(int id);

	/**
	 * Retrieves all speakers
	 * 
	 * @return List of speakers
	 */
	public List<Speaker> getAllSpeakers();

}
