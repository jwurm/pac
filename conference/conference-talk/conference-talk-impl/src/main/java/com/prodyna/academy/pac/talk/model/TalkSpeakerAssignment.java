package com.prodyna.academy.pac.talk.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.prodyna.academy.pac.speaker.model.Speaker;

/**
 * The Class TalkSpeakerAssignment.
 */
@Entity
@Table(name = "talk_speaker_assignment")
@NamedQueries({
@NamedQuery(name = TalkSpeakerAssignment.FIND_BY_SPEAKER_AND_TALK, query = "select t from TalkSpeakerAssignment t where speaker_id=:speakerId and talk_id=:talkId"),
@NamedQuery(name = TalkSpeakerAssignment.FIND_BY_SPEAKER, query = "select t from TalkSpeakerAssignment t where speaker_id=:speakerId"),
@NamedQuery(name = TalkSpeakerAssignment.FIND_BY_TALK, query = "select t from TalkSpeakerAssignment t where talk_id=:talkId")
})
public class TalkSpeakerAssignment {
	
	/** The Constant FIND_BY_SPEAKER_AND_TALK. */
	public static final String FIND_BY_SPEAKER_AND_TALK = "TalkSpeakerAssignment.FIND_BY_SPEAKER_AND_TALK";
	
	/** The Constant FIND_BY_SPEAKER. */
	public static final String FIND_BY_SPEAKER = "TalkSpeakerAssignment.FIND_BY_SPEAKER";
	
	/** The Constant FIND_BY_TALK. */
	public static final String FIND_BY_TALK = "TalkSpeakerAssignment.FIND_BY_TALK";
	
	/** The id. */
	@Id
	@GeneratedValue
	private int id;

	/** The speaker. */
	@ManyToOne()
	@JoinColumn(name = "speaker_id", referencedColumnName = "id")
	private Speaker speaker;

	/** The talk. */
	@ManyToOne()
	@JoinColumn(name = "talk_id", referencedColumnName = "id")
	private Talk talk;

	/**
	 * Instantiates a new talk speaker assignment.
	 *
	 * @param talk the talk
	 * @param speaker the speaker
	 */
	public TalkSpeakerAssignment(Talk talk, Speaker speaker) {
		super();
		this.talk = talk;
		this.speaker = speaker;
	}

	/**
	 * Instantiates a new talk speaker assignment.
	 */
	public TalkSpeakerAssignment() {
		super();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the speaker.
	 *
	 * @return the speaker
	 */
	public Speaker getSpeaker() {
		return speaker;
	}

	/**
	 * Sets the speaker.
	 *
	 * @param speaker the new speaker
	 */
	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	/**
	 * Gets the talk.
	 *
	 * @return the talk
	 */
	public Talk getTalk() {
		return talk;
	}

	/**
	 * Sets the talk.
	 *
	 * @param talk the new talk
	 */
	public void setTalk(Talk talk) {
		this.talk = talk;
	}

}
