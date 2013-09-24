package com.prodyna.academy.pac.conference.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.prodyna.academy.pac.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.conference.model.Conference;

@Stateless
@PerformanceLogged
public class ConferenceServiceImpl implements ConferenceService {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	@Override
	public List<Conference> findAllConferences() {
		Query query = em.createNamedQuery(Conference.SELECT_ALL);
		@SuppressWarnings("unchecked")
		List<Conference> resultList = query.getResultList();
		log.info("Search for all conferences returned " + resultList.size()
				+ " results.");
		return resultList;
	}

	@Override
	public Conference getCompleteConference(int id) {
		Conference ret = em.find(Conference.class, id);
		log.info("Search for conference with id " + id + " returned " + ret);
		return ret;
	}

	@Override
	public Conference updateConference(Conference conference) {
		Conference ret = em.merge(conference);
		log.info("Updated conference " + conference);
		return ret;
	}

	@Override
	public void deleteConference(Conference conference) {
		Conference ret = em.find(Conference.class, conference.getId());
		em.remove(ret);
		log.info("Deleted conference " + conference);
	}

	@Override
	public Conference createConference(Conference conference) {
		em.persist(conference);
		log.info("Created conference " + conference);
		return conference;
	}

}
