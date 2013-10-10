/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prodyna.academy.pac.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.room.service.RoomService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
//@Model
@ManagedBean(name="roomController")
@ViewScoped
public class RoomController {
	
	@Inject 
	private Logger log;

    @Inject
    private FacesContext facesContext;

    @Inject
    private RoomService roomService;

    private Room newRoom;
    
    
    
    private UICommand updateCommand;

    
    private HtmlDataTable dataTable;
    
    public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}
    
    public HtmlDataTable getDataTable() {
		return dataTable;
	}
    
    private List<Room> rooms=new ArrayList<Room>();
    
    public UICommand getUpdateCommand() {
		return updateCommand;
	}

	public void setUpdateCommand(UICommand updateCommand) {
		this.updateCommand = updateCommand;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	
//	@PostConstruct
	public void postConstruct(){
		loadRooms();
	}

	private void loadRooms() {
		rooms=roomService.findAllRooms();
	}

    @Produces
    @Named
    public Room getNewRoom() {
        return newRoom;
    }
    
    public String test() throws Exception {
    	log.info("test called");
    	return "";
    }

    public String createNewRoom() throws Exception {
        try {
        	roomService.createRoom(newRoom);
        	loadRooms();
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "New room created!", "Room creation successful"));
//            rooms.add(newRoom);
            initNewRoom();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(null, m);
        }
        return "";
    }

    @PostConstruct
    public void initNewRoom() {
        newRoom = new Room("E785", 12);
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
    
    public String updateRoom() throws Exception{
    	Room room = (Room) ((HtmlDataTable)dataTable).getRowData();
    	roomService.updateRoom(room);
    	return "";
    }
    
    public String updateRoomName() throws Exception{
    	log.info("update called");
    	return "";
    }
}
