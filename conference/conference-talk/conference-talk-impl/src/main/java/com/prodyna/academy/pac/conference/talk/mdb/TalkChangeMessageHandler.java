package com.prodyna.academy.pac.conference.talk.mdb;

import javax.jms.TextMessage;

/**
 * Handles talk change messages
 * 
 * @author Jens Wurm
 *
 */
public interface TalkChangeMessageHandler {

	/**
	 * Processes the message
	 * @param string
	 */
	public void handleMessage(String string);

}
