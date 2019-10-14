package com.seven20.picklejar.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Generates a source file for a TestRunner child and deposits in the runners package. Accepts as
 * arguments the type of runner (isRerun) to generate and the feature tags which should be targeted
 * by the generated TestRunner.
 */
public class DynamicRunner {

	private static final String COMMA = ",";
	private static final String AVOID_SYMBOL = "~";
	private static final String AT_SYMBOL = "@";
	private static final String QUOTE = "\"";
	private static final String NEW_LINE = System.getProperty("line.separator");
	// @formatter:off
	private static final String TEMPLATE = "package com.seven20.picklejar.runners;" + NEW_LINE + NEW_LINE
			+ "import org.junit.runner.RunWith;" + NEW_LINE + "import cucumber.api.CucumberOptions;"
			+ NEW_LINE + "import cucumber.api.junit.Cucumber;" + NEW_LINE + NEW_LINE
			+ "@RunWith(Cucumber.class)" + NEW_LINE + "@CucumberOptions(" + NEW_LINE + "%s"
			+ NEW_LINE // Options line
			+ "%s" + NEW_LINE // Class name line
			+ NEW_LINE + "}";
	// @formatter:on
	private static final String CLASS_LINE = "public class %s {";
	private static final String FEATURE_PATH = path("com", "seven20", "picklejar", "features");
	private static final String RERUN_PATH = path("@target", "rerun.txt");
	private static final String REPORT_PATH = path("reports", "cucumber", "rerunner");
	private static final String GLUE_PATH = path("com", "seven20", "picklejar", "stepdefinitions");
	private static final String RUNNER_OPTS = "		features = {\"classpath:" + FEATURE_PATH
			+ "\"}," + NEW_LINE + "		tags = {%s})";
	private static final String RERUNNER_OPTS = "		features = {\"" + RERUN_PATH + "\"},"
			+ NEW_LINE + "		plugin = {\"html:" + REPORT_PATH + "\"}," + NEW_LINE
			+ "		glue = {\"" + GLUE_PATH + "\"})";
	private static final String[] DEFAULT_ARGS = { "~dev", "~ignore" };
	private static final String DEFAULT_OUTPUT_DIR = path("src", "test", "java", "com", "seven20",
			"picklejar", "runners", "");

	public static void main(String[] args) {
		boolean isRerun = isRerun(args);
		String[] tags = pullOutTags(args);
		File output = createRunnerSource(isRerun, tags, DEFAULT_OUTPUT_DIR);
		System.out.println(output.getName());
	}

	private static String path(String... files) {
		StringBuilder path = new StringBuilder();
		String prefix = "";
		for (String file : files) {
			path.append(prefix);
			path.append(file);
			prefix = "/";
		}
		return path.toString();
	}

	private static boolean isRerun(String[] args) {
		if ("true".equalsIgnoreCase(args[0])) {
			return true;
		}
		return false;
	}

	private static String[] pullOutTags(String[] args) {
		String[] tags = new String[args.length - 1];
		for (int i = 0; i < args.length - 1; i++) {
			tags[i] = args[i + 1];
		}
		return tags;
	}

	/**
	 * Creates a java source file which contains the logic for a Cucumber test runner. The runner
	 * will have tag annotations for each of the arguments passed and will be deposited in the
	 * specified directory with a uniquely identifying file name.
	 * 
	 * @param isRerun true if rerunner
	 * @param tags array of tags to inject into the runner
	 * @param outputDir location where runner will be created
	 * @return output runner file generated
	 */
	public static File createRunnerSource(boolean isRerun, String[] tags, String outputDir) {
		final String className = getUniqueClassName(tags, isRerun);
		File output = getOutputFile(outputDir, className);
		writeToSource(output, determineContent(isRerun, className, tags));
		return output;
	}

	private static String getUniqueClassName(String[] dirtyTags, boolean isRerun) {
		final String uniqueID = joinArgs(dirtyTags);
		if (isRerun) {
			return String.format("DynamicTest%s_%s", "Rerunner", uniqueID);
		}
		return String.format("DynamicTest%s_%s", "Runner", uniqueID);
	}

	private static File getOutputFile(String outputPath, String uniqueID) {
		String fileName = uniqueID + ".java";
		return new File(prepend(outputPath, fileName));
	}

	private static String determineContent(boolean isRerun, String className, String[] tags) {
		final String optionsline = getOptionsLine(isRerun, tags);
		final String classline = getClassLine(isRerun, className);
		return String.format(TEMPLATE, optionsline, classline);
	}

