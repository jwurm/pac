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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.conference.service.TalkService;
import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.speaker.service.SpeakerService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * speakers table.
 */
@Path("/talks")
@RequestScoped
public class TalkRESTService {
	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private TalkService repository;

	@Inject
	private SpeakerService speakerService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllMembers() {
		try {
			List<Talk> talks = repository.getTalks();
			for (Talk talk : talks) {
				cleanTalk(talk);
			}
			return RestResponseBuilder.buildOkResponse(talks);
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
			Talk talk = repository.getTalk(id);
			// // Create an "ok" response
			cleanTalk(talk);
			return RestResponseBuilder.buildOkResponse(talk);
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	@GET
	@Path("/assignspeaker/{talkId:[0-9][0-9]*}/{speakerId:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response assignSpeaker(@PathParam("talkId") int talkid,
			@PathParam("speakerId") int speakerId) {
		try {
			Talk talk = repository.getTalk(talkid);
			if (talk == null) {
				throw new Exception("No talk found for id " + talkid);
			}
			Speaker speaker = speakerService.findSpeaker(speakerId);
			if (speaker == null) {
				throw new Exception("No speaker found for id " + speaker);
			}
			repository.assignSpeaker(talk, speaker);
			// // Create an "ok" response
			cleanTalk(talk);
			return RestResponseBuilder.buildOkResponse(talk);
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	/**
	 * Removes backwards references to the conference in the talk to avoid a the
	 * JSON marshaller to run into a circular path.
	 * 
	 * @param talk
	 */
	private void cleanTalk(Talk talk) {
		talk.setConference(null);
	}

	@GET
	@Path("/unassignspeaker/{talkId:[0-9][0-9]*}/{speakerId:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response unassignSpeaker(@PathParam("talkid") int talkid,
			@PathParam("speakerId") int speakerId) {
		try {
			Talk talk = repository.getTalk(talkid);
			if (talk == null) {
				throw new Exception("No talk found for id " + talkid);
			}
			Speaker speaker = speakerService.findSpeaker(speakerId);
			if (speaker == null) {
				throw new Exception("No speaker found for id " + speaker);
			}
			repository.unassignSpeaker(talk, speaker);
			// // Create an "ok" response
			cleanTalk(talk);
			return RestResponseBuilder.buildOkResponse(talk);
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Talk talk) {
		try {
			validateTalk(talk);
			Talk rs = repository.createTalk(talk);

			// // Create an "ok" response
			cleanTalk(rs);
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
	public Response update(Talk talk) {
		try {
			validateTalk(talk);
			Talk rs = repository.updateTalk(talk);
			// // Create an "ok" response
			cleanTalk(rs);
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
			Talk talk = repository.deleteTalk(id);
			// // Create an "ok" response
			cleanTalk(talk);
			return RestResponseBuilder.buildOkResponse(talk);
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);

		}
	}

	/**
	 * Validates a talk instance
	 * 
	 * @param talk
	 * @throws ConstraintViolationException
	 * @throws ValidationException
	 */
	private void validateTalk(Talk talk) throws ConstraintViolationException,
			ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Talk>> violations = validator.validate(talk);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
					new HashSet<ConstraintViolation<?>>(violations));
		}

	}

}
