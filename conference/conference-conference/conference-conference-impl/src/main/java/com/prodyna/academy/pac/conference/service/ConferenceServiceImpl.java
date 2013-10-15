package com.prodyna.academy.pac.conference.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.prodyna.academy.pac.base.BusinessException;
import com.prodyna.academy.pac.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.model.Conference;

@Stateless
@PerformanceLogged
@ServiceLogged
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
	public Conference deleteConference(int id) {
		Conference ret = em.find(Conference.class,id);
		if(!ret.getTalks().isEmpty()){
			throw new BusinessException("Cannot delete conference "+id+" due to assigned talks.");
		}
		em.remove(ret);
		em.flush();
		log.info("Deleted conference " + ret);
		return ret;
	}

	@Override
	public Conference createConference(Conference conference) {
		em.persist(conference);
		log.info("Created conference " + conference);
		return conference;
	}

}
