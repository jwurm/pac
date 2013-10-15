package com.prodyna.academy.pac.base;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Producer for Logger etc.
 * 
 */
public class Resources {

	@Produces
	@PersistenceContext
	private EntityManager em;

	@Produces
	public static Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
				.getName());
	}

	@Produces
	public InitialContext produceIC() {
		try {
			return new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Produces
	public QueueConnectionFactory produceQCF() {
		try {
			return (QueueConnectionFactory) produceIC().lookup(
					"ConnectionFactory");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

}
