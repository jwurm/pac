package com.prodyna.academy.pac.conference.service;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.speaker.model.Speaker;

public interface TalkService {
	
	public List<Talk> getTalks();
	
	public List<Talk> getTalksBySpeaker(Speaker speaker);

	public List<Talk> getByRoom(Room room);

	void assignSpeaker(Talk talk, Speaker speaker);

	void unassignSpeaker(Talk talk, Speaker speaker);

	public Talk createTalk(Talk talk);

	public Talk updateTalk(Talk talk);

	public Talk deleteTalk(int id);
	
	public Talk findTalk(int id);
	
	public List<Speaker> findSpeakers(int talkId);
	
	

}
