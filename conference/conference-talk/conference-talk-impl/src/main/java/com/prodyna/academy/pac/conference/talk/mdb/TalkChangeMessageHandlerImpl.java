package com.prodyna.academy.pac.conference.talk.mdb;

import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Handles talk change messages by logging it
 * 
 * @author Jens Wurm
 * 
 */
public class TalkChangeMessageHandlerImpl implements TalkChangeMessageHandler {

	@Inject
	private Logger log;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.prodyna.academy.pac.conference.talk.mdb.TalkChangeMessageHandler#
	 * handleMessage(java.lang.String)
	 */
	@Override
	public void handleMessage(String string) {
		log.info("Talk change queue message arrived: " + string);
	}

}
