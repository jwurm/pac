package com.prodyna.academy.pac.web.controller.backoffice;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.prodyna.academy.pac.room.model.Room;

@FacesConverter(forClass = Room.class)
public class RoomConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// somewhat hacky workaround to the Converter injection problem
		return TalkCRUDController.roomService.getRoom(Integer.valueOf(arg2));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return String.valueOf(((Room) arg2).getId());
	}

}
