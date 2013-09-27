package com.prodyna.academy.pac.conference.service;

import java.util.logging.Logger;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.speaker.model.Speaker;

@Decorator
public abstract class TalkChangeNotificationDecorator implements TalkService {

	@Inject
	@Delegate
	private TalkService service;

	@Inject
	private InitialContext ctx;
	
	@Inject
	Logger log;

	@Inject
	private QueueConnectionFactory qcf;

	private void sendQueueMessage(String message) {
		try {
			Queue queue = (Queue) ctx.lookup("queue/test");
			Connection conn = qcf.createConnection();
			Session session = conn.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			
			
//			// And we create a MessageConsumer which will consume orders from
//			MessageConsumer consumer = session.createConsumer(queue);
			// We make sure we start the connection, or delivery won't occur on
			// it:

			conn.start();
			// We create a simple TextMessage and send it:

			TextMessage tm = session.createTextMessage(message);
			producer.send(tm);
			producer.close();
			session.close();
			conn.stop();
			conn.close();
			log.info("Queue message written: "+tm.getText());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void assignSpeaker(Talk talk, Speaker speaker) {
		service.assignSpeaker(talk, speaker);
		sendQueueMessage("Speaker " + speaker.getName() + " was added to talk "
				+ talk.getName());

	}

	@Override
	public void unassignSpeaker(Talk talk, Speaker speaker) {
		service.unassignSpeaker(talk, speaker);
		sendQueueMessage("Speaker " + speaker.getName()
				+ " was removed from talk " + talk.getName());

	}

	@Override
	public Talk updateTalk(Talk talk) {
		Talk updateTalk = service.updateTalk(talk);
		sendQueueMessage("Talk was updated: " + talk.toString());
		return updateTalk;
	}

	@Override
	public void deleteTalk(Talk talk) {
		service.deleteTalk(talk);
		sendQueueMessage("Talk was deleted: " + talk.toString());

	}

}
