package util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9]+([._%+-]?[A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";

    private static final String PHONE_REGEX =
            "^(03|05|07|08|09)\\d{8}$";
    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && Pattern.matches(PHONE_REGEX, phone);
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasLower = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;

            // tối ưu: đủ điều kiện thì thoát luôn
            if (hasUpper && hasLower) return true;
        }

        return false;
    }
    public static String formatMoney(double amount) {
        long value = Math.round(amount); // loại bỏ .0 và E7

        String num = String.valueOf(value);
        StringBuilder sb = new StringBuilder(num);

        for (int i = sb.length() - 3; i > 0; i -= 3) {
            sb.insert(i, ".");
        }

        return sb.toString();
    }

}
