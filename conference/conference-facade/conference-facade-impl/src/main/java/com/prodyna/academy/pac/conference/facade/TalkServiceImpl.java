package com.prodyna.academy.pac.conference.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.joda.time.Instant;
import org.joda.time.Interval;

import com.prodyna.academy.pac.conference.base.exception.BusinessException;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.conference.service.ConferenceCRUDService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

@Stateless
@PerformanceLogged
@ServiceLogged
public class TalkServiceImpl implements TalkService {

	@Inject
	private ConferenceCRUDService conference;

	@Inject
	private TalkCRUDService talkservice;

	@Override
	public List<Talk> getAllTalks() {
		return talkservice.getAllTalks();
	}

	@Override
	public List<Talk> getTalksBySpeaker(int speakerId) {
		return talkservice.getTalksBySpeaker(speakerId);
	}

	@Override
	public List<Talk> getTalksByRoom(int roomid) {
		return talkservice.getTalksByRoom(roomid);
	}

	@Override
	public void assignSpeaker(Talk talk, Speaker speaker) {
		validateSpeakerAvailability(talk, speaker);
		talkservice.assignSpeaker(talk, speaker);

	}

	@Override
	public void unassignSpeaker(Talk talk, Speaker speaker) {
		talkservice.unassignSpeaker(talk, speaker);

	}

	@Override
	public Talk createTalk(Talk talk) {
		validateTalk(talk);
		return talkservice.createTalk(talk);
	}

	@Override
	public Talk updateTalk(Talk talk) {
		validateTalk(talk);
		return talkservice.updateTalk(talk);
	}

	@Override
	public Talk deleteTalk(int id) {
		List<Speaker> speakersByTalk = talkservice.getSpeakersByTalk(id);
		if (!speakersByTalk.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Speaker speaker : speakersByTalk) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(speaker.getName());
			}
			throw new BusinessException(
					"Cannot delete the talk due to assigned speakers: "
							+ sb.toString());
		}
		return talkservice.deleteTalk(id);
	}

	@Override
	public Talk getTalk(int id) {
		return talkservice.getTalk(id);
	}

	@Override
	public List<Talk> getTalksByConference(int conferenceId) {
		return talkservice.getTalksByConference(conferenceId);
	}

	@Override
	public List<Speaker> getSpeakersByTalk(int talkId) {
		return talkservice.getSpeakersByTalk(talkId);
	}

	/**
	 * Validates the data of the talk, checks for overlapping dates of room,
	 * speaker etc.
	 * 
	 * @param talk
	 */
	private void validateTalk(Talk talk) {
		validateConferenceInterval(talk);

		validateRoomAvailability(talk);

		validateSpeakerAvailability(talk);

	}

	/**
	 * Checks if the talk fits into the conference interval.
	 * 
	 * @param talk
	 * @thows BusinessException if it's outside of the conference
	 */
	private void validateConferenceInterval(Talk talk) {
		// read conference and room to have up to date data

		Integer conferenceId = talk.getConference().getId();
		Conference conf = conference.getConference(conferenceId);
		if (conf == null) {
			throw new BusinessException("no conference found for id"
					+ conferenceId);
		}

		// validate conference date
		Interval conferenceInterval = conf.buildInterval();
		boolean conferenceIntervalOk = conferenceInterval.contains(talk
				.buildInterval());
		if (!conferenceIntervalOk) {
			throw new BusinessException(
					"Talk is set outside of the duration of the conference! "
							+ conf.getStart() + " to " + conf.getEnd().toString());
		}
	}

	/**
	 * Checks if the room is available at the time of the talk.
	 * 
	 * @param talk
	 * @thows BusinessException if it's not available
	 */
	private void validateRoomAvailability(Talk talk) {
		Interval talkInterval = talk.buildInterval();
		// validate room availability
		List<Talk> roomTalks = getTalksByRoom(talk.getRoom().getId());
		for (Talk currTalk : roomTalks) {
			if (currTalk.getId().equals(talk.getId())) {
				// if the talk already exists, don't validate against itself
				continue;
			}

			Interval otherRoomTalkInterval = currTalk.buildInterval();
			if (otherRoomTalkInterval.overlaps(talkInterval)) {
				throw new BusinessException(
						"The designated room is already occupied by "
								+ currTalk.getName() + " at that time.");
			}
		}
	}

	/**
	 * Checks if the speaker is available for the talk
	 * 
	 * @thows BusinessException if it's not available
	 * @param talk
	 */
	private void validateSpeakerAvailability(Talk talk) {
		if (talk.getId() == null) {
			// if the talk hasn't been persisted yet, then it cannot have any
			// speakers yet anyway
			return;
		}
		Interval talkInterval = talk.buildInterval();
		List<Speaker> speakers = getSpeakersByTalk(talk.getId());
		for (Speaker speaker : speakers) {
			// no one of the speakers is allowed to have a talk at the same time
			// as this one, except for this talk.

			List<Talk> speakerTalks = getTalksBySpeaker(speaker.getId());
			for (Talk currTalk : speakerTalks) {
				if (currTalk.getId().equals(talk.getId())) {
					// if the talk already exists, don't validate against itself
					continue;
				}

				Interval otherSpeakerTalkInterval = new Interval(new Instant(
						currTalk.getDatetime()), new Instant(
						currTalk.buildEndDateTime()));
				if (otherSpeakerTalkInterval.overlaps(talkInterval)) {
					throw new BusinessException("The designated speaker "
							+ speaker.getName() + " is already occupied by "
							+ currTalk.getName() + " at that time.");
				}
			}
		}
	}

	/**
	 * Checks if the speaker is available for the talk
	 * 
	 * @thows BusinessException if it's not available
	 * @param talk
	 * @param speaker
	 */
	private void validateSpeakerAvailability(Talk talk, Speaker speaker) {
		if (talk.getId() == null) {
			// if the talk hasn't been persisted yet, then it cannot have any
			// speakers yet anyway
			return;
		}
		Interval talkInterval = talk.buildInterval();
		// no one of the speakers is allowed to have a talk at the same time
		// as this one, except for this talk.

		List<Talk> speakerTalks = getTalksBySpeaker(speaker.getId());
		for (Talk currTalk : speakerTalks) {
			if (currTalk.getId().equals(talk.getId())) {
				// if the talk already exists, don't validate against itself
				continue;
			}

			Interval otherSpeakerTalkInterval = new Interval(new Instant(
					currTalk.getDatetime()), new Instant(
					currTalk.buildEndDateTime()));
			if (otherSpeakerTalkInterval.overlaps(talkInterval)) {
				throw new BusinessException(
						"The designated speaker is already occupied by "
								+ currTalk.getName() + " at that time.");
			}
		}
	}

}
