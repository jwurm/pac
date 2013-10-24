package com.prodyna.academy.pac.conference.base.monitoring.interceptor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Logs service method invocations with parameters, duration and return value
 * 
 * @author jwurm
 *
 */
@ServiceLogged
@Interceptor
public class ServiceLoggingInterceptor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	/**
	 * Logs the data involved in a method invocation
	 * 
	 * @param invocationContext
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext)
			throws Exception {

		long nanoTime = System.nanoTime();
		String serviceName = invocationContext.getMethod().getDeclaringClass()
				.getCanonicalName();
		String methodName = invocationContext.getMethod().getName();
		Object[] params = invocationContext.getParameters();
		try {
			Object response = invocationContext.proceed();
			long duration = (System.nanoTime() - nanoTime) / 1000000;

			log.info("Service invocation " + serviceName + "." + methodName
					+ " " + Arrays.asList(params) + "; duration " + duration
					+ "ms return value: " + response);
			return response;
		} catch (Exception e) {
			long duration = (System.nanoTime() - nanoTime) / 1000000;
			log.severe("Service invocation " + serviceName + "." + methodName
					+ " " + Arrays.asList(params) + "; duration: " + duration
					+ "ms, exception: " + e.getMessage());
			throw e;
		}
	}
}
