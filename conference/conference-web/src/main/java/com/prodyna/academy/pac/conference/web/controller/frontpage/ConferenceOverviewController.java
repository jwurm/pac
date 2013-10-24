package com.prodyna.academy.pac.conference.web.controller.frontpage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.conference.service.ConferenceCRUDService;
import com.prodyna.academy.pac.conference.facade.service.ConferenceService;

@ManagedBean(name = "conferenceOverviewController")
@ViewScoped
public class ConferenceOverviewController {

	@Inject
	private Logger log;

	@Inject
	private ConferenceService conferenceService;


	private HtmlDataTable dataTable;

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	private List<Conference> conferences = new ArrayList<Conference>();

	private Conference conference;

	public List<Conference> getConferences() {
		return conferences;
	}

	private void loadConferences() {
		conferences = conferenceService.getAllConferences();
	}

	@PostConstruct
	public void initData() {
		loadConferences();
	}
	
	public String conferenceDetails(){
		return "conferenceDetails";
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

}
