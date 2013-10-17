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

import com.prodyna.academy.pac.base.monitoring.interceptor.PerformanceLogged;
import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.room.service.RoomService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the room
 * table.
 */
@Path("/rooms")
@RequestScoped
@PerformanceLogged
public class RoomRESTService {
	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private RoomService repository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Room> listAllMembers() {
		return repository.getRooms();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@PathParam("id") int id) {
		try {
			Room room = repository.getRoom(id);
			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(room);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "+ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);
			
		}
		
	}

	@GET
	@Path("/create/{name}/{capacity:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@PathParam("name") String name,
			@PathParam("capacity") int capacity) {
		Room room = new Room(name, capacity);
		return create(room);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Room room) {
		try {
			validateRoom(room);
			Room rs = repository.createRoom(room);

			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(rs);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "+ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);
			
		}

	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Room room) {
		try {
			validateRoom(room);
			Room rs = repository.updateRoom(room);

			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(rs);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "+ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce.getConstraintViolations());
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
			Room room = repository.deleteRoom(id);

			// // Create an "ok" response
			return RestResponseBuilder.buildOkResponse(room);
		} catch (ConstraintViolationException ce) {
			log.severe("Constraint violations have been found: "+ce.getConstraintViolations());
			return RestResponseBuilder.buildViolationResponse(ce.getConstraintViolations());
		} catch (Exception e) {
			log.severe(e.getMessage());
			// Handle generic exceptions
			return RestResponseBuilder.buildErrorResponse(e);
			
		}
	}

	

	/**
	 * Validates a room instance
	 * @param room
	 * @throws ConstraintViolationException
	 * @throws ValidationException
	 */
	private void validateRoom(Room room) throws ConstraintViolationException,
			ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Room>> violations = validator.validate(room);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
					new HashSet<ConstraintViolation<?>>(violations));
		}

	}

	

}
