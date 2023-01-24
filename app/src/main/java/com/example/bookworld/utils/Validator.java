package com.example.bookworld.utils;

import android.text.TextUtils;

/**
 * Validator for String. this class receive string and category of string and return the string is
 * valid or not. This class can validate Email, Text, Phone and password.
 */
public class Validator {

  public static final String EMAIL = "email";
  public static final String TEXT = "text";
  public static final String PASSWORD = "password";

  public static boolean validate(String s, String category) {

    switch (category) {
      case EMAIL:
        return validateEmail(s);
      case PASSWORD:
        return validatePassword(s);
      case TEXT:
        return !TextUtils.isEmpty(s);
      default:
        return false;
    }

  }

  private static boolean validatePassword(String s) {
    String regex = "[a-zA-Z0-9]{3,25}$";
    return s.matches(regex);
  }

  private static boolean validateEmail(String s) {
    String regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
    return s.matches(regex);
  }
}
