package com.prodyna.academy.pac.room.service;

import java.util.List;

import com.prodyna.academy.pac.room.model.Room;

public interface RoomService {
	
	public Room createRoom(Room room);
	
	public Room updateRoom(Room room);
	
	public void deleteRoom(Room room);
	
	public Room findRoom(int id);

	public List<Room> findAllRooms();

}
