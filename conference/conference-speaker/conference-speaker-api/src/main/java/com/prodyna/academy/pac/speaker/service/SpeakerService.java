package com.prodyna.academy.pac.speaker.service;

import java.util.List;

import com.prodyna.academy.pac.speaker.model.Speaker;

public interface SpeakerService {
	
	public Speaker createSpeaker(Speaker speaker);
	
	public Speaker updateSpeaker(Speaker speaker);
	
	public void deleteSpeaker(Speaker speaker);
	
	public Speaker findSpeaker(int id);

	public List<Speaker> findAllSpeakers();

}
