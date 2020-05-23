package com.template.pre.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.template.api.dto.UserDTO;
import com.template.pre.request.UserRequest;
import com.template.pre.response.Response;
import com.template.pre.vo.UserVO;

@RestController
public class UserController {

	@CrossOrigin
	@GetMapping("/user")
	public Response<UserVO> user(@Validated UserRequest request, BindingResult bindingResult) {

		// UserDTO userDTO=userService.getUser(id);

		UserDTO userDTO = new UserDTO(1L, "ssl");
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userDTO, userVO);
		return new Response<>(userVO);
	}
}
