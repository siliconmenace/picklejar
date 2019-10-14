package com.seven20.picklejar.configuration;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import com.seven20.picklejar.stepdefinitions.StepDefinition;

public final class ConfigurationLoader {
	private static Logger logger = Logger.getLogger(StepDefinition.class.getName());
	private static CombinedConfiguration masterConfig = new CombinedConfiguration();
	static {
		// Load all configuration files in the com/seven20/picklejar/configuration folder
		// which have name .alias, .configuration, .properties.
		for (EXTENTIONS e : EXTENTIONS.values()) {
			Configuration config = loadConfiguration(ConfigurationLoader.class, e);
			masterConfig.append(config);
		}
	}

	public enum EXTENTIONS {
		CONFIGURATION(".configuration"), ALIAS(".alias"), PROPERTIES(".properties");

		String ext;

		private EXTENTIONS(String _ext) {
			ext = _ext;
		}
	}

	private static Configuration loadConfiguration(Class<?> _class, EXTENTIONS _ext) {

		String path = _class.getPackage().getName().replace(".", "/").concat("/")
				.concat("ata".concat(_ext.ext));
		return load(_class, path);
	}

	private static Configuration load(Class<?> _class, String path) {
		try {
			URL resource = _class.getClassLoader().getResource(path);
			Files.notExists(Paths.get(resource.toURI()),
					new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
			return new Configurations().properties(resource);

		} catch (Exception e) {
			logger.warning("Unable to find file, " + path
					+ ". Please be sure the file exists and is spelled correctly, e.g. matches the name of the StepDefinition.");
		}
		return new PropertiesConfiguration(); // Return an empty config rather
												// throw an exception.
	}

	public static AtaConfig getConfiguration() {
		return new AtaConfig(masterConfig);
	}
}
