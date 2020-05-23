package com.template.pre.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.template.pre.response.Response;

@RestController
public class HomeController {

	@RequestMapping(value = { "/", "index", "index.html", "index.htm" })
	public Response<String> index() {
		return new Response<>("Hello,World!");
	}

}
