package com.prodyna.academy.pac.conference.web.controller.backoffice;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.prodyna.academy.pac.conference.room.model.Room;

/**
 * Converts between rooms and roomIds
 */
@FacesConverter(forClass = Room.class)
public class RoomConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// somewhat hacky workaround to the Converter injection problem
		return TalkCRUDController.roomService.getRoom(Integer.valueOf(arg2));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return String.valueOf(((Room) arg2).getId());
	}

}
