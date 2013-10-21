package com.prodyna.academy.pac.talk.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.Instant;
import org.joda.time.Interval;

import com.prodyna.academy.pac.base.BusinessException;
import com.prodyna.academy.pac.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.conference.service.ConferenceService;
import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.talk.model.Talk;
import com.prodyna.academy.pac.talk.model.TalkSpeakerAssignment;

/**
 * @author jwurm Implementation of the TalkService.
 * 
 */
@Stateless
@PerformanceLogged
@ServiceLogged
public class TalkServiceImpl implements TalkService {
	@Inject
	private EntityManager em;

	@Inject
	private ConferenceService conference;

	@Inject
	private Logger log;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.talk.service.TalkService#getTalksBySpeaker(int)
	 */
	@Override
	public List<Talk> getTalksBySpeaker(int speakerId) {
		Query query = em
				.createNamedQuery(TalkSpeakerAssignment.FIND_BY_SPEAKER);
		query.setParameter("speakerId", speakerId);
		@SuppressWarnings("unchecked")
		List<TalkSpeakerAssignment> ret = query.getResultList();
		Set<Talk> talks = new HashSet<Talk>();
		for (TalkSpeakerAssignment talkSpeakerAssignment : ret) {
			talks.add(talkSpeakerAssignment.getTalk());
		}

		return new ArrayList<Talk>(talks);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prodyna.academy.pac.talk.service.TalkService#getByRoom(int)
	 */
	@Override
	public List<Talk> getByRoom(int roomId) {
		Query query = em.createNamedQuery(Talk.FIND_BY_ROOM);
		query.setParameter("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<Talk> ret = query.getResultList();

		return new ArrayList<Talk>(ret);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.talk.service.TalkService#assignSpeaker(com.prodyna
	 * .academy.pac.talk.model.Talk,
	 * com.prodyna.academy.pac.speaker.model.Speaker)
	 */
	@Override
	public void assignSpeaker(Talk talk, Speaker speaker) {
		List<TalkSpeakerAssignment> resultList = findTalkSpeakerAssignments(
				talk, speaker);
		if (resultList.size() > 0) {
			log.info("Speaker " + speaker.getId()
					+ " already is assigned to talk " + talk.getId());
		} else {
			validateSpeakerAvailability(talk, speaker);
			TalkSpeakerAssignment tsa = new TalkSpeakerAssignment(talk, speaker);
			em.persist(tsa);
			log.info("Assigning Speaker +" + speaker.getId() + " to talk "
					+ talk.getId());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.talk.service.TalkService#unassignSpeaker(com.
	 * prodyna.academy.pac.talk.model.Talk,
	 * com.prodyna.academy.pac.speaker.model.Speaker)
	 */
	@Override
	public void unassignSpeaker(Talk talk, Speaker speaker) {
		List<TalkSpeakerAssignment> resultList = findTalkSpeakerAssignments(
				talk, speaker);
		// technically there should never be more than one result, but it
		// doesn't hurt...
		for (TalkSpeakerAssignment object : resultList) {
			em.remove(object);
			log.info("Unassigning speaker " + speaker.getId() + " from talk "
					+ talk.getId());
		}

	}

	/**
	 * @param talk
	 * @param speaker
	 * @return
	 */
	private List<TalkSpeakerAssignment> findTalkSpeakerAssignments(Talk talk,
			Speaker speaker) {
		Query query = em
				.createNamedQuery(TalkSpeakerAssignment.FIND_BY_SPEAKER_AND_TALK);
		query.setParameter("speakerId", speaker.getId());
		query.setParameter("talkId", talk.getId());
		@SuppressWarnings("unchecked")
		List<TalkSpeakerAssignment> resultList = query.getResultList();
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.talk.service.TalkService#createTalk(com.prodyna
	 * .academy.pac.talk.model.Talk)
	 */
	@Override
	public Talk createTalk(Talk talk) {
		validateTalk(talk);
		em.persist(talk);
		log.info("Created talk " + talk);
		return talk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.talk.service.TalkService#updateTalk(com.prodyna
	 * .academy.pac.talk.model.Talk)
	 */
	@Override
	public Talk updateTalk(Talk talk) {
		validateTalk(talk);
		Talk ret = em.merge(talk);
		log.info("Updated talk " + talk);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prodyna.academy.pac.talk.service.TalkService#deleteTalk(int)
	 */
	@Override
	public Talk deleteTalk(int id) {
		Talk talk = getTalk(id);
		em.remove(talk);
		log.info("Deleted talk " + talk);
		return talk;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prodyna.academy.pac.talk.service.TalkService#getTalk(int)
	 */
	@Override
	public Talk getTalk(int id) {
		Talk ret = em.find(Talk.class, id);
		log.info("Search for id " + id + " returned " + ret);
		return ret;

	}

	/**
	 * Validates the data of the talk, checks for overlapping dates of room,
	 * speaker etc.
	 * 
	 * @param talk
	 */
	private void validateTalk(Talk talk) {
		validateConferenceInterval(talk);

		validateRoomAvailability(talk);

		validateSpeakerAvailability(talk);

	}

	/**
	 * Checks if the talk fits into the conference interval.
	 * 
	 * @param talk
	 * @thows BusinessException if it's outside of the conference
	 */
	private void validateConferenceInterval(Talk talk) {
		// read conference and room to have up to date data

		Integer conferenceId = talk.getConference().getId();
		Conference conf = conference.getConference(conferenceId);
		if (conf == null) {
			throw new BusinessException("no conference found for id"
					+ conferenceId);
		}

		// validate conference date
		Interval conferenceInterval = conf.buildInterval();
		boolean conferenceIntervalOk = conferenceInterval.contains(talk
				.buildInterval());
		if (!conferenceIntervalOk) {
			throw new BusinessException(
					"Talk is set outside of the duration of the conference! "
							+ talk.toString() + " " + conf.toString());
		}
	}

	/**
	 * Checks if the room is available at the time of the talk.
	 * 
	 * @param talk
	 * @thows BusinessException if it's not available
	 */
	private void validateRoomAvailability(Talk talk) {
		Interval talkInterval = talk.buildInterval();
		// validate room availability
		List<Talk> roomTalks = getByRoom(talk.getRoom().getId());
		for (Talk currTalk : roomTalks) {
			if (currTalk.getId().equals(talk.getId())) {
				// if the talk already exists, don't validate against itself
				continue;
			}

			Interval otherRoomTalkInterval = currTalk.buildInterval();
			if (otherRoomTalkInterval.overlaps(talkInterval)) {
				throw new BusinessException(
						"The designated room is already occupied by "
								+ currTalk + " at that time.");
			}
		}
	}

	/**
	 * Checks if the speaker is available for the talk
	 * 
	 * @thows BusinessException if it's not available
	 * @param talk
	 */
	private void validateSpeakerAvailability(Talk talk) {
		if (talk.getId() == null) {
			// if the talk hasn't been persisted yet, then it cannot have any
			// speakers yet anyway
			return;
		}
		Interval talkInterval = talk.buildInterval();
		List<Speaker> speakers = getSpeakersByTalk(talk.getId());
		for (Speaker speaker : speakers) {
			// no one of the speakers is allowed to have a talk at the same time
			// as this one, except for this talk.

			List<Talk> speakerTalks = getTalksBySpeaker(speaker.getId());
			for (Talk currTalk : speakerTalks) {
				if (currTalk.getId().equals(talk.getId())) {
					// if the talk already exists, don't validate against itself
					continue;
				}

				Interval otherSpeakerTalkInterval = new Interval(new Instant(
						currTalk.getDatetime()), new Instant(
						currTalk.buildEndDateTime()));
				if (otherSpeakerTalkInterval.overlaps(talkInterval)) {
					throw new BusinessException(
							"The designated speaker is already occupied by "
									+ currTalk + " at that time.");
				}
			}
		}
	}

	/**
	 * Checks if the speaker is available for the talk
	 * 
	 * @thows BusinessException if it's not available
	 * @param talk
	 * @param speaker
	 */
	private void validateSpeakerAvailability(Talk talk, Speaker speaker) {
		if (talk.getId() == null) {
			// if the talk hasn't been persisted yet, then it cannot have any
			// speakers yet anyway
			return;
		}
		Interval talkInterval = talk.buildInterval();
		// no one of the speakers is allowed to have a talk at the same time
		// as this one, except for this talk.

		List<Talk> speakerTalks = getTalksBySpeaker(speaker.getId());
		for (Talk currTalk : speakerTalks) {
			if (currTalk.getId().equals(talk.getId())) {
				// if the talk already exists, don't validate against itself
				continue;
			}

			Interval otherSpeakerTalkInterval = new Interval(new Instant(
					currTalk.getDatetime()), new Instant(
					currTalk.buildEndDateTime()));
			if (otherSpeakerTalkInterval.overlaps(talkInterval)) {
				throw new BusinessException(
						"The designated speaker is already occupied by "
								+ currTalk + " at that time.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.talk.service.TalkService#getSpeakersByTalk(int)
	 */
	@Override
	public List<Speaker> getSpeakersByTalk(int talkId) {
		Query query = em.createNamedQuery(TalkSpeakerAssignment.FIND_BY_TALK);
		query.setParameter("talkId", talkId);
		@SuppressWarnings("unchecked")
		List<TalkSpeakerAssignment> resultList = query.getResultList();
		Set<Speaker> ret = new HashSet<Speaker>();
		for (TalkSpeakerAssignment talkSpeakerAssignment : resultList) {
			ret.add(talkSpeakerAssignment.getSpeaker());
		}
		log.info("Searching the speakers for talk " + talkId + " returns "
				+ ret.size() + " results.");
		return new ArrayList<Speaker>(ret);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.prodyna.academy.pac.talk.service.TalkService#getAllTalks()
	 */
	@Override
	public List<Talk> getAllTalks() {
		Query query = em.createNamedQuery(Talk.SELECT_ALL);
		@SuppressWarnings("unchecked")
		List<Talk> ret = query.getResultList();
		Set<Talk> talks = new HashSet<Talk>();
		for (Talk curr : ret) {
			talks.add(curr);
		}

		return new ArrayList<Talk>(talks);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.talk.service.TalkService#getTalksByConference
	 * (int)
	 */
	@Override
	public List<Talk> getTalksByConference(int conferenceId) {
		Query query = em.createNamedQuery(Talk.FIND_BY_CONFERENCE);
		query.setParameter("conferenceId", conferenceId);
		@SuppressWarnings("unchecked")
		List<Talk> ret = query.getResultList();

		return new ArrayList<Talk>(ret);
	}

}
