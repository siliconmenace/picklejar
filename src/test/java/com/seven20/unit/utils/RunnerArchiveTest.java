package com.seven20.unit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seven20.picklejar.utils.RunnerArchive;

public class RunnerArchiveTest {

	private static final String ARCHIVE_SKIPPED = "Archive was not attempted!";
	private static final String MISSING_ZIP = "Zip Location was not created!";
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private File cleanup;
	private File zipLocation;
	private static final String DATE_PATTERN = ".+\\.\\d{4}_.+"; // stuffMM.YYYY_stuff
	private static final String INCORRECT_ZIP = "Output zip: %s, does not include %s!";

	@Test
	public void archiveReportReturnsFalseIfTargetFolderDoesntExist() {
		String target = "doesntexist";
		zipLocationShouldExist(true);
		assertFalse(RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath()));
	}

	@Test
	public void archiveReportReturnsFalseIfTargetReportDoesntExist() {
		String target = createMockReport("reports", false, null);
		zipLocationShouldExist(true);
		assertFalse(RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath()));
	}

	@Test
	public void archiveReportReturnsFalseIfTargetReportDoesntContainFeatureName() {
		String target = createMockReport("reports", true, "");
		zipLocationShouldExist(true);
		assertFalse(RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath()));
	}

	@Test
	public void archiveReportCreatesOutputDirectoryIfItDoesntExist() {
		String target = createMockReport("reports", true, "\"name\": \"myfeature\",");
		zipLocationShouldExist(false);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(MISSING_ZIP, zipLocation.exists());
	}

	@Test
	public void archiveReportCreatesAZipFileInOutputDirectory() {
		String target = createMockReport("reports", true, "\"name\": \"myfeature\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(MISSING_ZIP, zipLocation.listFiles().length == 1);
	}

	@Test
	public void archiveReportZipShouldContainTargetReport() throws ZipException, IOException {
		String target = createMockReport("reports", true, "\"name\": \"myfeature\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(MISSING_ZIP, zipLocation.listFiles().length == 1);
		assertEquals("reports" + File.separator + "report.js",
				getZipContent(zipLocation.listFiles()[0]));
	}

	@Test
	public void archiveReportZipNameShouldIncludeFeatureName() {
		String target = createMockReport("reports", true, "\"name\": \"myfeature\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldIncludeDate() {
		String target = createMockReport("reports", true, "\"name\": \"myfeature\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "last modified date"),
				zipLocation.list()[0].matches(DATE_PATTERN));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeSpaces() {
		String target = createMockReport("reports", true, "\"name\": \"my feature has spaces\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeCommas() {
		String target = createMockReport("reports", true, "\"name\": \"my,feature,has,commas\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeQuotes() {
		String target = createMockReport("reports", true, "\"name\": \"myfeaturehas\"quotes\"\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludePipes() {
		String target = createMockReport("reports", true, "\"name\": \"myfeaturehas|pipes|\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeLessthan() {
		String target = createMockReport("reports", true, "\"name\": \"myfeaturehas<lessthan\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeGreaterthan() {
		String target = createMockReport("reports", true,
				"\"name\": \"myfeaturehas>greaterthan\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeQuestionMark() {
		String target = createMockReport("reports", true,
				"\"name\": \"myfeaturehasquestionmark?\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeStar() {
		String target = createMockReport("reports", true, "\"name\": \"myfeaturehasstar*\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeColon() {
		String target = createMockReport("reports", true, "\"name\": \"myfeaturehascolon:\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeBackSlash() {
		String target = createMockReport("reports", true, "\"name\": \"myfeaturehasbackslash\\\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportZipNameShouldNeverIncludeForwardSlash() {
		String target = createMockReport("reports", true,
				"\"name\": \"myfeaturehasforwardslash/\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertTrue(String.format(INCORRECT_ZIP, zipLocation.list()[0], "correct feature name"),
				zipLocation.list()[0].contains("myfeature"));
	}

	@Test
	public void archiveReportShouldRemoveTargetDirectory() {
		String target = createMockReport("reports", true, "\"name\": \"myfeature\",");
		zipLocationShouldExist(true);
		boolean attempted = RunnerArchive.archiveReport(target, zipLocation.getAbsolutePath());
		assertTrue(ARCHIVE_SKIPPED, attempted);
		assertFalse("Report file still exists",
				new File(target + File.separator + "report.js").exists());
		assertFalse("Report folder still exists", new File(target).exists());
	}

	@Before
	public void setup() {
		try {
			cleanup = tmp.newFolder("zipTearDownLocation");
		} catch (IOException e) {
			e.printStackTrace();
		}
		zipLocation = new File(
				cleanup.getAbsolutePath() + File.separator + "zipGoesHere" + File.separator);
	}

	private String createMockReport(String target, boolean reportExists, String content) {
		File reportDir = null;
		File report;
		try {
			reportDir = tmp.newFolder(target);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (reportExists) {
			report = new File(reportDir.getPath() + File.separator + "report.js");
			if (content != null && !content.isEmpty()) {
				write(report, content);
			}
		}
		return reportDir.getAbsolutePath();
	}

	private static void write(File output, String content) {
		PrintWriter printer = null;
		try {
			printer = new PrintWriter(output);
			printer.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (printer != null) {
				printer.flush();
				printer.close();
			}
		}
	}

	private boolean zipLocationShouldExist(boolean shouldExist) {
		if (shouldExist) {
			zipLocation.mkdirs();
		}
		assertEquals(shouldExist, zipLocation.exists());
		return shouldExist;
	}

	private String getZipContent(File zipFile) {
		String zipContent = "";
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			zipContent = entry.getName();
		}
		return zipContent;
	}

}
