package com.agri.app.utils;

import java.util.regex.Pattern;

public class IdentityValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._-]{3,20}$"
    );

    public static boolean isEmail(String input) {
        return input != null && EMAIL_PATTERN.matcher(input.trim()).matches();
    }

    public static boolean isUsername(String input) {
        return input != null && USERNAME_PATTERN.matcher(input.trim()).matches();
    }

    public static boolean isValidIdentifier(String input) {
        return isEmail(input) || isUsername(input);
    }
}
