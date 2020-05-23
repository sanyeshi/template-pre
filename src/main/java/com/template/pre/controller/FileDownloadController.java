package com.template.pre.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileDownloadController {

	@Value("${file.download.dir}")
	private String fileDownloadDir;
	@Value("${file.download.max.bytes}")
	private Long fileDownloadMaxBytes;

	@RequestMapping(value = { "/file/download" })
	public ResponseEntity<byte[]> download(HttpServletRequest request, @RequestParam("filename") String filename)
			throws Exception {
		String path = fileDownloadDir + filename;
		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (!file.canRead()) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if (file.length() > fileDownloadMaxBytes) {
			return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
		}
		HttpHeaders headers = new HttpHeaders();
		String downloadFilename = filename;
		headers.setContentDispositionFormData("attachment", downloadFilename);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
	}

}
