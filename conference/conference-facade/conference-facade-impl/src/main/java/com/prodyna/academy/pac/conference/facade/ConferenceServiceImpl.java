package com.prodyna.academy.pac.conference.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.joda.time.Interval;

import com.prodyna.academy.pac.conference.base.BusinessException;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.conference.service.ConferenceCRUDService;
import com.prodyna.academy.pac.conference.facade.service.ConferenceService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

@Stateless
@PerformanceLogged
@ServiceLogged
public class ConferenceServiceImpl implements ConferenceService {
	@Inject
	private ConferenceCRUDService conferenceService;

	@Inject
	private TalkCRUDService talkService;

	@Override
	public Conference getConference(int conferenceId) {
		return conferenceService.getConference(conferenceId);
	}

	@Override
	public Conference createConference(Conference conference) {
		return conferenceService.createConference(conference);
	}

	@Override
	public Conference updateConference(Conference conference) {
		validateConference(conference);
		return conferenceService.updateConference(conference);
	}

	/**
	 * Validates the date fields of the conference.
	 * 
	 * @param conferenceF
	 * @throws BusinessException
	 *             if the date fields are invalid
	 */
	private void validateConference(Conference conference)
			throws BusinessException {
		if (conference.getEnd().before(conference.getStart())) {
			throw new BusinessException(
					"Conference start date is after its end date.");
		}
		List<Talk> talks = talkService.getTalksByConference(conference.getId());
		Interval interval = conference.buildInterval();
		for (Talk talk : talks) {
			Interval talkInterval = talk.buildInterval();
			boolean talkIntervalInsideConferenceInterval = interval
					.contains(talkInterval);
			if (!talkIntervalInsideConferenceInterval) {
				throw new BusinessException(
						"The conference has talks which are outside of the set conference duration: "
								+ talk.getName());
			}
		}
	}

	@Override
	public Conference deleteConference(int conferenceId) {
		List<Talk> talksByConference = talkService
				.getTalksByConference(conferenceId);
		if (!talksByConference.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Talk talk : talksByConference) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(talk.getName());
			}
			throw new BusinessException(
					"Cannot delete the conference due to assigned talks: "
							+ sb.toString());
		}
		return conferenceService.deleteConference(conferenceId);
	}

	@Override
	public List<Conference> getAllConferences() {
		return conferenceService.getAllConferences();
	}

}
