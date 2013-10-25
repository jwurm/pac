package com.prodyna.academy.pac.conference.talk.mdb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Handles talk change messages by logging it and keeping it in a list
 * 
 * @author Jens Wurm
 * 
 */
public class TalkChangeMessageHandlerTestImpl implements TalkChangeMessageHandler {

	@Inject
	private Logger log;
	
	private static List<String> messages=new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.talk.mdb.TalkChangeMessageHandler#
	 * handleMessage(java.lang.String)
	 */
	@Override
	public void handleMessage(String string) {
		log.info(string);
		messages.add(string);
	}
	
	public static List<String> getMessages() {
		return messages;
	}

}
