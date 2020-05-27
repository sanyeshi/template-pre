package com.template.pre.controller;

import javax.validation.constraints.Min;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.template.api.dto.UserDTO;
import com.template.pre.request.UserRequest;
import com.template.pre.response.Response;
import com.template.pre.vo.UserVO;

@RestController
public class UserController {

	@CrossOrigin
	@GetMapping("/user")
	public Response<UserVO> user(UserRequest request) {

		// UserDTO userDTO=userService.getUser(id);
		UserDTO userDTO = new UserDTO(1L, "ssl");
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userDTO, userVO);
		return new Response<>(userVO);
	}

	@CrossOrigin
	@GetMapping("/user/{id}")
	public Response<UserVO> user(@Min(value = 1, message = "{user.id.missing.msg}") @PathVariable long id) {

		// UserDTO userDTO=userService.getUser(id);
		UserDTO userDTO = new UserDTO(id, "ssl");
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userDTO, userVO);
		return new Response<>(userVO);
	}

}
