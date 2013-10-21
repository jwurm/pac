package com.prodyna.academy.pac.speaker.service;

import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.prodyna.academy.pac.speaker.model.Speaker;

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
		return ShrinkWrap
				.create(WebArchive.class, "conferencespeakertest.war")
				.addPackages(true, "com.prodyna.academy.pac")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");
	}

	/** The SpeakerService. */
	@Inject
	private SpeakerService service;

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
	}

}
