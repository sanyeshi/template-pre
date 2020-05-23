package com.template.pre.aspect;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.template.pre.response.Response;

@Aspect
@Order(value = 1)
@Component
public class ValidationAspect extends BaseAspect {
	private static final Logger LOG = LoggerFactory.getLogger(ValidationAspect.class);

	@Around("execution(* com.template.pre.controller..*.*(..))")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		Object ret = null;
		try {
			Object[] args = joinPoint.getArgs();
			for (Object arg : args) {
				if (arg instanceof BindingResult) {
					BindingResult bindingResult = (BindingResult) arg;
					if (bindingResult.hasErrors()) {
						Response<Void> response = processBingingErrors(bindingResult);
						LOG.warn("request validation error [ {} ],{}", response.getMsg(), formatMethod(joinPoint));
						return response;
					}
				}
			}
			ret = joinPoint.proceed();
		} catch (Throwable e) {
			throw e;
		}
		return ret;
	}

	private Response<Void> processBingingErrors(BindingResult bindingResult) {

		List<ObjectError> errors = bindingResult.getAllErrors();
		List<String> errorMsgs = new ArrayList<>(errors.size());
		for (ObjectError objectError : errors) {
			errorMsgs.add(objectError.getDefaultMessage());
		}
		String msg = String.join(",", errorMsgs);
		return Response.fail(msg);
	}
}
