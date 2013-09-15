package com.prodyna.academy.pac.speaker.service;

import java.util.List;

import com.prodyna.academy.pac.speaker.model.Speaker;

public interface SpeakerService {
	
	public Speaker createSpeaker(Speaker room);
	
	public Speaker updateSpeaker(Speaker room);
	
	public void deleteSpeaker(Speaker room);
	
	public Speaker findSpeaker(int id);

	public List<Speaker> findAllSpeakers();

}
