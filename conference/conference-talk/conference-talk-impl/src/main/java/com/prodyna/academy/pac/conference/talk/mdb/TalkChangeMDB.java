package com.prodyna.academy.pac.conference.talk.mdb;

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
 * queue that is written by the TalkChangeNotificationDecorator
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = TalkChangeNotificationDecorator.QUEUE_NAME) }, mappedName = TalkChangeNotificationDecorator.QUEUE_NAME)
public class TalkChangeMDB implements MessageListener {

	@Inject
	private Logger log;
	
	@Inject
	private TalkChangeMessageHandler handler;

	/**
	 * Default constructor.
	 */
	public TalkChangeMDB() {
		super();
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				handler.handleMessage(((TextMessage)message).getText());
			} catch (JMSException e) {
				log.severe(e.toString());
			}
		} else {
			//any unexpected message type should result in an noticeable exception
			throw new IllegalArgumentException("Unexpected message type: "
					+ message.toString());
		}

	}

}
