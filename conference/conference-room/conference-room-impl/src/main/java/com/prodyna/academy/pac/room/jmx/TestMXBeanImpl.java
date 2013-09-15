package com.prodyna.academy.pac.room.jmx;

import javax.inject.Inject;

import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.room.service.RoomService;


public class TestMXBeanImpl implements TestMXBean {
	public static final String OBJECT_NAME = "conference:service=tester";

	
	
	@Inject
	private RoomService service;
	
	
	public void setService(RoomService service) {
		this.service = service;
	}
	
	public void runTest() {
		Room room=new Room("ME701", 50);
		room = service.createRoom(room);
		System.out.println(room);
		
	}
	
}
