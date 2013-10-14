package com.prodyna.academy.pac.base.monitoring.interceptor;

import java.io.Serializable;
import java.lang.management.ManagementFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import com.prodyna.academy.pac.base.BusinessException;


@PerformanceLogged
@Interceptor
public class PerformanceLoggingInterceptor implements Serializable {
	private static final long serialVersionUID = 1L;

	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext)
			throws Exception {
		MBeanServer mbServer = ManagementFactory.getPlatformMBeanServer();
				PerformanceMXBean performance = MBeanServerInvocationHandler
				.newProxyInstance(mbServer, new ObjectName(
						Performance.OBJECT_NAME), PerformanceMXBean.class,
						false);

		long nanoTime = System.nanoTime();
		try {
			Object response = invocationContext.proceed();
			long duration = (System.nanoTime() - nanoTime) / 1000000;
			performance.report(invocationContext.getMethod()
					.getDeclaringClass().getCanonicalName(), invocationContext
					.getMethod().getName(), duration, SuccessType.SUCCESS);
			return response;
		} catch (Exception e) {
			long duration = (System.nanoTime() - nanoTime) / 1000000;
			String errorType = e instanceof BusinessException?SuccessType.BUSINESS_ERROR:SuccessType.TECHNICAL_ERROR;
			performance.report(invocationContext.getMethod()
					.getDeclaringClass().getCanonicalName(), invocationContext
					.getMethod().getName(), duration, errorType);
			throw e;
		}
	}
}
