package com.seven20.picklejar.exceptions;

public class UnsupportedBrowserException extends RuntimeException {

	private static final long serialVersionUID = 7595042602932524935L;

	public UnsupportedBrowserException(String msg) {
		super(msg + " is not supported at this time");
	}
}
