package com.prodyna.academy.pac.conference.service;

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

import com.prodyna.academy.pac.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.conference.model.TalkSpeakerAssignment;
import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.speaker.model.Speaker;

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

	@Override
	public List<Talk> getTalksBySpeaker(Speaker speaker) {
		Query query = em
				.createNamedQuery(TalkSpeakerAssignment.FIND_BY_SPEAKER);
		query.setParameter("speakerId", speaker.getId());
		@SuppressWarnings("unchecked")
		List<TalkSpeakerAssignment> ret = query.getResultList();
		Set<Talk> talks = new HashSet<Talk>();
		for (TalkSpeakerAssignment talkSpeakerAssignment : ret) {
			talks.add(talkSpeakerAssignment.getTalk());
		}

		return new ArrayList<Talk>(talks);
	}

	@Override
	public List<Talk> getByRoom(Room room) {
		Query query = em.createNamedQuery(Talk.FIND_BY_ROOM);
		query.setParameter("roomId", room.getId());
		@SuppressWarnings("unchecked")
		List<Talk> ret = query.getResultList();

		return new ArrayList<Talk>(ret);
	}

	@Override
	public void assignSpeaker(Talk talk, Speaker speaker) {
		List<TalkSpeakerAssignment> resultList = findTalkSpeakerAssignments(
				talk, speaker);
		if (resultList.size() > 0) {
			log.info("Speaker " + speaker.getId()
					+ " already is assigned to talk " + talk.getId());
		} else {
			TalkSpeakerAssignment tsa = new TalkSpeakerAssignment(talk, speaker);
			em.persist(tsa);
			log.info("Assigning Speaker +" + speaker.getId() + " to talk "
					+ talk.getId());
		}

	}

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

	@Override
	public Talk createTalk(Talk talk) {
		validateTalk(talk);
		Talk ret = em.merge(talk);
		log.info("Created talk " + talk);
		return ret;
	}

	@Override
	public Talk updateTalk(Talk talk) {
		validateTalk(talk);
		Talk ret = em.merge(talk);
		log.info("Updated talk " + talk);
		return ret;
	}

	@Override
	public Talk deleteTalk(int id) {
		Talk talk = findTalk(id);
		em.remove(talk);
		log.info("Deleted talk " + talk);
		return talk;

	}

	@Override
	public Talk findTalk(int id) {
		Talk ret = em.find(Talk.class, id);
		log.info("Search for id " + id + " returned " + ret);
		return ret;

	}

	/**
	 * Validates the data of the talk
	 * 
	 * @param talk
	 */
	private void validateTalk(Talk talk) {
		validateConferenceInterval(talk);

		validateRoomAvailability(talk);

		validateSpeakerAvailability(talk);

	}

	private void validateConferenceInterval(Talk talk) {
		// read conference and room to have up to date data
		Conference conf = conference.getCompleteConference(talk.getConference()
				.getId());

		// validate conference date

		Interval conferenceInterval = conf.getInterval();
		boolean conferenceIntervalOk = conferenceInterval.contains(talk
				.getInterval());
		if (!conferenceIntervalOk) {
			throw new RuntimeException(
					"Talk is set outside of the duration of the conference! "
							+ talk.toString() + " " + conf.toString());
		}
	}

	private void validateRoomAvailability(Talk talk) {
		Interval talkInterval = talk.getInterval();
		// validate room availability
		List<Talk> roomTalks = getByRoom(talk.getRoom());
		for (Talk currTalk : roomTalks) {
			if (currTalk.getId().equals(talk.getId())) {
				// if the talk already exists, don't validate against itself
				continue;
			}

			Interval otherRoomTalkInterval = currTalk.getInterval();
			if (otherRoomTalkInterval.overlaps(talkInterval)) {
				throw new RuntimeException(
						"The designated room is already occupied by "
								+ currTalk + " at that time.");
			}
		}
	}

	private void validateSpeakerAvailability(Talk talk) {
		if (talk.getId() == null) {
			// if the talk hasn't been persisted yet, then it cannot have any
			// speakers yet anyway
			return;
		}
		Interval talkInterval = talk.getInterval();
		List<Speaker> speakers = findSpeakers(talk.getId());
		for (Speaker speaker : speakers) {
			// no one of the speakers is allowed to have a talk at the same time
			// as this one, except for this talk.

			List<Talk> speakerTalks = getTalksBySpeaker(speaker);
			for (Talk currTalk : speakerTalks) {
				if (currTalk.getId().equals(talk.getId())) {
					// if the talk already exists, don't validate against itself
					continue;
				}

				Interval otherSpeakerTalkInterval = new Interval(new Instant(
						currTalk.getDatetime()), new Instant(
						currTalk.getEndDateTime()));
				if (otherSpeakerTalkInterval.overlaps(talkInterval)) {
					throw new RuntimeException(
							"The designated speaker is already occupied by "
									+ currTalk + " at that time.");
				}
			}
		}
	}

	@Override
	public List<Speaker> findSpeakers(int talkId) {
		// TODO ungetestet
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

	@Override
	public List<Talk> getTalks() {
		Query query = em.createNamedQuery(Talk.SELECT_ALL);
		@SuppressWarnings("unchecked")
		List<Talk> ret = query.getResultList();
		Set<Talk> talks = new HashSet<Talk>();
		for (Talk curr : ret) {
			talks.add(curr);
		}

		return new ArrayList<Talk>(talks);
	}

}
