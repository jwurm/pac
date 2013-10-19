/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prodyna.academy.pac.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
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

import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.conference.service.ConferenceService;
import com.prodyna.academy.pac.talk.model.Talk;
import com.prodyna.academy.pac.talk.service.TalkService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * conferences table.
 */
@Path("/conferences")
@RequestScoped
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
	public Response listAllMembers() {

		try {
			List<Conference> findAllConferences = conferenceService
					.findAllConferences();
			/*
			 * remove backwards reference. not very nice, but it saves us from
			 * using jackson specific annotations and the effect is the same.
			 */
			for (Conference conference : findAllConferences) {
				for(Talk talk:talkService.getTalksByConference(conference.getId())){
					talk.setConference(null);
				}
				
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

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@PathParam("id") int id) {
		try {
			Conference conference = conferenceService.getCompleteConference(id);
			
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
	public Response create(Conference conference) {

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
	public Response update(Conference conference) {
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
	public Response delete(@PathParam("id") Integer id) {
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
