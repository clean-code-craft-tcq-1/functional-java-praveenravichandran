package vitals;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageUtil {

	public static Locale locale = Locale.ENGLISH;
	public static final String FILENAME = "vitals/messages";

	public static void printMessage(String message, String type, String level) {
		ResourceBundle bundle = ResourceBundle.getBundle(FILENAME, locale);
		System.out.println(bundle.getString(buildKeyName(message, type, level)));
	}

	private static String buildKeyName(String message, String type, String level) {
		return level + MessageConstants.UNDERSCORE + type + MessageConstants.UNDERSCORE + message;
	}
}
