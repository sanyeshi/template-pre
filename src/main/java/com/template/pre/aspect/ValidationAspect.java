package com.template.pre.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.template.pre.response.Response;

@Aspect
@Order(value = 2)
@Component
public class ValidationAspect extends BaseAspect {

	@Autowired
	private Validator validator;

	@Around("execution(* com.template.pre.controller..*.*(..))")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		Object ret = null;
		try {
			Object target = joinPoint.getThis();
			Object[] args = joinPoint.getArgs();
			Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
			ExecutableValidator executableValidator = validator.unwrap(Validator.class).forExecutables();
			Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(target, method, args);
			if (!violations.isEmpty()) {
				return processViolations(violations);
			}
			for (Object arg : args) {
				Class<?> argClass = arg.getClass();
				Annotation annot = argClass.getAnnotation(Validated.class);
				if (annot != null) {
					violations = validator.validate(arg);
					if (!violations.isEmpty()) {
						return processViolations(violations);
					}
				}
			}
			ret = joinPoint.proceed();
		} catch (Throwable e) {
			throw e;
		}
		return ret;
	}

	private Response<Void> processViolations(Set<ConstraintViolation<Object>> violations) {

		List<String> errorMsgs = new ArrayList<>(violations.size());
		for (ConstraintViolation<Object> violation : violations) {
			errorMsgs.add(violation.getMessage());
		}
		String msg = String.join(",", errorMsgs);
		return Response.fail(msg);
	}
}
