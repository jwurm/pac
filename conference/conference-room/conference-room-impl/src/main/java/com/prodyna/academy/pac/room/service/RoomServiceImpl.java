package com.prodyna.academy.pac.room.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.prodyna.academy.pac.room.model.Room;

@Stateless
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

	public void deleteRoom(Room room) {
		Room toRemove = findRoom(room.getId());
		em.remove(toRemove);
		log.info("Deleted room: " + room);
	}

	public Room findRoom(int id) {
		Room ret = em.find(Room.class, id);
		log.info("Search for room with id " + id + " returned result: " + ret);
		return ret;
	}

	@Override
	public List<Room> findAllRooms() {
		Query query = em.createNamedQuery(Room.SELECT_ALL);
		@SuppressWarnings("unchecked")
		List<Room> result = query.getResultList();
		log.info("Search for all rooms returned " + result.size() + " results.");
		return result;
	}

}
