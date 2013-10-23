package com.prodyna.academy.pac.base.monitoring;

import java.text.ParseException;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The Class ConferenceServiceTest.
 */
@RunWith(Arquillian.class)
public class MonitoringTest {

	/**
	 * Creates the test archive.
	 * 
	 * @return the archive
	 */
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "conferencetest.war")
				.addPackages(true, "com.prodyna.academy.pac.conference.base")
				// .addAsResource("META-INF/test-persistence.xml",
				// "META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml")
		// Deploy our test datasource
		// .addAsWebInfResource("test-ds.xml", "test-ds.xml")
		;
	}

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
	public void testCRUD() {

	}

}
