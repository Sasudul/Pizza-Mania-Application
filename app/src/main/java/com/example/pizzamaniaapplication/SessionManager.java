package com.example.pizzamaniaapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "PizzaManiaSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "userId";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Save login details
    public void saveLogin(int userId, String username) {
        editor.putInt(KEY_USERID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USERID, -1);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
