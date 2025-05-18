package com.example.patientmobileapp;

import org.json.JSONObject;

import java.util.Map;

public class Helper {
    public static String stringToHex(String input) {
        StringBuilder hex = new StringBuilder();
        for (char ch : input.toCharArray()) {
            hex.append(String.format("%02x", (int) ch));
        }
        return hex.toString();
    }

}
