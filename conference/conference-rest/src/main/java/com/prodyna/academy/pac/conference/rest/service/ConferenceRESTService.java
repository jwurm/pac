package com.prodyna.academy.pac.conference.rest.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.prodyna.academy.pac.conference.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.conference.base.monitoring.interceptor.ServiceLogged;
import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.facade.service.ConferenceService;
import com.prodyna.academy.pac.conference.facade.service.TalkService;
import com.prodyna.academy.pac.conference.rest.util.RestResponseBuilder;
import com.prodyna.academy.pac.conference.talk.model.Talk;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * conferences table.
 */
@Path("/conferences")
@ApplicationScoped
@PerformanceLogged
@ServiceLogged
public class ConferenceRESTService {

	@Inject
	private Validator validator;

	@Inject
	private Logger log;

	@Inject
	private ConferenceService conferenceService;

	@Inject
	private TalkService talkService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllConferences() {

		try {
			List<Conference> findAllConferences = conferenceService
					.getAllConferences();
			for (Conference conference : findAllConferences) {
				cleanConference(conference);
			}
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(findAllConferences);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "
					+ ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce
					.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	/**
	 * remove backwards reference. not very nice, but it saves us from using
	 * jackson specific annotations and the effect is the same.
	 */
	private void cleanConference(Conference conference) {

		for (Talk talk : talkService.getTalksByConference(conference.getId())) {
			talk.setConference(null);
		}
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConference(@PathParam("id") int id) {
		try {
			Conference conference = conferenceService.getConference(id);

			/*
			 * remove backwards reference. not very nice, but it saves us from
			 * using jackson specific annotations and the effect is the same.
			 */
			for (Talk talk : talkService.getTalksByConference(id)) {
				talk.setConference(null);
			}

			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(conference);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "
					+ ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce
					.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createConference(Conference conference) {

		try {
			validateConference(conference);
			Conference rs = conferenceService.createConference(conference);
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(rs);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "
					+ ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce
					.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateConference(Conference conference) {
		try {
			validateConference(conference);
			Conference rs = conferenceService.updateConference(conference);
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(rs);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "
					+ ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce
					.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response deleteConference(@PathParam("id") Integer id) {
		try {
			Conference conference = conferenceService.deleteConference(id);
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(conference);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "
					+ ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce
					.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	/**
	 * Validates a conference instance.
	 * 
	 * @param conference
	 * @throws ConstraintViolationException
	 * @throws ValidationException
	 */
	private void validateConference(Conference conference)
			throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Conference>> violations = validator
				.validate(conference);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
					new HashSet<ConstraintViolation<?>>(violations));
		}

	}

}
