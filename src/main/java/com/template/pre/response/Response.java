package com.template.pre.response;

import java.io.Serializable;

import com.template.pre.constant.Constants;

public class Response<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;
	private T data;

	public Response() {

	}

	public Response(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public Response(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Response(T data) {
		super();
		this.code = Constants.CODE_SUCCESS;
		this.msg = Constants.MSG_SUCCESS;
		this.data = data;
	}

	public static Response<Void> fail(String msg) {
		return new Response<Void>(Constants.CODE_FAILURE, msg);
	}

	public static <T> Response<T> fail(String msg, T data) {
		return new Response<T>(Constants.CODE_FAILURE, msg, data);
	}

	public static Response<Void> success(String msg) {
		return new Response<Void>(Constants.CODE_SUCCESS, msg);
	}

	public static <T> Response<T> success(String msg, T data) {
		return new Response<T>(Constants.CODE_SUCCESS, msg, data);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}

}
