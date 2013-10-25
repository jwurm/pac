package com.prodyna.academy.pac.conference.web.util;

/**
 * 
 * Extracts the root error message from an exception
 * @author jwurm
 *
 */
public class RootErrorMessageReader {

	/**
	 * Extracts the root error message
	 * @param e Exception
	 * @return Error message
	 */
	public static String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Registration failed. See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}
	
		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

}
