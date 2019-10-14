package com.seven20.picklejar.search;

/**
 * Conditions which will end a wait.
 */
public enum Until {
	PRESENT("presenceOfAllElementsLocatedBy"), VISIBLE("visibilityOfAllElementsLocatedBy");

	public String message;

	private Until(String _message) {
		message = _message;
	}
}
