package com.example.patientmobileapp;

public class obat_card {
    String title, subtitle1, subtitle2;

    public obat_card(String title, String subtitle1, String subtitle2) {
        this.title = title;
        this.subtitle1 = subtitle1;
        this.subtitle2 = subtitle2;
    }

    public String getName() {
        return this.title;
    }

    public String getForm() {
        return this.subtitle1;
    }

    public String getStrength() {
        return this.subtitle2;
    }
}
