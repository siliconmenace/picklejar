package com.seven20.picklejar.exceptions;

public class AmbiguousLocatorException extends RuntimeException {

	private static final long serialVersionUID = -6779818084212206876L;

	public AmbiguousLocatorException(String _locator) {
		super(_locator + " matches multiple elements");
	}
}
