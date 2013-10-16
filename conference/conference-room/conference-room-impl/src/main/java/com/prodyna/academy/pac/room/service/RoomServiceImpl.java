package com.prodyna.academy.pac.room.service;

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
import com.prodyna.academy.pac.room.model.Room;

@Stateless
@PerformanceLogged
@ServiceLogged
public class RoomServiceImpl implements RoomService {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public Room createRoom(Room room) {
		em.persist(room);
		log.info("Created room: " + room);
		return room;
	}

	public Room updateRoom(Room room) {
		Room ret = em.merge(room);
		log.info("Updated room: " + ret);
		return ret;

	}

	public Room deleteRoom(int roomId) {
		Room toRemove = getRoom(roomId);
		try {
			em.remove(toRemove);
			//flush to provoke constraint violation exceptions before leaving the method
			em.flush();
			log.info("Deleted room: " + toRemove);
			return toRemove;
		} catch (PersistenceException e) {
			log.warning(e.getMessage());
			/*
			 * Optimistically assume that this is a constraint violation exception. 
			 * We don't have a dependency to hibernate here, so we cannot check this explicitly.
			 */
			throw new BusinessException(
					"Could not delete room "
							+ roomId
							+ ", likely it is being used for talks.");
		}
	}

	public Room getRoom(int id) {
		Room ret = em.find(Room.class, id);
		log.info("Search for room with id " + id + " returned result: " + ret);
		return ret;
	}

	@Override
	public List<Room> getRooms() {
		Query query = em.createNamedQuery(Room.SELECT_ALL);
		@SuppressWarnings("unchecked")
		List<Room> result = query.getResultList();
		log.info("Search for all rooms returned " + result.size() + " results.");
		return result;
	}

}
