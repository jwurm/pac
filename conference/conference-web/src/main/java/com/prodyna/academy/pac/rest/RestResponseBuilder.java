package com.prodyna.academy.pac.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * Helper class which constructs REST Response objects using either a response
 * object or exceptions
 * 
 * @author jwurm
 * 
 */
public class RestResponseBuilder {
	

	/**
	 * Creates a JAX-RS "ok" response with the passed object
	 * 
	 * @param returnObject
	 * @return Response containing returnObject
	 */
	public static Response buildOkResponse(Object returnObject) {
		ResponseBuilder builder = Response.ok();
		builder.entity(returnObject);
		return builder.build();
	}

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation
	 * fields, and their message. This can then be used by clients to show
	 * violations.
	 * 
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	public static Response buildViolationResponse(
			Set<ConstraintViolation<?>> violations) {

		Map<String, String> responseObj = new HashMap<String, String>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(),
					violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj)
				.build();
	}

	/**
	 * Creates a JAX-RS error response containing the exception data
	 * 
	 * @param Exception
	 *            the exception that is supposed to be reported to the caller
	 * @return JAX-RS Response that contains the exception data
	 */
	public static Response buildErrorResponse(Exception e) {
		Map<String, String> responseObj = new HashMap<String, String>();
		responseObj.put("error", e.getMessage());
		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj)
				.build();
	}

}
