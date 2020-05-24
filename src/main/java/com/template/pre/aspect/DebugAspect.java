package com.template.pre.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(value = 0)
@Component
public class DebugAspect extends BaseAspect {
	private static final Logger LOG = LoggerFactory.getLogger(DebugAspect.class);

	@Around("execution(* com.template.pre.controller..*.*(..))")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		Object ret = null;
		try {
			ret = joinPoint.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("{},{},{}", formatMethod(joinPoint), formatArgs(joinPoint), formatReturn(ret));
			}
		}
		return ret;
	}
}
