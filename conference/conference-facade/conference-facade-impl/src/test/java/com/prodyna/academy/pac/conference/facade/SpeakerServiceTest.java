package com.prodyna.academy.pac.conference.facade;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.conference.service.ConferenceCRUDService;
import com.prodyna.academy.pac.conference.facade.service.SpeakerService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;
import com.prodyna.academy.pac.conference.speaker.model.Speaker;
import com.prodyna.academy.pac.conference.speaker.service.SpeakerCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

/**
 * @author jwurm Test of basic crud actions
 */
@RunWith(Arquillian.class)
public class SpeakerServiceTest {

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
				.create(WebArchive.class, "conferencefacadetest.war")
				.addPackages(true, "com.prodyna.academy.pac")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml")
				.addAsLibraries(resolveAsFiles)
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");
	}

	/** The SpeakerService. */
	@Inject
	private SpeakerService service;

	/** The ConferenceService. */
	@Inject
	private ConferenceCRUDService conferenceservice;

	/** The TalkCRUDService. */
	@Inject
	private TalkCRUDService talkservice;

	/** The RoomCRUDService. */
	@Inject
	private RoomCRUDService roomservice;

	/**
	 * Test crud.
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	public void testCRUD() throws ParseException {
		Speaker speaker1 = new Speaker("Darko", "Hat den Plan");
		speaker1 = service.createSpeaker(speaker1);
		Assert.assertEquals(Integer.valueOf(1), speaker1.getId());

		Speaker speaker2 = new Speaker("Ralf", "Weiss nix");
		speaker2 = service.createSpeaker(speaker2);
		Assert.assertEquals(Integer.valueOf(2), speaker2.getId());

		Speaker speaker3 = new Speaker("Stefan", "Labert gern");
		speaker3 = service.createSpeaker(speaker3);
		Assert.assertEquals(Integer.valueOf(3), speaker3.getId());

		Assert.assertEquals("Weiss nix", speaker2.getDescription());
		speaker2.setDescription("Hört sich gern reden");
		service.updateSpeaker(speaker2);

		Speaker speaker2_1 = service.getSpeaker(2);
		Assert.assertEquals(Integer.valueOf(2), speaker2_1.getId());
		Assert.assertEquals("Ralf", speaker2.getName());
		Assert.assertEquals("Hört sich gern reden", speaker2.getDescription());

		List<Speaker> speakers = service.getAllSpeakers();
		Assert.assertEquals(3, speakers.size());

		service.deleteSpeaker(speaker2_1.getId());

		speakers = service.getAllSpeakers();
		Assert.assertEquals(2, speakers.size());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Conference conference1 = new Conference("JAX", "Java conference",
				sdf.parse("2013-02-01"), sdf.parse("2013-02-05"));
		conference1 = conferenceservice.createConference(conference1);
		Room room = roomservice.createRoom(new Room("E504", 12));
		Talk talk = new Talk("JAXB", "JAXB fuer Dummies", new Instant(
				"2013-02-05T15:00").toDate(), 60, conference1, room);
		talk = talkservice.createTalk(talk);
		Speaker speaker = speakers.get(0);
		talkservice.assignSpeaker(talk, speaker);

		try {
			// deletion should not be possible due to assigned speakers
			service.deleteSpeaker(speaker.getId());
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(
					"Cannot delete the speaker due to assigned talks: JAXB",
					e.getCause().getMessage());
		}
	}

}
