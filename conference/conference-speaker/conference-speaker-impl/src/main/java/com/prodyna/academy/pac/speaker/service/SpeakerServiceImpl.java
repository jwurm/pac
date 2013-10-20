package com.prodyna.academy.pac.speaker.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.prodyna.academy.pac.base.BusinessException;
import com.prodyna.academy.pac.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.speaker.model.Speaker;

@Stateless
@PerformanceLogged
@ServiceLogged
public class SpeakerServiceImpl implements SpeakerService {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public Speaker createSpeaker(Speaker speaker) {
		Speaker ret = em.merge(speaker);
		log.info("Created speaker: " + ret);
		return ret;
	}

	public Speaker updateSpeaker(Speaker speaker) {
		Speaker ret = em.merge(speaker);
		log.info("Updated speaker: " + ret);
		return ret;

	}

	public Speaker deleteSpeaker(int id) {
		try {
			Speaker toRemove = getSpeaker(id);
			em.remove(toRemove);
			// flush to provoke constraint violation exceptions before leaving
			// the method
			em.flush();
			log.info("Deleted speaker: " + toRemove);
			return toRemove;
		} catch (PersistenceException e) {
			log.warning(e.getMessage());
			/*
			 * Optimistically assume that this is a constraint violation
			 * exception. We don't have a dependency to hibernate here, so we
			 * cannot check this explicitly.
			 */
			throw new BusinessException("Could not delete speaker " + id
					+ ", likely it is assigned to talks.");
		}

	}

	public Speaker getSpeaker(int id) {
		Speaker ret = em.find(Speaker.class, id);
		log.info("Search for speaker with id " + id + " returned result: "
				+ ret);
		return ret;
	}

	@Override
	public List<Speaker> getSpeakers() {
		Query query = em.createNamedQuery(Speaker.SELECT_ALL);
		@SuppressWarnings("unchecked")
		List<Speaker> result = query.getResultList();
		log.info("Search for all speakers returned " + result.size()
				+ " results.");
		return result;
	}

}
