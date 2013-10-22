package com.prodyna.academy.pac.conference.web.controller.frontpage;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import com.prodyna.academy.pac.conference.talk.model.Talk;

@ManagedBean
public class TalksByDay {
	public TalksByDay(String dateString) {
		this.day = dateString;
	}

	private String day;
	private List<Talk> talks = new ArrayList<Talk>();

	public List<Talk> getTalks() {
		return talks;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

}
