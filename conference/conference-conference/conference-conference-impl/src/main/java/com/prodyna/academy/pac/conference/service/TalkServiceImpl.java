package com.prodyna.academy.pac.conference.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.prodyna.academy.pac.conference.model.Talk;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Talk> getByRoom(Room room) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void assignSpeaker(Talk talk, Speaker speaker) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unassignSpeaker(Talk talk, Speaker speaker) {
		// TODO Auto-generated method stub

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
