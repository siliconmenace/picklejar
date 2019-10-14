package com.seven20.unit.runners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seven20.picklejar.utils.DynamicRunner;

public class DynamicRunnerTest {

	private static final String RUNNER_NAME = "DynamicTestRunner_%s.java";

	private static final String RERUNNER_NAME = "DynamicTestRerunner_%s.java";

	/**
	 * Creates a temporary folder in AppData and automatically tears down
	 */
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	private String outputDir;

	@Before
	public void setupForRunner() {
		outputDir = tempFolder.getRoot().getAbsolutePath().toString();
	}

	@Test
	public void shouldStripMisplacedAtSymbolForRunner() {
		String[] args = { "a@rg" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\",\"@arg\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldRemoveExtraAtsForRunner() {
		String[] args = { "a@r@g" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\",\"@arg\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldAddAtToFrontForRunner() {
		String[] args = { "arg" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\",\"@arg\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldPullTildaToFrontForRunner() {
		String[] args = { "~arg" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\",\"~@arg\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldRemoveExtraTildasForRunner() {
		String[] args = { "~a~rg" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\",\"~@arg\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldHaveDefaultTagsIfNoneGivenForRunner() {
		String[] args = {};
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\"})", fileContains("tags", output));
	}

	@Test
	public void shouldHaveDefaultTagsEvenIfTagsAreGivenForRunner() {
		String[] args = { "arg1", "arg2" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\",\"@arg1\",\"@arg2\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldAllowForMixOfAvoidAndTargetTagsForRunner() {
		String[] args = { "arg1", "~arg2", "arg3" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals("		tags = {\"~@dev\",\"~@ignore\",\"@arg1\",\"~@arg2\",\"@arg3\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldSupportAnyNumberOfTagsForRunner() {
		String[] args = { "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "arg8" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		assertEquals(
				"		tags = {\"~@dev\",\"~@ignore\"," + "\"@arg1\",\"@arg2\",\"@arg3\""
						+ ",\"@arg4\",\"@arg5\",\"@arg6\"" + ",\"@arg7\",\"@arg8\"})",
				fileContains("tags", output));
	}

	@Test
	public void shouldIgnoreTagOptionsForReRunner() {
		String[] args = { "a@rg" };
		File output = DynamicRunner.createRunnerSource(true, args, outputDir);
		assertEquals("", fileContains("tags", output));
	}

	@Test
	public void shouldCreateFileNameUsingTagsForRunner() {
		String[] args = { "arg1", "arg2", "arg3" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		String expected = "arg1arg2arg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RUNNER_NAME, expected)));
	}

	@Test
	public void shouldCreateFileNameUsingTagsForRerunner() {
		String[] args = { "arg1", "arg2", "arg3" };
		File output = DynamicRunner.createRunnerSource(true, args, outputDir);
		String expected = "arg1arg2arg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RERUNNER_NAME, expected)));
	}

	@Test
	public void shouldCreateFileNameUsingTagsIncludingTildasForRunner() {
		String[] args = { "~arg1", "~arg2", "~arg3" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		String expected = "Notarg1Notarg2Notarg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RUNNER_NAME, expected)));
	}

	@Test
	public void shouldCreateFileNameUsingTagsIncludingTildasForRerunner() {
		String[] args = { "~arg1", "~arg2", "~arg3" };
		File output = DynamicRunner.createRunnerSource(true, args, outputDir);
		String expected = "Notarg1Notarg2Notarg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RERUNNER_NAME, expected)));
	}

	@Test
	public void shouldCreateFileNameUsingTagsIncludingAtsForRunner() {
		String[] args = { "@arg1", "@arg2", "@arg3" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		String expected = "arg1arg2arg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RUNNER_NAME, expected)));
	}

	@Test
	public void shouldCreateFileNameUsingTagsIncludingAtsForRerunner() {
		String[] args = { "@arg1", "@arg2", "@arg3" };
		File output = DynamicRunner.createRunnerSource(true, args, outputDir);
		String expected = "arg1arg2arg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RERUNNER_NAME, expected)));
	}

	@Test
	public void shouldCreateFileNameUsingTagsIncludingMisplacedSymbolsForRunner() {
		String[] args = { "arg1", "a~rg2", "a@rg3" };
		File output = DynamicRunner.createRunnerSource(false, args, outputDir);
		String expected = "arg1Notarg2arg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RUNNER_NAME, expected)));
	}

	@Test
	public void shouldCreateFileNameUsingTagsIncludingMisplacedSymbolsForRerunner() {
		String[] args = { "arg1", "a~rg2", "a@rg3" };
		File output = DynamicRunner.createRunnerSource(true, args, outputDir);
		String expected = "arg1Notarg2arg3";
		String fileName = output.getName();
		assertTrue("Wrong file name: " + fileName,
				fileName.endsWith(String.format(RERUNNER_NAME, expected)));
	}

	/**
	 * Returns the entire line in the specified file which contains the key. Returns an empty string
	 * if the file does not contain the key.
	 * 
	 * @param lineKey
	 * @return
	 */
	private String fileContains(String lineKey, File target) {
		String output = "";
		Scanner scanner = null;
		try {
			scanner = new Scanner(target);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains(lineKey)) {
					output = line;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} finally {
			scanner.close();
		}
		return output;
	}
}