	private static String getOptionsLine(boolean isRerun, String[] tags) {
		if (isRerun) {
			return RERUNNER_OPTS;
		}
		return String.format(RUNNER_OPTS, decorateTags(tags));
	}

	private static String getClassLine(boolean isRerun, String className) {
		if (isRerun) {
			return String.format(CLASS_LINE, className);
		}
		return String.format(CLASS_LINE, className + " extends TestRunner");
	}

	private static void writeToSource(File output, String content) {
		PrintWriter printer = null;
		try {
			printer = new PrintWriter(output);
			printer.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (printer != null) {
				printer.close();
			}
		}
	}

	private static String joinArgs(String[] args) {
		StringBuilder join = new StringBuilder();
		for (String arg : args) {
			join.append(compileFriendly(arg));
		}
		return join.toString();
	}

	private static String compileFriendly(String arg) {
		String willCompile = arg;
		willCompile = scrubbedAvoid(willCompile);
		willCompile = willCompile.replaceAll("~", "Not");
		willCompile = strip("@", willCompile);
		return willCompile;
	}

	private static String scrubbedAvoid(String target) {
		String scrubbed = target;
		if (scrubbed.contains(AVOID_SYMBOL)) {
			scrubbed = pullToFront(AVOID_SYMBOL, scrubbed);
		}
		return scrubbed;
	}

	private static String scrubbedAt(String target) {
		String scrubbed = target;
		if (scrubbed.contains(AT_SYMBOL)) {
			scrubbed = pullToFront(AT_SYMBOL, scrubbed);
		} else {
			scrubbed = prepend(AT_SYMBOL, scrubbed);
		}
		return scrubbed;
	}

	private static String pullToFront(String prefix, String target) {
		String stripped = strip(prefix, target);
		return prepend(prefix, stripped);
	}

	private static String strip(String remove, String target) {
		return target.replaceAll(remove, "");
	}

	private static String prepend(String prefix, String target) {
		StringBuilder result = new StringBuilder();
		result.append(prefix);
		result.append(target);
		return result.toString();
	}

	/**
	 * Generates format: "~@dev", "~@ignore", "@1", "@2". See below for mechanics of cucumber tags
	 */
	private static String decorateTags(String[] args) {

		String defaultTags = stringTogether(wrapEach(scrub(DEFAULT_ARGS)));
		String inputTags = stringTogether(wrapEach(scrub(args)));
		return stringTogether(new String[] { defaultTags, inputTags });
	}

	private static String[] wrapEach(String[] args) {
		String[] wrapped = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			wrapped[i] = wrapInQuotes(args[i]);
		}
		return wrapped;
	}

	private static String[] scrub(String[] tags) {
		String[] scrubbed = new String[tags.length];
		for (int i = 0; i < tags.length; i++) {
			scrubbed[i] = (scrubbedTag(tags[i]));
		}
		return scrubbed;
	}

	private static String scrubbedTag(String arg) {
		String scrubbed = arg;
		scrubbed = scrubbedAt(scrubbed);
		scrubbed = scrubbedAvoid(scrubbed);
		return scrubbed;
	}

	private static String stringTogether(String[] args) {
		StringBuilder tagList = new StringBuilder();
		String prefix = "";
		for (String arg : args) {
			if (!isNullOrEmpty(arg)) {
				tagList.append(prefix);
				prefix = COMMA;
				tagList.append(arg);
			}
		}
		return tagList.toString();
	}

	private static String wrapInQuotes(String arg) {
		StringBuilder wrapper = new StringBuilder();
		if (!isNullOrEmpty(arg)) {
			wrapper.append(QUOTE);
			wrapper.append(arg);
			wrapper.append(QUOTE);
		}
		return wrapper.toString();
	}

	private static boolean isNullOrEmpty(String arg) {
		return arg == null || "".equals(arg);
	}

	/**
	 * Mechanics of cucumber tags Given scenarios:
	 * 
	 * 1. @dev 2. @ignore 3. @dev, @ignore 4. @dev, @ignore, @1 5. @1
	 * 
	 * Runner tag Scenarios executed
	 * 
	 * "@dev" 1,3,4 "~@dev" 2,5 "~@ignore" 1,5 "~@dev, ~@ignore" 1,2,5 "~@dev", "~@ignore" none
	 * "~@dev", "~@ignore", "@1" 5
	 * 
	 */
}
