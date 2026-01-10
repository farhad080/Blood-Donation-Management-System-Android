package com.roktim.blooddonation.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context. getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(int userId, String role, String username) {
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants. KEY_USER_ROLE, role);
        editor.putString(Constants. KEY_USERNAME, username);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(Constants.KEY_USER_ID, -1);
    }

    public String getUserRole() {
        return prefs.getString(Constants. KEY_USER_ROLE, "");
    }

    public String getUsername() {
        return prefs. getString(Constants.KEY_USERNAME, "");
    }

    public boolean isAdmin() {
        return Constants.ROLE_ADMIN.equals(getUserRole());
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}