package com.example.patientmobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private static final String PREF_NAME = "PatientAppSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_DATA = "userData";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUser(User user) {
        if (user == null) {
            clearSession();
            return;
        }
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_DATA, user.toJSON().toString());
        editor.apply();
    }

    public User getUser() {
        if (!isLoggedIn()) {
            return null;
        }
        String userData = pref.getString(KEY_USER_DATA, null);
        if (userData == null || userData.isEmpty()) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(userData);
            return User.fromJSON(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
