package com.template.pre.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.template.common.util.JsonUtil;
import com.template.pre.constant.Constants;
import com.template.pre.response.Response;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		String msg = null;
		if (ex instanceof MaxUploadSizeExceededException) {
			msg = Constants.MSG_FILE_UPLOAD_EXCESS_MAX_SIZE;
		} else {
			msg = Constants.MSG_UNKNOWN_EXCEPTION;
		}
		Response<Void> resp = Response.fail(msg);
		String respJson = "";
		try {
			respJson = JsonUtil.toJson(resp);
			response.addHeader("Content-Type", "application/json");
			response.getOutputStream().write(respJson.getBytes());
		} catch (Exception e) {
			LOG.error("unkown exception", e);
		}
		ModelAndView modelAndView = new ModelAndView();
		return modelAndView;
	}

}
