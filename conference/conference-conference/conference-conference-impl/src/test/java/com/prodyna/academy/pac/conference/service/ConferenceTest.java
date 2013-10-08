package com.prodyna.academy.pac.conference.service;

import java.text.ParseException;

import junit.framework.Assert;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.junit.Test;

import com.prodyna.academy.pac.conference.model.Conference;

public class ConferenceTest {
	@Test
	public void testDays() throws ParseException{
		Conference conference = new Conference("JAX", "java stuff", new Instant("2013-01-01").toDate(), new Instant("2013-01-08").toDate());
		
		Interval interval = conference.buildInterval();
		Assert.assertEquals(true, interval.contains(new Interval(new Instant("2013-01-08T22:00"), new Instant("2013-01-08T23:56"))));
		Assert.assertEquals(true, interval.contains(new Interval(new Instant("2013-01-08T22:00"), new Instant("2013-01-09T00:00"))));
		Assert.assertEquals(false, interval.contains(new Interval(new Instant("2013-01-08T22:00"), new Instant("2013-01-09T00:01"))));
		
		
	}

}
