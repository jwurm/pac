package com.prodyna.academy.pac.conference.facade.service;

import java.util.List;

import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.talk.model.Talk;

/**
 * @author Jens Wurm
 * 
 *         Talk services
 */
public interface TalkService {
	/**
	 * @return List of all talks
	 */
	public List<Talk> getAllTalks();

	/**
	 * Retrieves a list of all talks by a given speaker
	 * 
	 * @param speakerId
	 * @return List of talks
	 */
	public List<Talk> getTalksBySpeaker(int speakerId);

	/**
	 * Retrieves a list of all talks in a room
	 * 
	 * @param roomid
	 * @return List of talks
	 */
	public List<Talk> getByRoom(int roomid);

	/**
	 * Assigns a speaker to a talk
	 * 
	 * @param talk
	 * @param speaker
	 */
	void assignSpeaker(Talk talk, Speaker speaker);

	/**
	 * Removes a speaker from a talk
	 * 
	 * @param talk
	 * @param speaker
	 */
	void unassignSpeaker(Talk talk, Speaker speaker);

	/**
	 * Creates a talk.
	 * 
	 * @param talk
	 * @return the created talk including ID
	 */
	public Talk createTalk(Talk talk);

	/**
	 * Updates a talk
	 * 
	 * @param talk
	 * @return the updated talk.
	 */
	public Talk updateTalk(Talk talk);

	/**
	 * Deletes a talk
	 * 
	 * @param id
	 * @return The talk that has just been deleted.
	 */
	public Talk deleteTalk(int id);

	/**
	 * Reads a certain talk
	 * 
	 * @param id
	 *            of the talk
	 * @return The Talk
	 */
	public Talk getTalk(int id);

	/**
	 * Finds all talks that belong to a certain conference
	 * 
	 * @param conferenceId
	 * @return List of talks.
	 */
	public List<Talk> getTalksByConference(int conferenceId);

	/**
	 * Finds all speakers that take part in a certain talk.
	 * 
	 * @param talkId
	 * @return List of speakers.
	 */
	public List<Speaker> getSpeakersByTalk(int talkId);

}
