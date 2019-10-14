package com.seven20.picklejar.utils;

/**
 * Convenient access to information about runtime operating system.
 */
public class OperatingSystem {

	private static String OS = System.getProperty("os.name");

	private static String os() {
		return OS.toLowerCase();
	}

	public static String getOS() {
		return OS;
	}

	public static void setOS(String os) {
		if (os == null) {
			throw new IllegalArgumentException("Operating System cannot be null!");
		}
		OS = os;
	}

	/**
	 * @return true if OS is supported
	 */
	public static boolean isSupported() {
		return isWindows() || isMac() || isUnix() || isSolaris();
	}

	/**
	 * @return true if OS is windows flavor
	 */
	public static boolean isWindows() {
		return os().startsWith("win".toLowerCase());
	}

	/**
	 * @return true if OS is mac flavor.
	 */
	public static boolean isMac() {
		return os().startsWith("mac");
	}

	/**
	 * @return true if OS is unix flavor.
	 */
	public static boolean isUnix() {
		return os().startsWith("nix") || os().startsWith("nux") || os().startsWith("aix");
	}

	/**
	 * @return true if OS is solaris flavor.
	 */
	public static boolean isSolaris() {
		return os().startsWith("sunos");
	}

}
