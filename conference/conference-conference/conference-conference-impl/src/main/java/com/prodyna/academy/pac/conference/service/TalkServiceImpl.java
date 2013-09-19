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

import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.conference.model.TalkSpeakerAssignment;
import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.speaker.model.Speaker;

@Stateless
public class TalkServiceImpl implements TalkService {
	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	@Override
	public List<Talk> getBySpeaker(Speaker speaker) {
		Query query = em
				.createNamedQuery(TalkSpeakerAssignment.FIND_BY_SPEAKER);
		query.setParameter("speakerId", speaker.getId());
		List<TalkSpeakerAssignment> ret = query.getResultList();
		Set<Talk> talks = new HashSet<Talk>();
		for (TalkSpeakerAssignment talkSpeakerAssignment : ret) {
			talks.add(talkSpeakerAssignment.getTalk());
		}

		return new ArrayList<Talk>(talks);
	}

	@Override
	public List<Talk> getByRoom(Room room) {
		// TODO Auto-generated method stub
		return null;
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
		List<TalkSpeakerAssignment> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public Talk createTalk(Talk talk) {
		Talk ret = em.merge(talk);
		log.info("Created talk " + talk);
		return ret;
	}

	@Override
	public Talk updateTalk(Talk talk) {
		Talk ret = em.merge(talk);
		log.info("Updated talk " + talk);
		return ret;
	}

	@Override
	public void deleteTalk(Talk talk) {
		em.remove(talk);
		log.info("Deleted talk " + talk);

	}

	@Override
	public Talk findTalk(int id) {
		Talk ret = em.find(Talk.class, id);
		log.info("Search for id " + id + " returned " + ret);
		return ret;

	}

}
