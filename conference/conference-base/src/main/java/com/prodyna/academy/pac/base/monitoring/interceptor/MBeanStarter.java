package com.prodyna.academy.pac.base.monitoring.interceptor;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;


@Singleton
@Startup
public class MBeanStarter {

	@Inject
	private Logger log;
	
	@Inject
	private Performance performance;
	

	@PostConstruct
	public void registerMBeans() {
		log.info("Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
		try {
			ms.registerMBean(performance, new ObjectName(
					Performance.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to register MBeans: " + e.getMessage());

		}

	}

	@PreDestroy
	public void unRegisterMBeans() {
		log.info("Un-Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
		try {
			ms.unregisterMBean(new ObjectName(Performance.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to unregister MBeans: " + e.getMessage());

		}
	}

}
