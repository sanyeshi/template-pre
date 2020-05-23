package com.template.pre.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.template.pre.constant.Constants;
import com.template.pre.response.Response;

@RestController
public class FileUploadController {

	private static final Logger LOG = LoggerFactory.getLogger(FileUploadController.class);

	@Value("${file.upload.dir}")
	private String fileUploadDir;

	@PostMapping(value = { "/file/upload" })
	public Response<Void> download(HttpServletRequest request, @RequestParam("desc") String desc,
			@RequestParam("file") MultipartFile file) throws Exception {

		if (file.isEmpty()) {
			return Response.fail(Constants.MSG_FILE_UPLOAD_ERROR);
		}
		String filename = file.getOriginalFilename();
		String md5 = DigestUtils.md5Hex(filename);
		String saveFilename = String.format("%s-%s", md5, filename);
		File filepath = new File(fileUploadDir, saveFilename);
		if (filepath.exists()) {
			return Response.fail(Constants.MSG_FILE_UPLOAD_EXIST);
		}
		if (!filepath.getParentFile().exists()) {
			if (!filepath.getParentFile().mkdirs()) {
				return Response.fail(Constants.MSG_FILE_UPLOAD_ERROR);
			}
		}
		try {
			file.transferTo(filepath);
		} catch (Exception e) {
			LOG.error("unkown exception", e);
			return Response.fail(Constants.MSG_FILE_UPLOAD_EXIST);
		}
		return Response.success(Constants.MSG_FILE_UPLOAD_EXCESS_MAX_SIZE);
	}

}
