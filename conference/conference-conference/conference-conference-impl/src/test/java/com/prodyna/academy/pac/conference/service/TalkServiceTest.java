package com.prodyna.academy.pac.conference.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.joda.time.Instant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.conference.model.Talk;
import com.prodyna.academy.pac.room.model.Room;
import com.prodyna.academy.pac.room.service.RoomService;
import com.prodyna.academy.pac.speaker.model.Speaker;
import com.prodyna.academy.pac.speaker.service.SpeakerService;

/**
 * The Class ConferenceServiceTest.
 */
@RunWith(Arquillian.class)
public class TalkServiceTest {

	/**
	 * Creates the test archive.
	 * 
	 * @return the archive
	 */
	@Deployment
	public static Archive<?> createTestArchive() {
		// File[] libs =
		// org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver()
		// .loadPomFromFile("pom.xml").resolve("org.apache.commons:commons-lang3")
		// .withTransitivity().asFile();

		MavenDependencyResolver resolver = DependencyResolvers.use(
				MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
		File[] resolveAsFiles = resolver.artifact("joda-time:joda-time")
				.resolveAsFiles();
		return ShrinkWrap
				.create(WebArchive.class, "conferencetest.war")
				.addPackages(true, "com.prodyna.academy.pac")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml")
				.addAsLibraries(
						resolveAsFiles)
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");
	}

	/** The service. */
	@Inject
	private ConferenceService cservice;

	/** The service. */
	@Inject
	private TalkService service;

	/** The service. */
	@Inject
	private SpeakerService speakerservice;

	/** The service. */
	@Inject
	private RoomService roomservice;

	/**
	 * Test crud.
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	@InSequence(1)
	public void testCRUD() throws ParseException {
		Conference conference1 = new Conference("JAX", "Java conference",
				new Instant("2013-02-01").toDate(), new Instant("2013-02-05").toDate());
		conference1 = cservice.createConference(conference1);
		Assert.assertEquals(1, conference1.getId());

		Room room = new Room("E785", 12);
		room = roomservice.createRoom(room);
		Assert.assertEquals(Integer.valueOf(2), room.getId());

		Talk talk = new Talk("JAXB", "JAXB fuer Dummies",
				new Instant("2013-02-05T15:00").toDate(), 60, conference1, room);
		talk = service.createTalk(talk);
		Assert.assertEquals(Integer.valueOf(3), talk.getId());

		Talk foundTalk = service.findTalk(3);
		Assert.assertNotNull(foundTalk.getRoom());

		foundTalk.getRoom().setName("E504");
		foundTalk.setDuration(60);
		service.updateTalk(talk);

		foundTalk = service.findTalk(3);
		Assert.assertNotNull(foundTalk.getRoom());
		Assert.assertEquals(Integer.valueOf(60), talk.getDuration());
		// raum soll nicht aktualisiert worden sein
		Assert.assertEquals("E785", talk.getRoom().getName());

	}

	/**
	 * Test crud.
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	@InSequence(2)
	public void testSpeakerAssignment() throws ParseException {
		Conference conference = cservice.getCompleteConference(1);
		Room room = roomservice.findRoom(2);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Speaker speaker = speakerservice.createSpeaker(new Speaker("Darko",
				"Hat Plan"));
		Speaker speaker2 = speakerservice.createSpeaker(new Speaker("Frank",
				"Hat auch Plan"));

		Talk talk = service.findTalk(3);

		Talk talk2 = service.createTalk(new Talk("OpenJPA", "Sucks", new Instant("2013-02-01T15:00").toDate(), 10, conference, room));

		service.assignSpeaker(talk, speaker);
		// should not do anything
		service.assignSpeaker(talk, speaker);

		service.assignSpeaker(talk, speaker2);

		service.assignSpeaker(talk2, speaker);

		List<Talk> talksBySpeaker = service.getTalksBySpeaker(speaker);
		Assert.assertEquals(2, talksBySpeaker.size());

		List<Talk> talksBySpeaker2 = service.getTalksBySpeaker(speaker2);
		Assert.assertEquals(1, talksBySpeaker2.size());
		service.unassignSpeaker(talk, speaker2);

		talksBySpeaker = service.getTalksBySpeaker(speaker2);
		Assert.assertEquals(0, talksBySpeaker.size());

		service.unassignSpeaker(talk, speaker);
		talksBySpeaker = service.getTalksBySpeaker(speaker);
		Assert.assertEquals(1, talksBySpeaker.size());

	}

}
