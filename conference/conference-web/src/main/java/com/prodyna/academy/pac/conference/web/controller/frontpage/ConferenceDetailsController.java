package com.prodyna.academy.pac.conference.web.controller.frontpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.facade.service.ConferenceService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.talk.model.Talk;

@ManagedBean(name = "conferenceDetailsController")
@ViewScoped
public class ConferenceDetailsController {

	private Conference conference;

	private int conferenceId;


	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private ConferenceService conferenceService;
	
	@Inject
	private TalkService talkService;

	private HtmlDataTable dataTable;

	private List<TalksByDay> talkList=new ArrayList<TalksByDay>();
	

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
		Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
		String string = requestParameterMap.get("cId");
		if(string==null){
			return;
		}
		conferenceId = Integer.valueOf(string);
		conference = conferenceService.getConference(conferenceId);
		
		List<Talk> talks = talkService.getTalksByConference(conferenceId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//sorted map so that we get the right order simply by using its iterator
		SortedMap<String, TalksByDay> talksByDay=new TreeMap<String, TalksByDay>();
		for (Talk talk : talks) {
			String dateString = sdf.format(talk.getDatetime());
			TalksByDay talksbd = talksByDay.get(dateString);
			if(talksbd==null){
				talksbd=new TalksByDay(dateString);
				talksByDay.put(dateString, talksbd);
			}
			talksbd.getTalks().add(talk);
		}
		
		talkList.addAll(talksByDay.values());
		
	}
	
	public String talkDetails(){
//		Object rowData = dataTable.getRowData();
		return "talkDetails";
	}
	

}
