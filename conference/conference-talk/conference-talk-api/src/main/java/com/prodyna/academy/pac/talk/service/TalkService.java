package com.prodyna.academy.pac.talk.service;

import java.util.List;

import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.talk.model.Talk;

public interface TalkService {
	
	public List<Talk> getTalks();
	
	public List<Talk> getTalksBySpeaker(int speakerId);

	public List<Talk> getByRoom(int roomid);

	void assignSpeaker(Talk talk, Speaker speaker);

	void unassignSpeaker(Talk talk, Speaker speaker);

	public Talk createTalk(Talk talk);

	public Talk updateTalk(Talk talk);

	public Talk deleteTalk(int id);
	
	public Talk getTalk(int id);
	
	public List<Talk> getTalksByConference(int id);
	
	public List<Speaker> getSpeakersByTalk(int talkId);
	
	

}
