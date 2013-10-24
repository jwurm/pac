package com.prodyna.academy.pac.conference.base.monitoring.interceptor;

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


/**
 * Bootstrapping helper for MBeans
 * @author Jens Wurm
 *
 */
@Singleton
@Startup
public class MBeanStarter {

	@Inject
	private Logger log;
	
	/**
	 * Performance Mbean
	 */
	@Inject
	private PerformanceMonitor performance;
	

	/**
	 * Registers Mbeans upon startup
	 */
	@PostConstruct
	public void registerMBeans() {
		log.info("Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
		try {
			ms.registerMBean(performance, new ObjectName(
					PerformanceMonitor.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to register MBeans: " + e.getMessage());

		}

	}

	/**
	 * Unregisters MBeans
	 */
	@PreDestroy
	public void unRegisterMBeans() {
		log.info("Un-Registering MBeans");
		MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
		try {
			ms.unregisterMBean(new ObjectName(PerformanceMonitor.OBJECT_NAME));
		} catch (Exception e) {
			log.severe("Failed to unregister MBeans: " + e.getMessage());

		}
	}

}
