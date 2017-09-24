package com.aleksandrp.bitsteptest.utils;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class Validation {

    public static final String REGULAR_MAIL = "^[a-z0-9](\\.?[a-z0-9_-]){0,}@[a-z0-9-]+\\.([a-z]{1,6}\\.)?[a-z]{2,15}$";
    public static final String REGULAR_PASS = "(?=(.*[0-9]))(?=.*[a-z])(?=(.*[A-Z]))(?=(.*)).{8,30}";


    /**
     * Check the email validity
     *
     * @return true or false
     */
    public static boolean isValidEmail(String loginText) {
        if (!loginText.isEmpty()) {
            loginText = loginText.toLowerCase();
        } else return false;
        return loginText.matches(Validation.REGULAR_MAIL);
    }

    /**
     * Check the password validity
     *
     * @return true or false
     */
    public static boolean isValidPassword(String loginText) {
        if (loginText.isEmpty()) return false;
        return loginText.matches(Validation.REGULAR_PASS);
    }

}
