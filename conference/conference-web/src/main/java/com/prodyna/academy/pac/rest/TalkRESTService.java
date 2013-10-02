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

import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.conference.service.TalkService;

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

	// @Inject
	// private Validator validator;

	@Inject
	private TalkService repository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Talk> listAllMembers() {
		return repository.getTalks();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Talk find(@PathParam("id") int id) {
		Talk member = repository.findTalk(id);
		if (member == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return member;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Talk create(Talk room) {
		Talk rs = repository.createTalk(room);
		if (rs == null) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		return rs;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Talk update(Talk room) {
		Talk rs = repository.updateTalk(room);
		if (rs == null) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		return rs;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Talk delete(@PathParam("id") Integer id) {
		try {
			Talk room = repository.deleteTalk(id);
			return room;
		} catch (Exception e) {
			log.severe(e.getMessage());
			return null;
		}
	}
	
}
