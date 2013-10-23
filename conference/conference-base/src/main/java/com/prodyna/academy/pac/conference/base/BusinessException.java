package com.prodyna.academy.pac.conference.base;

/**
 * Generic business exception class, used as a superclass for all non-technical
 * exceptions that may occur
 * 
 * @author Jens Wurm
 * 
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message);
	}

}
