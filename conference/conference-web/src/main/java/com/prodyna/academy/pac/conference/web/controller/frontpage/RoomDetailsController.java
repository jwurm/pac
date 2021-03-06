package com.prodyna.academy.pac.conference.web.controller.frontpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.facade.service.RoomService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;

@ManagedBean(name = "roomDetailsController")
@ViewScoped
public class RoomDetailsController {

	private Room room;

	private int roomId;

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private RoomService roomService;

	@Inject
	private TalkService talkService;

	private HtmlDataTable dataTable;

	private List<TalksByDay> talkList = new ArrayList<TalksByDay>();

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public List<TalksByDay> getTalkList() {
		return talkList;
	}

	public void setTalkList(List<TalksByDay> talkList) {
		this.talkList = talkList;
	}

	@PostConstruct
	public void initData() {
		Map<String, String> requestParameterMap = facesContext
				.getExternalContext().getRequestParameterMap();
		String string = requestParameterMap.get("roomId");
		// if(string==null){
		// return;
		// }
		roomId = Integer.valueOf(string);
		room = roomService.getRoom(roomId);
		if (room == null) {
			log.severe("Room not found for id " + roomId);
			return;
		}

		List<Talk> talks = talkService.getTalksByRoom(room.getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// sorted map so that we get the right day order simply by using its
		// iterator
		SortedMap<String, TalksByDay> talksByDay = new TreeMap<String, TalksByDay>();
		for (Talk talk : talks) {
			String dateString = sdf.format(talk.getDatetime());
			TalksByDay talksbd = talksByDay.get(dateString);
			if (talksbd == null) {
				talksbd = new TalksByDay(dateString);
				talksByDay.put(dateString, talksbd);
			}
			talksbd.getTalks().add(talk);
		}
		//sort all talks by time
		for (TalksByDay curr : talksByDay.values()) {
			Collections.sort(curr.getTalks(), new Comparator<Talk>() {

				@Override
				public int compare(Talk o1, Talk o2) {
					return o1.getDatetime().compareTo(o2.getDatetime());
				}
			});

		}
		talkList.addAll(talksByDay.values());

	}

}
