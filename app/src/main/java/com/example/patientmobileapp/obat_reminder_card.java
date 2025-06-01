package com.example.patientmobileapp;

import org.json.JSONObject;

public class obat_reminder_card {
    public String title, subtitle1, subtitle2, subtitle3;
    public boolean isChecked;
    public JSONObject record;

    public obat_reminder_card(String title, String subtitle1, String subtitle2, String subtitle3, boolean isChecked, JSONObject record) {
        this.title = title;
        this.subtitle1 = subtitle1;
        this.subtitle2 = subtitle2;
        this.subtitle3 = subtitle3;
        this.isChecked = isChecked;
        this.record = record;
    }

    public obat_reminder_card(String title, String subtitle1, String subtitle2, String subtitle3) {
        this(title, subtitle1, subtitle2, subtitle3, false, null);
    }

    public obat_reminder_card(String origin, String namaPoliklinik, String namaDokter, String createdAt, JSONObject record) {
        this(origin, namaPoliklinik, namaDokter, createdAt, false, record);
    }
}
