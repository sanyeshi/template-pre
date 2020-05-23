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
public class AccessTimeAspect extends BaseAspect {
	private static final Logger LOG=LoggerFactory.getLogger(AccessTimeAspect.class);
	
	@Around("execution(* com.template.pre.controller..*.*(..))")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		Object ret = null;
		long startMs=System.currentTimeMillis();
		try {
			ret = joinPoint.proceed();
		} catch (Throwable e) {
			throw e;
		}finally {
			long endMs=System.currentTimeMillis();
			LOG.info("access time {} ms,{}",endMs-startMs,formatMethod(joinPoint));
			LOG.debug("access time {} ms,{},{},{}",
					endMs-startMs,
					formatMethod(joinPoint),
					formatArgs(joinPoint),
					formatReturn(ret));
		}
		return ret;
	}
}
