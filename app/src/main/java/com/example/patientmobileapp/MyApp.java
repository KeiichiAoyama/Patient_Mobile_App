package com.example.patientmobileapp;

import android.app.Application;

public class MyApp extends Application {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
