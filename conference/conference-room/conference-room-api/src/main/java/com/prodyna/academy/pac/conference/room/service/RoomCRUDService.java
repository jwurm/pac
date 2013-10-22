package com.prodyna.academy.pac.conference.room.service;

import java.util.List;

import com.prodyna.academy.pac.conference.room.model.Room;

/**
 * This is the RoomService interface definition.
 * 
 * @author Jens Wurm
 * 
 */
public interface RoomCRUDService {

	/**
	 * Creates a room and retrieves it
	 * 
	 * @param room
	 * @return the freshly created Room with ID
	 */
	public Room createRoom(Room room);

	/**
	 * Updates a room
	 * 
	 * @param room
	 * @return the updated room
	 */
	public Room updateRoom(Room room);

	/**
	 * Deletes a room
	 * 
	 * @param id
	 *            the ID of the room
	 * @return the freshly deleted room.
	 */
	public Room deleteRoom(int id);

	/**
	 * Finds a room by id
	 * 
	 * @param roomId
	 * @return the Room
	 */
	public Room getRoom(int roomId);

	/**
	 * Reads all rooms in the database
	 * 
	 * @return List of Rooms
	 */
	public List<Room> getRooms();

}
