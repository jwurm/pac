/*
 * 
 */
package com.prodyna.academy.pac.conference.talk.service.decorators;

import java.util.logging.Logger;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

// TODO: Auto-generated Javadoc
/**
 * Decorator which watches for talk changes and writes a report about these into
 * a queue.
 */
@Decorator
public abstract class TalkChangeNotificationDecorator implements TalkCRUDService {

	/** Name of the queue to write to. */
	public static final String QUEUE_NAME = "queue/test";

	/** The talk service. */
	@Inject
	@Delegate
	private TalkCRUDService service;

	/** The InitialContext. */
	@Inject
	private InitialContext ctx;

	/** The logger. */
	@Inject
	private Logger log;

	/** The InitialContext. */
	@Inject
	private QueueConnectionFactory qcf;

	/**
	 * Send queue message.
	 *
	 * @param message the message
	 */
	private void sendQueueMessage(String message) {
		try {
			Queue queue = (Queue) ctx.lookup(QUEUE_NAME);
			Connection conn = qcf.createConnection();
			Session session = conn.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);

			conn.start();
			// We create a simple TextMessage and send it:

			TextMessage tm = session.createTextMessage(message);
			producer.send(tm);
			producer.close();
			session.close();
			conn.stop();
			conn.close();
			log.info("Queue message written: " + tm.getText());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.conference.talk.service.TalkService#assignSpeaker(com.prodyna.academy.pac.conference.conference.talk.model.Talk, com.prodyna.academy.pac.speaker.model.Speaker)
	 */
	@Override
	public void assignSpeaker(Talk talk, Speaker speaker) {
		service.assignSpeaker(talk, speaker);
		sendQueueMessage("Speaker " + speaker.getName() + " was added to talk "
				+ talk.getName());

	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.conference.talk.service.TalkService#unassignSpeaker(com.prodyna.academy.pac.conference.conference.talk.model.Talk, com.prodyna.academy.pac.speaker.model.Speaker)
	 */
	@Override
	public void unassignSpeaker(Talk talk, Speaker speaker) {
		service.unassignSpeaker(talk, speaker);
		sendQueueMessage("Speaker " + speaker.getName()
				+ " was removed from talk " + talk.getName());

	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.conference.talk.service.TalkService#updateTalk(com.prodyna.academy.pac.conference.conference.talk.model.Talk)
	 */
	@Override
	public Talk updateTalk(Talk talk) {
		Talk updateTalk = service.updateTalk(talk);
		sendQueueMessage("Talk was updated: " + talk.toString());
		return updateTalk;
	}

	/* (non-Javadoc)
	 * @see com.prodyna.academy.pac.conference.conference.talk.service.TalkService#deleteTalk(int)
	 */
	@Override
	public Talk deleteTalk(int id) {
		Talk talk = service.deleteTalk(id);
		sendQueueMessage("Talk was deleted: " + talk.toString());
		return talk;

	}

}
