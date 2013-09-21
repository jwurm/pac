package com.prodyna.academy.pac.base.monitoring.interceptor;



import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.management.MBeanServer;
import javax.management.ObjectName;

@Singleton
@Startup
public class MBeanStarter {
	

	@Inject
	private Logger log;


	@PostConstruct
	public void registerMBeans() {
		log.warning("Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
		try {
			ms.registerMBean(new Performance(), new ObjectName(
					Performance.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to register MBeans: " + e.getMessage());

		}
//		throw new RuntimeException("success!");

	}

	@PreDestroy
	public void unRegisterMBeans() {
		log.warning("Un-Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
		try {
			ms.unregisterMBean(new ObjectName(Performance.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to unregister MBeans: " + e.getMessage());

		}
	}

}
