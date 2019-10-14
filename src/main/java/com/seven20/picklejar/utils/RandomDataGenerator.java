package com.seven20.picklejar.utils;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.logging.Logger;

public class RandomDataGenerator {

	private static Logger logger = Logger.getLogger(RandomDataGenerator.class.getName());

	private static SecureRandom sr;

	private static RandomDataGenerator instance = new RandomDataGenerator();

	private static String[] names = { "Hildegard", "Harley", "Tracy", "Toal", "Rudolph", "Rundell",
			"Corrina", "Collingwood", "Kyong", "Koziel", "Allyn", "Autin", "Bill", "Bosio",
			"Shelby", "Stromain", "Allyson", "Angert", "Clementina", "Coulston", "Benita", "Berta",
			"Jenae", "Janas", "Arlean", "Arel", "Yajaira", "Ybanez", "Shanda", "Sweatt", "Lamar",
			"Loos", "Mikel", "Minott", "Marleen", "Moscato", "Katherina", "Kuester", "Jule",
			"Jeppson", "Fairy", "Fill", "Clyde", "Charleston", "Otelia", "Ormand", "Josette",
			"Joiner", "Drema", "Deason", "Jimmy", "Jacobo", "Leonida", "Lehn", "Saran", "Staples",
			"Israel", "Isaacs", "Evie", "Engen", "Maximo", "Metzer", "Pamela", "Papenfuss",
			"Angelic", "Agosta", "Kiera", "Koester", "Jestine", "Josephson", "Toshia",
			"Terwilliger", "Marylin", "Mccalley", "Clarence", "Clemmer", "Aracelis", "Allis",
			"Crystal", "Copenhaver", "Carolina", "Chunn", "Renate", "Roesner", "Dagmar",
			"Dickenson", "Salvatore", "Stelly", "Jordan", "Jobst", "Clayton", "Chaffins", "Noella",
			"Newton", "Jerlene", "Johannsen", "Lonna", "Lansford", "Eve", "Edgin", "Phyliss",
			"Chery", "Jettie", "Jana", "Tessa", "Lilian", "Dixie", "Soila", "Sarai", "Iola",
			"Dierdre", "Tilda", "Lou", "Edra", "Micki", "Lavera", "Dorthy", "Malvina", "Stephen",
			"Hong", "Maire", "Jarrod", "Nidia", "Luke", "Esteban", "Twanda", "Stacie", "Trudy",
			"Tiffani", "Deeanna", "Winston", "Kay", "Salina", "Christiana", "Dia", "Nadene",
			"Alverta", "Rosa", "Emmie", "Delena", "Rosalina", "Leah", "Chelsie", "Crysta", "Odette",
			"Delois", "Leesa", "Collin", "Rachelle", "Glinda", "Elvira", "Kristan", "Bennett",
			"Danita", "Debora", "Aaron", "Vickie", "Lurlene", "January", "Loise", "Ollie", "Bennie",
			"Melina", "Paul", "Sherlyn", "Diedre", "Angla", "Ressie", "Wilhelmina", "Sunday",
			"Leighann", "Stephenie", "Mariel", "Emelia", "Peggy", "Concetta", "Riva", "Viviana",
			"Jeff", "Christel", "Jerrod", "Howard", "Roxana", "Breanne", "Siobhan", "Leota",
			"Deangelo", "Ericka", "Adriene", "Marisela", "Letha", "Mariam", "Ocie", "Doreatha",
			"Catherina", "Marisa", "Else", "Colette", "Dante", "Annmarie" };

	private RandomDataGenerator() {
		try {
			sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
			logger.warning("Unable to get instance of SecureRandom using SHA1PRNG for SUN"
					+ "\nUsing standard SecureRandom Instance.");
		}
		byte[] seed = String.valueOf(System.currentTimeMillis()).getBytes();
		sr.nextBytes(seed);

	};

	public static RandomDataGenerator instance() {
		return instance;
	}

	public String nextName() {
		reseed();
		return names[sr.nextInt(names.length)];
	}

	private void reseed() {
		byte[] seed = String.valueOf(System.currentTimeMillis()).getBytes();
		sr.setSeed(seed);
	}

	private String phoneNumberFormat = "(%s)%s-%s";

	public String nextPhoneNumber() {
		String ac = String.valueOf(Math.abs(sr.nextLong())).substring(0, 3);
		String pf = String.valueOf(Math.abs(sr.nextLong())).substring(0, 3);
		String lf = String.valueOf(Math.abs(sr.nextLong())).substring(0, 4);
		return String.format(phoneNumberFormat, pad(ac), pad(pf), pad(lf));

	}

	private String pad(String value) {
		return value.length() < 3 ? "0".concat(value) : value;
	}

	public int nextNumber() {
		return sr.nextInt(10000);
	}

}
