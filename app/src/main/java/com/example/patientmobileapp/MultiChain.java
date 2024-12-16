package com.example.patientmobileapp;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class MultiChain {
    private static final String RPC_URL = "http://82.112.230.186:4370/";
    private static final String RPC_USER = "multichainrpc";
    private static final String RPC_PASSWORD = "6Z27Pq5eyrBjZ2giwUJYAjY8zbquJMEoUKT4mb6759oi";
    private final OkHttpClient client;

    public MultiChain() {
        this.client = new OkHttpClient();
    }

    public void callMultiChain(String method, JSONArray params, Callback callback) throws JSONException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("jsonrpc", "2.0");
        jsonRequest.put("id", 1);
        jsonRequest.put("method", method);
        jsonRequest.put("params", params);

        RequestBody body = RequestBody.create(jsonRequest.toString(), JSON);

        Request request = new Request.Builder()
                .url(RPC_URL)
                .addHeader("Authorization", Credentials.basic(RPC_USER, RPC_PASSWORD))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
