package com.prodyna.academy.pac.conference.talk.mdb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.prodyna.academy.pac.conference.talk.service.decorators.TalkChangeNotificationDecorator;

/**
 * Message-Driven Bean implementation class for: TalkChangeMDB Listens on the
 * queue that is written by the TalkChangeNotificationDecorator and collects the
 * messages to be evaluated in unit tests.
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = TalkChangeNotificationDecorator.QUEUE_NAME) }, mappedName = TalkChangeNotificationDecorator.QUEUE_NAME)
public class TalkChangeTestMDB implements MessageListener {

	@Inject
	private Logger log;

	private static List<String> messages = new ArrayList<String>();

	/**
	 * Default constructor.
	 */
	public TalkChangeTestMDB() {
		super();
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				// read the message and write it into the log
				String text = ((TextMessage) message).getText();
				log.info("Talk change queue message arrived: " + text);
				messages.add(text);
			} catch (JMSException e) {
				log.severe(e.toString());
			}
		} else {
			// any unexpected message type should result in an noticeable
			// exception
			throw new IllegalArgumentException("Unexpected message type: "
					+ message.toString());
		}

	}

	public static List<String> getMessages() {
		return messages;

	}

}
