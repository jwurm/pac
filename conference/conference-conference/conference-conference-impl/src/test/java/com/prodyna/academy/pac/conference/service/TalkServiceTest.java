package com.prodyna.academy.pac.conference.service;

import java.io.File;
import java.text.ParseException;
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
				.addAsLibraries(resolveAsFiles)
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource("hornetq-jms.xml", "hornetq-jms.xml");
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
		foundTalk.setDuration(60);
		service.updateTalk(talk);

		foundTalk = service.getTalk(3);
		Assert.assertNotNull(foundTalk.getRoom());
		Assert.assertEquals(Integer.valueOf(60), talk.getDuration());
		// raum soll nicht aktualisiert worden sein
		Assert.assertEquals("E785", talk.getRoom().getName());

	}

	/**
	 * Test speaker assignment and unassignment
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	@InSequence(2)
	public void testSpeakerAssignment() throws ParseException {
		Conference conference = cservice.getCompleteConference(1);
		Room room = roomservice.findRoom(2);

		Speaker speaker = speakerservice.createSpeaker(new Speaker("Darko",
				"Hat Plan"));
		Speaker speaker2 = speakerservice.createSpeaker(new Speaker("Frank",
				"Hat auch Plan"));

		Talk talk = service.getTalk(3);

		Assert.assertEquals(1, service.getTalks().size());

		Talk talk2 = service
				.createTalk(new Talk("OpenJPA", "Sucks", new Instant(
						"2013-02-01T16:00").toDate(), 10, conference, room));

		Assert.assertEquals(2, service.getTalks().size());

		service.assignSpeaker(talk, speaker);
		// should not do anything
		service.assignSpeaker(talk, speaker);

		service.assignSpeaker(talk, speaker2);

		service.assignSpeaker(talk2, speaker);

		List<Talk> talksBySpeaker = service.getTalksBySpeaker(speaker.getId());
		Assert.assertEquals(2, talksBySpeaker.size());

		List<Talk> talksBySpeaker2 = service.getTalksBySpeaker(speaker2.getId());
		Assert.assertEquals(1, talksBySpeaker2.size());
		service.unassignSpeaker(talk, speaker2);

		talksBySpeaker = service.getTalksBySpeaker(speaker2.getId());
		Assert.assertEquals(0, talksBySpeaker.size());

		service.unassignSpeaker(talk, speaker);
		talksBySpeaker = service.getTalksBySpeaker(speaker.getId());
		Assert.assertEquals(1, talksBySpeaker.size());

	}

	/**
	 * Test speaker assignment collisions
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	@InSequence(3)
	public void testSpeakerCollision() throws ParseException {
		Conference conference = cservice.getCompleteConference(1);
		Room room1 = roomservice.createRoom(new Room("testraum", 50));
		Room room2 = roomservice.createRoom(new Room("testraum2", 50));

		Speaker speaker = speakerservice.createSpeaker(new Speaker("Laberer",
				"schwätzt dumm"));

		// create two talks with overlapping datetime
		Talk talk1 = new Talk("Test1", "desc",
				new Instant("2013-02-01T15:00").toDate(), 60, conference, room1);
		talk1 = service.createTalk(talk1);

		Talk talk2 = new Talk("Test2", "desc",
				new Instant("2013-02-01T15:30").toDate(), 60, conference, room2);
		talk2 = service.createTalk(talk2);

		service.assignSpeaker(talk1, speaker);
		try {

			service.assignSpeaker(talk2, speaker);
			Assert.fail("Collision detection failed!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		talk1.setDuration(30);
		talk1 = service.updateTalk(talk1);
		// now it should be possible to assign the speaker, as the talks no
		// longer overlap

		service.assignSpeaker(talk2, speaker);

		talk2.setDatetime(new Instant("2013-02-01T15:29").toDate());
		try {

			service.updateTalk(talk2);
			// this should fail, as this makes the talk overlap with another
			// talk that it shares the speaker with.
			Assert.fail("Collision detection failed!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Test room assignment collisions
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	@InSequence(4)
	public void testRoomCollision() throws ParseException {
		Conference conference = cservice.getCompleteConference(1);
		Room room1 = roomservice.createRoom(new Room("testraum", 50));


		// create two talks with overlapping datetime
		Talk talk1 = new Talk("Test1", "desc",
				new Instant("2013-02-01T15:00").toDate(), 60, conference, room1);
		talk1 = service.createTalk(talk1);

		Talk talk2 = new Talk("Test2", "desc",
				new Instant("2013-02-01T15:30").toDate(), 60, conference, room1);

		try {
			talk2 = service.createTalk(talk2);
			// room already is occupied by that time
			Assert.fail("Collision detection failed!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		talk2.setDatetime(new Instant("2013-02-01T16:00").toDate());
		talk2 = service.createTalk(talk2);

		talk1.setDuration(61);
		try {
			talk1 = service.updateTalk(talk1);
			// this extends into talk2
			Assert.fail("Collision detection failed!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// reschedule talk2 a minute later
		talk2.setDatetime(new Instant("2013-02-01T16:01").toDate());
		service.updateTalk(talk2);
		// now this update should be possible.
		talk1 = service.updateTalk(talk1);
		// now it should be possible to assign the speaker, as the talks no
		// longer overlap

	}

}
