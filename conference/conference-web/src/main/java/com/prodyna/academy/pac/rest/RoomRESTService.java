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

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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

import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.room.service.RoomService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * room table.
 */
@Path("/rooms")
@RequestScoped
public class RoomRESTService {
	@Inject
	private Logger log;

	// @Inject
	// private Validator validator;

	@Inject
	private RoomService repository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Room> listAllMembers() {
		return repository.findAllRooms();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Room find(@PathParam("id") int id) {
		Room member = repository.findRoom(id);
		if (member == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return member;
	}

	@GET
	@Path("/create/{name}/{capacity:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Room create(@PathParam("name") String name,
			@PathParam("capacity") int capacity) {
		Room room = repository.createRoom(new Room(name, capacity));
		if (room == null) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		return room;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Room create(Room room) {
		Room rs = repository.createRoom(room);
		if (rs == null) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		return rs;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Room update(Room room) {
		Room rs = repository.updateRoom(room);
		if (rs == null) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		return rs;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Room delete(@PathParam("id") Integer id) {
		try {
			Room room = repository.deleteRoom(id);
			return room;
		} catch (Exception e) {
			log.severe(e.getMessage());
			return null;
		}
	}
	//
	// /**
	// * Creates a new member from the values provided. Performs validation, and
	// will return a JAX-RS response with either 200 ok,
	// * or with a map of fields, and related errors.
	// */
	// @POST
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response createMember(Member member) {
	//
	// Response.ResponseBuilder builder = null;
	//
	// try {
	// // Validates member using bean validation
	// validateMember(member);
	//
	// registration.register(member);
	//
	// // Create an "ok" response
	// builder = Response.ok();
	// } catch (ConstraintViolationException ce) {
	// // Handle bean validation issues
	// builder = createViolationResponse(ce.getConstraintViolations());
	// } catch (ValidationException e) {
	// // Handle the unique constrain violation
	// Map<String, String> responseObj = new HashMap<String, String>();
	// responseObj.put("email", "Email taken");
	// builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
	// } catch (Exception e) {
	// // Handle generic exceptions
	// Map<String, String> responseObj = new HashMap<String, String>();
	// responseObj.put("error", e.getMessage());
	// builder =
	// Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	// }
	//
	// return builder.build();
	// }
	//
	// /**
	// * <p>
	// * Validates the given Member variable and throws validation exceptions
	// based on the type of error. If the error is standard
	// * bean validation errors then it will throw a
	// ConstraintValidationException with the set of the constraints violated.
	// * </p>
	// * <p>
	// * If the error is caused because an existing member with the same email
	// is registered it throws a regular validation
	// * exception so that it can be interpreted separately.
	// * </p>
	// *
	// * @param member Member to be validated
	// * @throws ConstraintViolationException If Bean Validation errors exist
	// * @throws ValidationException If member with the same email already
	// exists
	// */
	// private void validateMember(Member member) throws
	// ConstraintViolationException, ValidationException {
	// // Create a bean validator and check for issues.
	// Set<ConstraintViolation<Member>> violations = validator.validate(member);
	//
	// if (!violations.isEmpty()) {
	// throw new ConstraintViolationException(new
	// HashSet<ConstraintViolation<?>>(violations));
	// }
	//
	// // Check the uniqueness of the email address
	// if (emailAlreadyExists(member.getEmail())) {
	// throw new ValidationException("Unique Email Violation");
	// }
	// }
	//
	// /**
	// * Creates a JAX-RS "Bad Request" response including a map of all
	// violation fields, and their message. This can then be used
	// * by clients to show violations.
	// *
	// * @param violations A set of violations that needs to be reported
	// * @return JAX-RS response containing all violations
	// */
	// private Response.ResponseBuilder
	// createViolationResponse(Set<ConstraintViolation<?>> violations) {
	// log.fine("Validation completed. violations found: " + violations.size());
	//
	// Map<String, String> responseObj = new HashMap<String, String>();
	//
	// for (ConstraintViolation<?> violation : violations) {
	// responseObj.put(violation.getPropertyPath().toString(),
	// violation.getMessage());
	// }
	//
	// return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	// }
	//
	// /**
	// * Checks if a member with the same email address is already registered.
	// This is the only way to easily capture the
	// * "@UniqueConstraint(columnNames = "email")" constraint from the Member
	// class.
	// *
	// * @param email The email to check
	// * @return True if the email already exists, and false otherwise
	// */
	// public boolean emailAlreadyExists(String email) {
	// Member member = null;
	// try {
	// member = repository.findByEmail(email);
	// } catch (NoResultException e) {
	// // ignore
	// }
	// return member != null;
	// }
}