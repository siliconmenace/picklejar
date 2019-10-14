package com.seven20.picklejar.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

public class RunnerArchive {

	private static final String REMOVED_DIR = "Directory removed!";
	private static final String REMOVING_DIR = "Removing obsolete target directory";
	private static final String ZIP_CREATED = "Zip created!";
	private static final String CREATING_ZIP = "Creating zip: %s";
	private static final String IMPROPER_SETUP = "Achive target path missing expected files or does not exist, skipping archiving";
	private static final String BEGIN_ARCHIVE = "Archiving %s in %s";
	private static final String ZIP_EXT = ".zip";
	/**
	 * report.js should have line matching pattern: "name": "FeatureName",
	 */
	private static String FEATURE_MATCH = "\\s*(\"name\":\\s+\".*\",)";
	private static Logger LOG = Logger.getLogger(RunnerArchive.class.getName());

	/**
	 * Archives the target cucumber runner report in the specified output folder. Will not attempt
	 * to archive if source report does not exist or does not contain a feature name.
	 * 
	 * @param target folder which contains report of runner
	 * @param output folder which will contain archived report
	 * @return false if archive was not attempted
	 */
	public static boolean archiveReport(String target, String output) {
		LOG.info(String.format(BEGIN_ARCHIVE, target, output));
		File src = new File(target);
		File dest = new File(output);
		File report = new File(target + File.separator + "report.js");
		if (!properlySetup(src, report)) {
			LOG.info(IMPROPER_SETUP);
			return false;
		}
		dest.mkdirs();
		String zipname = getzipname(dest, report);
		LOG.info(String.format(CREATING_ZIP, zipname));
		zipTarget(src, zipname);
		LOG.info(ZIP_CREATED);
		LOG.info(REMOVING_DIR);
		deleteTarget(src);
		LOG.info(REMOVED_DIR);
		return true;
	}

	private static boolean properlySetup(File src, File report) {
		return src.exists() && report.exists() && report.length() > 0
				&& !readName(report).isEmpty();
	}

	private static String readName(File report) {
		String lineWithName = getLine(report);
		return parse(lineWithName);
	}

	private static String getLine(File report) {
		String lineWithName = "";
		String line;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(report));
			for (int i = 0; (line = br.readLine()) != null && i < 10; i++) {
				if (line.matches(FEATURE_MATCH)) {
					lineWithName = line;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineWithName;
	}

	private static String parse(String lineWithName) {
		String featurename = lineWithName;
		featurename = removeDecoration(featurename);
		featurename = removeSpecialCharacters(featurename);
		return featurename.trim();
	}

	// Always comes in as "name": "myfeature",
	private static String removeDecoration(String featurename) {
		featurename = featurename.replaceAll("\"name\": \"", "");
		return featurename.replaceAll("\",", "_");
	}

	private static String removeSpecialCharacters(String featurename) {
		featurename = featurename.replaceAll("[\\\\/:*?<>|\",]", "");
		return featurename.replaceAll(" ", "");
	}

	private static String getzipname(File dest, File report) {
		StringBuilder zipname = new StringBuilder();
		zipname.append(dest.getAbsolutePath());
		zipname.append(File.separator);
		zipname.append(readName(report));
		zipname.append(time(report));
		zipname.append(ZIP_EXT);
		return zipname.toString();
	}

	private static String time(File report) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE_dd.MM.yyyy_HHmmss");
		Long timeStamp = report.lastModified();
		Date timeStampReadable = new Date(timeStamp);
		return dateFormatter.format(timeStampReadable);
	}

	private static void zipTarget(File src, String zipname) {
		ZipOutputStream zip;
		try {
			zip = new ZipOutputStream(new FileOutputStream(zipname));
			zip("", src, zip);
			zip.flush();
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ZipOutputStream zip(String zipPath, File src, ZipOutputStream zip)
			throws IOException {
		if (src.isDirectory()) {
			addFolderToZip(zipPath, src, zip);
		} else {
			addFileToZip(zipPath, src, zip);
		}
		return zip;
	}

	private static void addFolderToZip(String zipPath, File src, ZipOutputStream zip)
			throws IOException {
		for (File file : src.listFiles()) {
			zip(zipPath + src.getName() + File.separator, file, zip);
		}
	}

	private static void addFileToZip(String zipPath, File src, ZipOutputStream zip)
			throws IOException {
		byte[] buf = new byte[(int) src.length()];
		int len;
		FileInputStream in = null;
		try {
			in = new FileInputStream(src);
			zip.putNextEntry(new ZipEntry(zipPath + src.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	private static void deleteTarget(File src) {
		try {
			FileUtils.deleteDirectory(src);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
