package com.prodyna.academy.pac.conference.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.prodyna.academy.pac.speaker.model.Speaker;

@Entity
@Table(name = "talk_speaker_assignment")
@NamedQueries({
@NamedQuery(name = TalkSpeakerAssignment.FIND_BY_SPEAKER_AND_TALK, query = "select t from TalkSpeakerAssignment t where speaker_id=:speakerId and talk_id=:talkId"),
@NamedQuery(name = TalkSpeakerAssignment.FIND_BY_SPEAKER, query = "select t from TalkSpeakerAssignment t where speaker_id=:speakerId"),
@NamedQuery(name = TalkSpeakerAssignment.FIND_BY_TALK, query = "select t from TalkSpeakerAssignment t where talk_id=:talkId")
})
public class TalkSpeakerAssignment {
	public static final String FIND_BY_SPEAKER_AND_TALK = "TalkSpeakerAssignment.FIND_BY_SPEAKER_AND_TALK";
	public static final String FIND_BY_SPEAKER = "TalkSpeakerAssignment.FIND_BY_SPEAKER";
	public static final String FIND_BY_TALK = "TalkSpeakerAssignment.FIND_BY_TALK";
	@Id
	@GeneratedValue
	private int id;

	@ManyToOne()
	@JoinColumn(name = "speaker_id", referencedColumnName = "id")
	private Speaker speaker;

	@ManyToOne()
	@JoinColumn(name = "talk_id", referencedColumnName = "id")
	private Talk talk;

	public TalkSpeakerAssignment(Talk talk, Speaker speaker) {
		super();
		this.talk = talk;
		this.speaker = speaker;
	}

	public TalkSpeakerAssignment() {
		super();
	}

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
