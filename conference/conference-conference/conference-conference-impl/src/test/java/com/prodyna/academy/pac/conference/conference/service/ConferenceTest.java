package com.prodyna.academy.pac.conference.conference.service;

import java.text.ParseException;

import junit.framework.Assert;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.junit.Test;

import com.prodyna.academy.pac.conference.conference.model.Conference;

/**
 * The Class ConferenceTest. Tests derived actions of the Conference class
 */
public class ConferenceTest {

	/**
	 * Test days.
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	@Test
	public void testDays() throws ParseException{
		Conference conference = new Conference("JAX", "java stuff", new Instant("2013-01-01").toDate(), new Instant("2013-01-08").toDate());
		
		Interval interval = conference.buildInterval();
		Assert.assertEquals(true, interval.contains(new Interval(new Instant("2013-01-08T22:00"), new Instant("2013-01-08T23:56"))));
		Assert.assertEquals(true, interval.contains(new Interval(new Instant("2013-01-08T22:00"), new Instant("2013-01-09T00:00"))));
		Assert.assertEquals(false, interval.contains(new Interval(new Instant("2013-01-08T22:00"), new Instant("2013-01-09T00:01"))));
		
		
	}

}
