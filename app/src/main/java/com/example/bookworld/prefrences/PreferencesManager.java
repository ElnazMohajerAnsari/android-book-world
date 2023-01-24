package com.example.bookworld.prefrences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

  private static final String PREF_FILE_NAME = "com.example.bookworld.SHARED_PREFRENCES";

  public static final String PREF_KEY_USERNAME = "username";
  public static final String PREF_KEY_PASSWORD = "password";
  public static final String PREF_KEY_FIRST_NAME = "first_name";
  public static final String PREF_KEY_LAST_NAME = "last_name";
  public static final String PREF_KEY_GENDER = "gender";
  public static final String PREF_KEY_EMAIL= "email";
  public static final String PREF_KEY_LOGIN_STATUS = "login_status";
  public static final String PREF_KEY_USER_ID = "user_id";

  private static PreferencesManager sInstance;
  private SharedPreferences mSharedPreferences;

  private PreferencesManager(Context context) {
    mSharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
  }

  public static synchronized PreferencesManager getInstance(Context context) {
    if (sInstance == null) {
      sInstance = new PreferencesManager(context);
    }
    return sInstance;
  }

  public <T> void put(String key, T value) {

    if (value instanceof String) {
      mSharedPreferences.edit().putString(key, (String) value).apply();
      return;
    }

    if (value instanceof Integer) {
      mSharedPreferences.edit().putInt(key, (Integer) value).apply();
      return;
    }

    if (value instanceof Boolean) {
      mSharedPreferences.edit().putBoolean(key, (Boolean) value).apply();
      return;
    }

    if (value instanceof Float) {
      mSharedPreferences.edit().putFloat(key, (Float) value).apply();
      return;
    }

    if (value instanceof Long) {
      mSharedPreferences.edit().putLong(key, (Long) value).apply();
      return;
    }
  }

  public <T> T get(String key, T defaultValue) {
    if (defaultValue instanceof String) {
      return (T) mSharedPreferences.getString(key, (String) defaultValue);
    }

    if (defaultValue instanceof Integer) {
      Integer result = mSharedPreferences.getInt(key, (Integer) defaultValue);
      return (T) result;
    }

    if (defaultValue instanceof Long) {
      Long result = mSharedPreferences.getLong(key, (Long) defaultValue);
      return (T) result;
    }

    if (defaultValue instanceof Boolean) {
      Boolean result = mSharedPreferences.getBoolean(key, (Boolean) defaultValue);
      return (T) result;
    }

    if (defaultValue instanceof Float) {
      Float result = mSharedPreferences.getFloat(key, (Float) defaultValue);
      return (T) result;
    }

    if (defaultValue instanceof Long) {
      Long result = mSharedPreferences.getLong(key, (Long) defaultValue);
      return (T) result;
    }
    return null;
  }
}
