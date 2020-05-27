package com.template.pre.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.prometheus.client.Histogram;

@Aspect
@Order(value = 0)
@Component
public class PrometheusMetricsAspect extends BaseAspect {
	static final Histogram requestLatency = Histogram.build()
			.name("http_requests_latency_seconds")
		    .help("Http request latency in seconds.")
		    .buckets(0.1, 0.25, 0.5, 1, 2.5, 5)
		    .labelNames("handler").register();
	@SuppressWarnings("rawtypes")
	private static final Class[] MAPPING_CLASSES;

	static {
		MAPPING_CLASSES = new Class[5];
		MAPPING_CLASSES[0] = GetMapping.class;
		MAPPING_CLASSES[1] = PostMapping.class;
		MAPPING_CLASSES[2] = PutMapping.class;
		MAPPING_CLASSES[3] = DeleteMapping.class;
		MAPPING_CLASSES[4] = RequestMapping.class;
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void requestMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public void getMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void postMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public void putMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public void deleteMapping() {
	}

	@Pointcut("requestMapping()||getMapping()||postMapping()||putMapping()||deleteMapping()")
	public void mapping() {
	}

	@Around("mapping()")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		Mapping mapping;
		try {
			mapping = getAnnotation(joinPoint);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(
					"Annotation could not be found for pjp \"" + joinPoint.toShortString() + "\"", e);
		} catch (NullPointerException e) {
			throw new IllegalStateException(
					"Annotation could not be found for pjp \"" + joinPoint.toShortString() + "\"", e);
		}
		Object ret = null;
		Histogram.Timer requestTimer = requestLatency.labels(mapping.paths[0]).startTimer();
		try {
			ret = joinPoint.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			requestTimer.observeDuration();
		}
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Mapping getAnnotation(ProceedingJoinPoint pjp) throws NoSuchMethodException {
		assert (pjp.getSignature() instanceof MethodSignature);
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		for (Class mappingClass : MAPPING_CLASSES) {
			Annotation annot = AnnotationUtils.findAnnotation(pjp.getTarget().getClass(), mappingClass);
			if (annot != null) {
				return getAnnotation(annot);
			}
			/* When target is an AOP interface proxy but annotation is on class method
			 (rather than Interface method).*/
			final String name = signature.getName();
			final Class[] parameterTypes = signature.getParameterTypes();
			Method method = ReflectionUtils.findMethod(pjp.getTarget().getClass(), name, parameterTypes);
			annot = AnnotationUtils.findAnnotation(method, mappingClass);
			if (annot != null) {
				return getAnnotation(annot);
			}
		}
		return null;
	}

	private Mapping getAnnotation(Annotation annotation) {
		if (annotation instanceof RequestMapping) {
			RequestMapping annot = (RequestMapping) annotation;
			return new Mapping(annot.value(), annot.path());
		} else if (annotation instanceof GetMapping) {
			GetMapping annot = (GetMapping) annotation;
			return new Mapping(annot.value(), annot.path());
		} else if (annotation instanceof PostMapping) {
			PostMapping annot = (PostMapping) annotation;
			return new Mapping(annot.value(), annot.path());
		} else if (annotation instanceof PutMapping) {
			PutMapping annot = (PutMapping) annotation;
			return new Mapping(annot.value(), annot.path());
		} else if (annotation instanceof DeleteMapping) {
			DeleteMapping annot = (DeleteMapping) annotation;
			return new Mapping(annot.value(), annot.path());
		}
		return null;
	}

	private static class Mapping {
		String[] paths;

		public Mapping(String[] values, String[] path) {
			super();
			this.paths = values.length != 0 ? values : path;
		}
	}
}
