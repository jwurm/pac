package com.prodyna.academy.pac.conference.service;

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
import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.room.model.Room;

// TODO: Auto-generated Javadoc
/**
 * The Class ConferenceServiceImpl.
 *
 * @author jwurm
 * 
 * Implementation of the ConferenceService
 */
@Stateless
@PerformanceLogged
@ServiceLogged
public class ConferenceServiceImpl implements ConferenceService {

	/** The EntityManager. */
	@Inject
	private EntityManager em;

	/** The Logger. */
	@Inject
	private Logger log;

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.service.ConferenceService#getAllConferences()
	 */
	@Override
	public List<Conference> getAllConferences() {
		Query query = em.createNamedQuery(Conference.SELECT_ALL);
		@SuppressWarnings("unchecked")
		List<Conference> resultList = query.getResultList();
		log.info("Search for all conferences returned " + resultList.size()
				+ " results.");
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.service.ConferenceService#getConference(int)
	 */
	@Override
	public Conference getConference(int id) {
		Conference ret = em.find(Conference.class, id);
		log.info("Search for conference with id " + id + " returned " + ret);
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.service.ConferenceService#updateConference(com.prodyna.academy.pac.conference.model.Conference)
	 */
	@Override
	public Conference updateConference(Conference conference) {
		Conference ret = em.merge(conference);
		log.info("Updated conference " + conference);
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.service.ConferenceService#deleteConference(int)
	 */
	@Override
	public Conference deleteConference(int id) {
		try {
			Conference toRemove = em.find(Conference.class, id);
			em.remove(toRemove);
			// flush to provoke constraint violation exceptions before leaving
			// the method
			em.flush();
			log.info("Deleted conference: " + toRemove);
			return toRemove;
		} catch (PersistenceException e) {
			log.warning(e.getMessage());
			/*
			 * Optimistically assume that this is a constraint violation
			 * exception. We don't have a dependency to hibernate here, so we
			 * cannot check this explicitly.
			 */
			throw new BusinessException("Could not delete conference " + id
					+ ", likely it is being used for talks.");
		}
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.service.ConferenceService#createConference(com.prodyna.academy.pac.conference.model.Conference)
	 */
	@Override
	public Conference createConference(Conference conference) {
		em.persist(conference);
		log.info("Created conference " + conference);
		return conference;
	}

}
