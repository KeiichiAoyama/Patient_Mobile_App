package com.example.patientmobileapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApp extends Application {
    private User user;
    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = new SessionManager(this);
        createNotificationChannel();
    }

    public User getUser() {
        if (user == null && sessionManager != null) {
            user = sessionManager.getUser();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (sessionManager != null) {
            sessionManager.saveUser(user);
        }
    }

    public boolean isLoggedIn() {
        return getUser() != null;
    }

    public void logout() {
        this.user = null;
        if (sessionManager != null) {
            sessionManager.clearSession();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "reminderChannel",
                    "Pengingat Obat",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Saluran notifikasi untuk jadwal minum obat");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}

