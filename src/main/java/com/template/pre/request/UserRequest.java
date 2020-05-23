package com.template.pre.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "{user.id.missing.msg}")
	private Long id;
	@NotNull(message = "{user.name.missing.msg}")
	@Size(min = 6, max = 20, message = "{user.name.length.msg}")
	private String name;

	public UserRequest() {

	}

	public UserRequest(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserRequest [id=" + id + ", name=" + name + "]";
	}

}
