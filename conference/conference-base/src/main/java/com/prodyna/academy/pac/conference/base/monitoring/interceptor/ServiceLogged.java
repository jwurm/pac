package com.prodyna.academy.pac.conference.base.monitoring.interceptor;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * 
 * Interface of the service logger
 * 
 * @author Jens Wurm
 * 
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ServiceLogged {
}