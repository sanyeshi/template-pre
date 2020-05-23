package com.template.pre.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.template.pre.constant.Constants;
import com.template.pre.response.Response;

@Aspect
@Order(value = 3)
@Component
public class ExceptionHandlerAspect extends BaseAspect {
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerAspect.class);

	@Around("execution(* com.template.pre.controller..*.*(..))")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		Object ret = null;
		try {
			ret = joinPoint.proceed();
		} catch (Throwable e) {
			ret = Response.fail(Constants.MSG_UNKNOWN_EXCEPTION);
			LOG.error("unkown exception,{}", formatMethod(joinPoint), e);
		}
		return ret;
	}
}
