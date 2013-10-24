package com.prodyna.academy.pac.conference.talk.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.prodyna.academy.pac.conference.base.BusinessException;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.model.TalkSpeakerAssignment;

/**
 * @author jwurm Implementation of the TalkService.
 * 
 */
@Stateless
@PerformanceLogged
@ServiceLogged
public class TalkCRUDServiceImpl implements TalkCRUDService {
	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #getTalksBySpeaker(int)
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
	 * @see
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #getByRoom(int)
	 */
	@Override
	public List<Talk> getTalksByRoom(int roomId) {
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
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #assignSpeaker(com.prodyna .academy.pac.talk.model.Talk,
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
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #unassignSpeaker(com. prodyna.academy.pac.talk.model.Talk,
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
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #createTalk(com.prodyna .academy.pac.talk.model.Talk)
	 */
	@Override
	public Talk createTalk(Talk talk) {
		em.persist(talk);
		log.info("Created talk " + talk);
		return talk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #updateTalk(com.prodyna .academy.pac.talk.model.Talk)
	 */
	@Override
	public Talk updateTalk(Talk talk) {
		Talk ret = em.merge(talk);
		log.info("Updated talk " + talk);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #deleteTalk(int)
	 */
	@Override
	public Talk deleteTalk(int id) {
		try {
			Talk talk = getTalk(id);
			em.remove(talk);
			// flush to provoke constraint violation exceptions before leaving
			// the method
			em.flush();
			log.info("Deleted talk: " + talk);
			return talk;
		} catch (PersistenceException e) {
			log.warning(e.getMessage());
			/*
			 * Optimistically assume that this is a constraint violation
			 * exception. We don't have a dependency to hibernate here, so we
			 * cannot check this explicitly.
			 */
			throw new BusinessException("Could not delete talk " + id
					+ ", likely it has speakers assigned.");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #getTalk(int)
	 */
	@Override
	public Talk getTalk(int id) {
		Talk ret = em.find(Talk.class, id);
		log.info("Search for id " + id + " returned " + ret);
		return ret;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #getSpeakersByTalk(int)
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
	 * @see
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #getAllTalks()
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
	 * com.prodyna.academy.pac.conference.conference.talk.service.TalkService
	 * #getTalksByConference (int)
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
