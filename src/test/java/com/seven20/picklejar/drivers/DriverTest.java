package com.seven20.picklejar.drivers;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.seven20.picklejar.configuration.Config;
import com.tseven20.picklejar.utils.OperatingSystem;

public abstract class DriverTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Mock
	public Config mock;

	protected static final String CHROME = "chrome";
	protected String pageDirectory = resolvePageDirectory();
	protected Driver driver;
	protected Double waitTimeFactor = 1.0;

	protected void assignMocks(String _driverName, String... args) {
		when(mock.getDouble("waitTimeFactor")).thenReturn(1.0);
		when(mock.getString("defaultBrowser")).thenReturn(_driverName);
		when(mock.getString(_driverName)).thenReturn(_driverName);
		for (String arg : args) {
			when(mock.getString(arg)).thenReturn(arg);
		}
		driver = new Driver(mock);
	}

	protected void load(String file) {
		String page = injectPageDirectory(file);
		if (!OperatingSystem.isWindows()) {
			page = "file://" + page;
		}
		when(mock.getUrl(page)).thenReturn(page);
		when(mock.getString(page)).thenReturn(page);
		driver.get(page);
	}

	private String injectPageDirectory(String file) {
		Path pageTemplate = Paths.get(pageDirectory + File.separator + file);
		Charset charset = StandardCharsets.UTF_8;
		String outputPage = null;
		try {
			Path output = tempFolder.newFile(file).toPath();
			String content = new String(Files.readAllBytes(pageTemplate), charset);
			content = content.replace("@pageDir@", pageDirectory.toString());
			Files.write(output, content.getBytes(charset));
			outputPage = output.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputPage;
	}

	private String resolvePageDirectory() {
		String currentPath = Paths.get("").toAbsolutePath().toString();
		StringBuilder dir = new StringBuilder();
		dir.append(currentPath);
		dir.append(folder("src"));
		dir.append(folder("test"));
		dir.append(folder("resources"));
		dir.append(folder("com"));
		dir.append(folder("seven20"));
		dir.append(folder("picklejar"));
		dir.append(folder("pages"));
		return dir.toString();
	}

	private String folder(String folder) {
		StringBuilder build = new StringBuilder();
		build.append(File.separator);
		build.append(folder);
		return build.toString();
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void teardown() {
		driver.close();
	}
}
