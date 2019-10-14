package com.seven20.picklejar.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores multiple object types for later retrieval.
 *
 */
public class DataStore {
	private static DataStore instance = new DataStore();

	private ConcurrentHashMap<String, Object> dataMap = new ConcurrentHashMap<>();

	private DataStore() {
	}

	public static DataStore instance() {
		return instance;
	}

	/**
	 * Stores a value.
	 * 
	 * @param _key used to retrieve stored value
	 * @param _value value, of any type, to be stored
	 */
	public void store(String _key, Object _value) {
		dataMap.put(_key, _value);
	}

	/**
	 * Returns a stored value.
	 * 
	 * @param _key used to retrieve stored value
	 * @return Boolean
	 */
	public Boolean getAsBoolean(String _key) {
		return (Boolean) dataMap.get(_key);
	}

	/**
	 * Returns a stored value.
	 * 
	 * @param _key used to retrieve stored value
	 * @return Double
	 */
	public Double getAsDouble(String _key) {
		return (Double) dataMap.get(_key);
	}

	/**
	 * Returns a stored value.
	 * 
	 * @param _key used to retrieve stored value
	 * @return Integer
	 */
	public Integer getAsInteger(String _key) {
		return (Integer) dataMap.get(_key);
	}

	/**
	 * Returns a stored value.
	 * 
	 * @param _key used to retrieve stored value
	 * @return Long
	 */
	public Long getAsLong(String _key) {
		return (Long) dataMap.get(_key);
	}

	/**
	 * Returns a stored value.
	 * 
	 * @param _key used to retrieve stored value
	 * @return String
	 */
	public String getAsString(String _key) {
		return (String) dataMap.get(_key);
	}

	/**
	 * Clears all stored values.
	 */
	public void clearStoredData() {
		dataMap.clear();
	}
}
