package com.seven20.picklejar.configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationDecoder;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.apache.commons.configuration2.interpol.Lookup;
import org.apache.commons.configuration2.sync.LockMode;
import org.apache.commons.configuration2.sync.Synchronizer;

/**
 * Decorates Configuration
 */
public class Config implements Configuration {

	private static Logger LOG = Logger.getLogger(Config.class.getName());
	private Configuration configuration;

	public Config(Configuration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Constructs and returns a url relative to a root domain name. If the alias value starts with
	 *  "http", will use the alias value instead; this allows navigation to urls outside the root
	 *  domain specified in the .property file.
	 * 
	 * @param key for the relative path which follows the domain name, or a full url which does not
	 * exist on the root domain.
	 * @return null if both domain and path are not configured
	 */
	public String getUrl(String key){
		//Still need to check for getString to allow for Urls other than our target instance
		String backup = getString(key);
		if(backup !=null && backup.startsWith("http")){
			return backup;
		}
		return targetUrl(key);
	}
	
	private String targetUrl(String key) {

		return defaultIfMissingKey("domain").concat(defaultIfMissingKey(key));
	}

	private String defaultIfMissingKey(String key) {
		String value = configuration.getString(key);
		if (value == null) {
			LOG.warning(String.format("Property file does not contain key: %s", key));
			value = "";
		}
		return value;
	}

	@Override
	public boolean isEmpty() {
		return configuration.isEmpty();
	}

	@Override
	public int size() {
		return configuration.size();
	}

	@Override
	public boolean containsKey(String key) {
		return configuration.containsKey(key);
	}

	@Override
	public Object getProperty(String key) {
		return configuration.getProperty(key);
	}

	@Override
	public Iterator<String> getKeys(String prefix) {
		return configuration.getKeys(prefix);
	}

	@Override
	public Iterator<String> getKeys() {
		return configuration.getKeys();
	}

	@Override
	public Properties getProperties(String key) {
		return configuration.getProperties(key);
	}

	@Override
	public boolean getBoolean(String key) {
		return configuration.getBoolean(key);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	@Override
	public byte getByte(String key) {
		return configuration.getByte(key);
	}

	@Override
	public byte getByte(String key, byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	@Override
	public double getDouble(String key) {
		return configuration.getDouble(key);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	@Override
	public Double getDouble(String key, Double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	@Override
	public float getFloat(String key) {
		return configuration.getFloat(key);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	@Override
	public Float getFloat(String key, Float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	@Override
	public int getInt(String key) {
		return configuration.getInt(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return configuration.getInt(key, defaultValue);
	}

	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		return configuration.getInteger(key, defaultValue);
	}

	@Override
	public long getLong(String key) {
		return configuration.getLong(key);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	@Override
	public short getShort(String key) {
		return configuration.getShort(key);
	}

	@Override
	public short getShort(String key, short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	@Override
	public Short getShort(String key, Short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return configuration.getBigDecimal(key);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return configuration.getBigDecimal(key, defaultValue);
	}

	@Override
	public BigInteger getBigInteger(String key) {
		return configuration.getBigInteger(key);
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return configuration.getBigInteger(key, defaultValue);
	}

	@Override
	public String getString(String key) {
		return configuration.getString(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return configuration.getString(key, defaultValue);
	}

	@Override
	public String getEncodedString(String key, ConfigurationDecoder decoder) {
		return configuration.getEncodedString(key, decoder);
	}

	@Override
	public String getEncodedString(String key) {
		return configuration.getEncodedString(key);
	}

	@Override
	public String[] getStringArray(String key) {
		return configuration.getStringArray(key);
	}

	@Override
	public List<Object> getList(String key) {
		return configuration.getList(key);
	}

	@Override
	public List<Object> getList(String key, List<?> defaultValue) {
		return configuration.getList(key, defaultValue);
	}

	@Override
	public <T> T get(Class<T> cls, String key) {
		return configuration.get(cls, key);
	}

	@Override
	public <T> T get(Class<T> cls, String key, T defaultValue) {
		return configuration.get(cls, key, defaultValue);
	}

	@Override
	public Object getArray(Class<?> cls, String key) {
		return configuration.getArray(cls, key);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object getArray(Class<?> cls, String key, Object defaultValue) {
		return configuration.getArray(cls, key, defaultValue);
	}

	@Override
	public <T> List<T> getList(Class<T> cls, String key) {
		return configuration.getList(cls, key);
	}

	@Override
	public <T> List<T> getList(Class<T> cls, String key, List<T> defaultValue) {
		return configuration.getList(cls, key, defaultValue);
	}

	@Override
	public <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target) {
		return configuration.getCollection(cls, key, target);
	}

	@Override
	public <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target,
			Collection<T> defaultValue) {
		return configuration.getCollection(cls, key, target, defaultValue);
	}

	@Override
	public ImmutableConfiguration immutableSubset(String prefix) {
		return configuration.immutableSubset(prefix);
	}

	@Override
	public Synchronizer getSynchronizer() {
		return configuration.getSynchronizer();
	}

	@Override
	public void setSynchronizer(Synchronizer sync) {
		configuration.setSynchronizer(sync);
	}

	@Override
	public void lock(LockMode mode) {
		configuration.lock(mode);
	}

	@Override
	public void unlock(LockMode mode) {
		configuration.unlock(mode);
	}

	@Override
	public Configuration subset(String prefix) {
		return configuration.subset(prefix);
	}

	@Override
	public void addProperty(String key, Object value) {
		configuration.addProperty(key, value);
	}

	@Override
	public void setProperty(String key, Object value) {
		configuration.setProperty(key, value);
	}

	@Override
	public void clearProperty(String key) {
		configuration.clearProperty(key);
	}

	@Override
	public void clear() {
		configuration.clear();
	}

	@Override
	public ConfigurationInterpolator getInterpolator() {
		return configuration.getInterpolator();
	}

	@Override
	public void setInterpolator(ConfigurationInterpolator ci) {
		configuration.setInterpolator(ci);
	}

	@Override
	public void installInterpolator(Map<String, ? extends Lookup> prefixLookups,
			Collection<? extends Lookup> defLookups) {
		configuration.installInterpolator(prefixLookups, defLookups);
	}

}
