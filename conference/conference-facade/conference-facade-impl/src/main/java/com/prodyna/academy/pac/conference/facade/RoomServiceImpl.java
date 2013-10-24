package com.prodyna.academy.pac.conference.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.base.BusinessException;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.facade.service.RoomService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

/**
 * Implementation of the room service
 * 
 * @author Jens Wurm
 * 
 */
@PerformanceLogged
@ServiceLogged
@Stateless
public class RoomServiceImpl implements RoomService {

	@Inject
	private TalkCRUDService talkService;

	@Inject
	private RoomCRUDService roomService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.facade.service.RoomService#createRoom
	 * (com.prodyna.academy.pac.conference.room.model.Room)
	 */
	@Override
	public Room createRoom(Room room) {
		return roomService.createRoom(room);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.facade.service.RoomService#updateRoom
	 * (com.prodyna.academy.pac.conference.room.model.Room)
	 */
	@Override
	public Room updateRoom(Room room) {
		return roomService.updateRoom(room);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.facade.service.RoomService#deleteRoom
	 * (int)
	 */
	@Override
	public Room deleteRoom(int id) {
		List<Talk> talksByRoom = talkService.getTalksByRoom(id);
		if (!talksByRoom.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Talk talk : talksByRoom) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(talk.getName());
			}
			throw new BusinessException(
					"Cannot delete the room due to assigned talks: "
							+ sb.toString());
		}
		return roomService.deleteRoom(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.facade.service.RoomService#getRoom
	 * (int)
	 */
	@Override
	public Room getRoom(int roomId) {
		return roomService.getRoom(roomId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.facade.service.RoomService#getRooms()
	 */
	@Override
	public List<Room> getRooms() {
		return roomService.getRooms();
	}

}
