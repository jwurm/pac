package com.prodyna.academy.pac.conference.talk.service;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.joda.time.Instant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.conference.service.ConferenceCRUDService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerCRUDService;
import com.prodyna.academy.pac.conference.talk.mdb.TalkChangeMessageHandlerImpl;
import com.prodyna.academy.pac.conference.talk.mdb.TalkChangeMessageHandlerTestImpl;
import com.prodyna.academy.pac.conference.talk.model.Talk;

/**
 * The Class ConferenceServiceTest.
 */
@RunWith(Arquillian.class)
public class TalkCRUDServiceTest {

	/**
	 * Creates the test archive.
	 * 
	 * @return the archive
	 */
	@Deployment
	public static Archive<?> createTestArchive() {

		MavenDependencyResolver resolver = DependencyResolvers.use(
				MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
		File[] resolveAsFiles = resolver.artifact("joda-time:joda-time")
				.resolveAsFiles();
		return ShrinkWrap
				.create(WebArchive.class, "conferencetest.war")
				// MDB exclude, in order to use our test implementation
				.addPackages(true,
						Filters.exclude(TalkChangeMessageHandlerImpl.class),
						"com.prodyna.academy.pac")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml")
				.addAsLibraries(resolveAsFiles)
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
		// .addAsWebInfResource("META-INF/test-jms.xml")
		;
	}

	/** The ConferenceService. */
	@Inject
	private ConferenceCRUDService cservice;

	/** The TalkService. */
	@Inject
	private TalkCRUDService service;

	/** The SpeakerService. */
	@Inject
	private SpeakerCRUDService speakerservice;

	/** The RoomService. */
	@Inject
	private RoomCRUDService roomservice;

	/**
	 * Test crud.
	 * 
	 * @throws ParseException
	 *             the parse exception
	 * @throws InterruptedException
	 */
	@Test
	@InSequence(1)
	public void testCRUD() throws ParseException, InterruptedException {
		Conference conference1 = new Conference("JAX", "Java conference",
				new Instant("2013-02-01").toDate(),
				new Instant("2013-02-05").toDate());
		conference1 = cservice.createConference(conference1);
		Assert.assertEquals(Integer.valueOf(1), conference1.getId());

		Room room = new Room("E785", 12);
		room = roomservice.createRoom(room);
		Assert.assertEquals(Integer.valueOf(2), room.getId());

		Talk talk = new Talk("JAXB", "JAXB fuer Dummies", new Instant(
				"2013-02-05T15:00").toDate(), 60, conference1, room);
		talk = service.createTalk(talk);
		Assert.assertEquals(Integer.valueOf(3), talk.getId());

		Talk foundTalk = service.getTalk(3);
		Assert.assertNotNull(foundTalk.getRoom());

		foundTalk.getRoom().setName("E504");
		foundTalk.setDuration(75);
		foundTalk.setName("Testtalk");
		foundTalk.setDescription("New description");
		foundTalk.setDatetime(new Instant("2013-02-05T14:45").toDate());
		service.updateTalk(foundTalk);

		foundTalk = service.getTalk(3);
		Assert.assertNotNull(foundTalk.getRoom());
		Assert.assertEquals(Integer.valueOf(75), foundTalk.getDuration());
		// raum soll nicht aktualisiert worden sein
		Assert.assertEquals("E785", foundTalk.getRoom().getName());

		// this part only works if there is no existing deployment of the
		// application on the server, as that one would provide a MDB instance
		// that listens to the same queue and interferes with this test.
		List<String> messages = TalkChangeMessageHandlerTestImpl.getMessages();
		Assert.assertEquals(2, messages.size());
		Assert.assertTrue(messages
				.remove("Talk was created: Talk [name=JAXB, description=JAXB fuer Dummies, datetime=Tue Feb 05 16:00:00 CET 2013, duration=60, room=Room [id=2, name=E785, capacity=12]]"));
		;
		Assert.assertTrue(messages
				.remove("Talk was updated: name was changed from \"JAXB\" to \"Testtalk\", description was changed from \"JAXB fuer Dummies\" to \"New description\", datetime was changed from 2013-02-05 16:00 to 2013-02-05 15:45, duration was changed from 60 to 75"));
		TalkChangeMessageHandlerTestImpl.getMessages().clear();

	}

	/**
	 * Test speaker assignment and unassignment
	 * 
	 * @throws ParseException
	 *             the parse exception
	 * @throws InterruptedException
	 */
	@Test
	@InSequence(2)
	public void testSpeakerAssignment() throws ParseException {
		Conference conference = cservice.getConference(1);
		Room room = roomservice.getRoom(2);

		Speaker speaker = speakerservice.createSpeaker(new Speaker("Darko",
				"Hat Plan"));
		Speaker speaker2 = speakerservice.createSpeaker(new Speaker("Frank",
				"Hat auch Plan"));

		Talk talk = service.getTalk(3);

		Assert.assertEquals(1, service.getAllTalks().size());

		Talk talk2 = service
				.createTalk(new Talk("OpenJPA", "Sucks", new Instant(
						"2013-02-01T16:00").toDate(), 10, conference, room));
		Assert.assertEquals(2, service.getAllTalks().size());

		service.assignSpeaker(talk, speaker);
		// should not do anything
		service.assignSpeaker(talk, speaker);

		service.assignSpeaker(talk, speaker2);
		service.assignSpeaker(talk2, speaker);
		List<Talk> talksBySpeaker = service.getTalksBySpeaker(speaker.getId());
		Assert.assertEquals(2, talksBySpeaker.size());

		List<Talk> talksBySpeaker2 = service
				.getTalksBySpeaker(speaker2.getId());
		Assert.assertEquals(1, talksBySpeaker2.size());
		service.unassignSpeaker(talk, speaker2);
		talksBySpeaker = service.getTalksBySpeaker(speaker2.getId());
		Assert.assertEquals(0, talksBySpeaker.size());

		service.unassignSpeaker(talk, speaker);
		talksBySpeaker = service.getTalksBySpeaker(speaker.getId());
		Assert.assertEquals(1, talksBySpeaker.size());

		// this part only works if there is no existing deployment of the
		// application on the server, as that one would provide a MDB instance
		// that listens to the same queue and interferes with this test.
		List<String> messages = TalkChangeMessageHandlerTestImpl.getMessages();
		Assert.assertEquals(7, messages.size());
		// this pattern avoids race conditions about the order with the queue
		Assert.assertTrue(messages
				.remove("Talk was created: Talk [name=OpenJPA, description=Sucks, datetime=Fri Feb 01 17:00:00 CET 2013, duration=10, room=Room [id=2, name=E785, capacity=12]]"));
		Assert.assertTrue(messages
				.remove("Speaker Darko was added to talk Testtalk"));
		Assert.assertTrue(messages
				.remove("Speaker Darko was added to talk Testtalk"));
		Assert.assertTrue(messages
				.remove("Speaker Frank was added to talk Testtalk"));
		Assert.assertTrue(messages
				.remove("Speaker Darko was added to talk OpenJPA"));
		Assert.assertTrue(messages
				.remove("Speaker Frank was removed from talk Testtalk"));
		Assert.assertTrue(messages
				.remove("Speaker Darko was removed from talk Testtalk"));

	}

}
