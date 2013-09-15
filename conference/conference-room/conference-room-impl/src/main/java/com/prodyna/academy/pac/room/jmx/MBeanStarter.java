package com.prodyna.academy.pac.room.jmx;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.prodyna.academy.pac.room.service.RoomService;

@Singleton
@Startup
public class MBeanStarter {

	@Inject
	private Logger log;
	
	

	@PostConstruct
	public void registerMBeans() {
		log.info("Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();

		try {
			ms.registerMBean(new TestMXBeanImpl(), new ObjectName(
					TestMXBeanImpl.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to register MBeans: " + e.getMessage());

		}

	}

	@PreDestroy
	public void unRegisterMBeans() {
		log.info("Un-Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
		try {
			ms.unregisterMBean(new ObjectName(TestMXBeanImpl.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to unregister MBeans: " + e.getMessage());

		}
	}

}
