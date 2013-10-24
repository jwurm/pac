package com.prodyna.academy.pac.conference.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.prodyna.academy.pac.conference.base.BusinessException;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.facade.service.SpeakerService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

/**
 * Implementation of the speaker service
 * @author Jens Wurm
 *
 */
@PerformanceLogged
@ServiceLogged
@Stateless
public class SpeakerServiceImpl implements SpeakerService {

	@Inject
	private TalkCRUDService talkService;

	@Inject
	private SpeakerCRUDService speakerService;

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.facade.service.SpeakerService#createSpeaker(com.prodyna.academy.pac.conference.speaker.model.Speaker)
	 */
	@Override
	public Speaker createSpeaker(Speaker speaker) {
		return speakerService.createSpeaker(speaker);
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.facade.service.SpeakerService#updateSpeaker(com.prodyna.academy.pac.conference.speaker.model.Speaker)
	 */
	@Override
	public Speaker updateSpeaker(Speaker speaker) {
		return speakerService.updateSpeaker(speaker);
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.facade.service.SpeakerService#deleteSpeaker(int)
	 */
	@Override
	public Speaker deleteSpeaker(int id) {
		List<Talk> talksBySpeaker = talkService.getTalksBySpeaker(id);
		if (!talksBySpeaker.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Talk talk : talksBySpeaker) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(talk.getName());
			}
			throw new BusinessException(
					"Cannot delete the speaker due to assigned talks: "
							+ sb.toString());
		}
		return speakerService.deleteSpeaker(id);
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.facade.service.SpeakerService#getSpeaker(int)
	 */
	@Override
	public Speaker getSpeaker(int speakerId) {
		return speakerService.getSpeaker(speakerId);
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.facade.service.SpeakerService#getSpeakers()
	 */
	@Override
	public List<Speaker> getAllSpeakers() {
		return speakerService.getAllSpeakers();
	}

}
