package com.seven20.picklejar.search;

/**
 * Duration of a wait, measured in seconds.
 */
public enum Wait {
	NO_WAIT(0.0), SHORT(2.0), MEDIUM(4.0), LONG(8.0);

	public double val;

	private Wait(Double _value) {
		val = _value;
	}
}
