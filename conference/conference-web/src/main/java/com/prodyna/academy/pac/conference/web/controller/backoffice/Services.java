package com.prodyna.academy.pac.conference.web.controller.backoffice;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;

@ManagedBean
@Named
@ApplicationScoped
public class Services {
	
	@Inject
	public static RoomCRUDService roomService;

}
