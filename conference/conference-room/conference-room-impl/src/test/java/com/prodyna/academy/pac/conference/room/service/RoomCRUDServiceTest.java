package com.prodyna.academy.pac.conference.room.service;

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

import com.prodyna.academy.pac.conference.room.model.Room;
import com.prodyna.academy.pac.conference.room.service.RoomCRUDService;


/**
 * The Class RoomServiceTest.
 */
@RunWith(Arquillian.class)
public class RoomCRUDServiceTest {

	/**
	 * Creates the test archive.
	 *
	 * @return the archive
	 */
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "conferenceroomtest.war")
				.addPackages(true, "com.prodyna.academy.pac")
				// .addClasses(ApartmentManagementServiceBean.class,
				// ApartmentServiceBean.class, BookingServiceBean.class,
				// ApartmentService.class, Apartment.class, Booking.class,
				// Resources.class, BusinessLoggingInterceptor.class,
				// BusinessLogged.class, BookingObserver.class)
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");
	}

	/** The service. */
	@Inject
	private RoomCRUDService service;

	/**
	 * Test crud.
	 *
	 * @throws ParseException the parse exception
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
		
		List<Room> rooms=service.getRooms();
		Assert.assertEquals(3, rooms.size());
		
		service.deleteRoom(room2_1.getId());
		
		rooms=service.getRooms();
		Assert.assertEquals(2, rooms.size());
	}

}
