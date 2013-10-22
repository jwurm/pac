package com.prodyna.academy.pac.conference.facade;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

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

import com.prodyna.academy.pac.base.BusinessException;
import com.prodyna.academy.pac.conference.conference.model.Conference;
import com.prodyna.academy.pac.conference.conference.service.ConferenceCRUDService;
import com.prodyna.academy.pac.conference.facade.service.ConferenceService;
import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;
import com.prodyna.academy.pac.conference.talk.model.Talk;
import com.prodyna.academy.pac.conference.talk.service.TalkCRUDService;

/**
 * The Class ConferenceServiceTest. Tests basic crud actions.
 */
@RunWith(Arquillian.class)
public class ConferenceServiceTest {

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
	private ConferenceService service;

	/** The service. */
	@Inject
	private TalkCRUDService talkservice;

	@Inject
	private RoomCRUDService roomservice;

	/**
	 * Test crud.
	 * 
	 * @throws ParseException
	 *             the parse exception
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws HeuristicRollbackException
	 * @throws HeuristicMixedException
	 * @throws RollbackException
	 * @throws IllegalStateException
	 * @throws SecurityException
	 */
	@Test
	@InSequence(1)
	public void testCRUD() throws ParseException, NotSupportedException,
			SystemException, SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException,
			HeuristicRollbackException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Conference conference1 = new Conference("JAX", "Java conference",
				sdf.parse("2013-02-01"), sdf.parse("2013-02-05"));
		conference1 = service.createConference(conference1);
		Assert.assertEquals(Integer.valueOf(1), conference1.getId());

		Conference conference2 = new Conference("GamesCon", "Games conference",
				sdf.parse("2013-07-03"), sdf.parse("2013-07-06"));
		conference2 = service.createConference(conference2);
		Assert.assertEquals(Integer.valueOf(2), conference2.getId());
		Conference conference3 = new Conference("IAA", "Autozeug",
				sdf.parse("2013-09-03"), sdf.parse("2013-09-06"));
		conference3 = service.createConference(conference3);

		Assert.assertEquals(Integer.valueOf(3), conference3.getId());
		Assert.assertEquals("Games conference", conference2.getDescription());

		conference2.setDescription("Spielekonferenz");
		service.updateConference(conference2);
		Conference conference2_1 = service.getConference(2);
		Assert.assertEquals(Integer.valueOf(2), conference2_1.getId());
		Assert.assertEquals("Spielekonferenz", conference2.getDescription());

		List<Conference> conferences = service.getAllConferences();
		Assert.assertEquals(3, conferences.size());

		service.deleteConference(conference2_1.getId());
		conferences = service.getAllConferences();
		Assert.assertEquals(2, conferences.size());
	}

	@Test
	@InSequence(2)
	public void testDateValidation() throws ParseException,
			NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException {

		Conference conference1 = service.getConference(1);
		Room room = new Room("E785", 12);
		room = roomservice.createRoom(room);

		Talk talk = new Talk("JAXB", "JAXB fuer Dummies", new Instant(
				"2013-02-05T15:00").toDate(), 60, conference1, room);
		talkservice.createTalk(talk);

		try {
			conference1.setEnd(new Instant("2013-01-05").toDate());
			service.updateConference(conference1);
			Assert.fail("Business exception due to conference date validation expected");
		} catch (Exception e) {
		}

		conference1.setStart(new Instant("2014-02-07").toDate());
		conference1.setEnd(new Instant("2014-02-09").toDate());
		try {
			service.updateConference(conference1);
			Assert.fail("Business exception due to talk date validation expected");
		} catch (Exception e) {
		}

		conference1.setStart(new Instant("2013-02-03").toDate());
		conference1.setEnd(new Instant("2013-02-08").toDate());
		service.updateConference(conference1);

	}

}
