package com.prodyna.academy.pac.web.controller.backoffice;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import com.prodyna.academy.pac.room.service.RoomService;

@ManagedBean
@Named
@ApplicationScoped
public class Services {
	
	@Inject
	public static RoomService roomService;

}
