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
import com.prodyna.academy.pac.conference.facade.service.SpeakerService;
import com.prodyna.academy.pac.conference.rest.util.RestResponseBuilder;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerCRUDService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * speakers table.
 */
@Path("/speakers")
@ApplicationScoped
@PerformanceLogged
@ServiceLogged
public class SpeakerRESTService {
	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private SpeakerService repository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSpeakers() {

		try {
			List<Speaker> findAllSpeakers = repository.getAllSpeakers();
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(findAllSpeakers);
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

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSpeaker(@PathParam("id") int id) {
		try {
			Speaker speaker = repository.getSpeaker(id);
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(speaker);
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

	@GET
	@Path("/create/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createSpeaker(@PathParam("name") String name) {
		try {
			Speaker speaker = repository.createSpeaker(new Speaker(name, ""));
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(speaker);
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
	public Response createSpeaker(Speaker speaker) {
		try {
			validateSpeaker(speaker);
			Speaker rs = repository.createSpeaker(speaker);
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
	public Response updateSpeaker(Speaker speaker) {
		try {
			validateSpeaker(speaker);
			Speaker rs = repository.updateSpeaker(speaker);
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
	public Response deleteSpeaker(@PathParam("id") Integer id) {
		try {
			Speaker room = repository.deleteSpeaker(id);
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(room);
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
	 * Validates a speaker instance
	 * 
	 * @param speaker
	 * @throws ConstraintViolationException
	 * @throws ValidationException
	 */
	private void validateSpeaker(Speaker speaker)
			throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Speaker>> violations = validator
				.validate(speaker);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
					new HashSet<ConstraintViolation<?>>(violations));
		}

	}

}
