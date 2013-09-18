package com.prodyna.academy.pac.conference.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.prodyna.academy.pac.speaker.model.Speaker;

@Entity
@Table(name = "talk_speaker_assignment")
public class TalkSpeakerAssignment {
	@Id
	@GeneratedValue
	private int id;
	
	@Transient
//	@ManyToOne()
//	@JoinColumn(name="speaker_id", referencedColumnName="id")
	private Speaker speaker;
	
	@Transient
//	@ManyToOne()
//	@JoinColumn(name="talk_id", referencedColumnName="id")
	private Talk talk;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	public Talk getTalk() {
		return talk;
	}

	public void setTalk(Talk talk) {
		this.talk = talk;
	}

}
