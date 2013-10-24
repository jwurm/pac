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
import com.prodyna.academy.pac.conference.facade.service.RoomService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

/**
 * The Class RoomServiceTest.
 */
@RunWith(Arquillian.class)
public class RoomServiceTest {

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

	/** The service. */
	@Inject
	private RoomService service;

	/** The service. */
	@Inject
	private TalkCRUDService talkservice;

	/** The service. */
	@Inject
	private ConferenceCRUDService conferenceservice;

	/**
	 * Test crud.
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	public void testCRUD() throws ParseException {
		Room room1 = new Room("ME701", 50);
		room1 = service.createRoom(room1);
		Assert.assertEquals(Integer.valueOf(1), room1.getId());

		Room room2 = new Room("E785", 12);
		room2 = service.createRoom(room2);
		Assert.assertEquals(Integer.valueOf(2), room2.getId());

		Room room3 = new Room("E504", 5);
		room3 = service.createRoom(room3);
		Assert.assertEquals(Integer.valueOf(3), room3.getId());

		Assert.assertEquals(Integer.valueOf(12), room2.getCapacity());
		room2.setName("2616");
		room2.setCapacity(15);
		service.updateRoom(room2);

		Room room2_1 = service.getRoom(2);
		Assert.assertEquals(Integer.valueOf(2), room2_1.getId());
		Assert.assertEquals("2616", room2.getName());
		Assert.assertEquals(Integer.valueOf(15), room2.getCapacity());

		List<Room> rooms = service.getRooms();
		Assert.assertEquals(3, rooms.size());

		service.deleteRoom(room2_1.getId());

		rooms = service.getRooms();
		Assert.assertEquals(2, rooms.size());

		// test the room deletion validation logic
		Room room = rooms.get(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Conference conference1 = new Conference("JAX", "Java conference",
				sdf.parse("2013-02-01"), sdf.parse("2013-02-05"));
		conference1 = conferenceservice.createConference(conference1);
		Talk talk = new Talk("JAXB", "JAXB fuer Dummies", new Instant(
				"2013-02-05T15:00").toDate(), 60, conference1, room);
		talk = talkservice.createTalk(talk);

		try {
			service.deleteRoom(room.getId());
			Assert.fail("Room must not be deleteable");
		} catch (Exception e) {
			Assert.assertEquals(
					"com.prodyna.academy.pac.conference.base.BusinessException: Cannot delete the room due to assigned talks: JAXB",
					e.getMessage());
		}

	}

}
