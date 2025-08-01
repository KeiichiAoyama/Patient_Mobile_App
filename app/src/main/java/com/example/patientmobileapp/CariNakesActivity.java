package com.example.patientmobileapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CariNakesActivity extends AppCompatActivity {
    JSONArray doctorRecords = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cari_nakes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("TESTING", "CariNakesActivity Started Successfully");

        MyApp app = (MyApp) getApplicationContext();
        User user = app.getUser();

        MultiChain client = new MultiChain();
        try {
            String medicalRecordObjectName = "layanan-kesehatan-records";

            JSONArray params = new JSONArray();
            params.put(medicalRecordObjectName);

            RecyclerView recyclerView = findViewById(R.id.recyclerNakes);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            List<obat_card> itemList = new ArrayList<>();

            client.callMultiChain("liststreamitems", params, new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("TESTING", "Received Response");

                    if (response.isSuccessful()) {
                        Log.d("TESTING", "Response Successful");

                        String responseBody = response.body().string();

                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            JSONArray hospitalRecords = jsonResponse.getJSONArray("result");

                            try {
                                for (int i = 0; i < hospitalRecords.length(); i++) {
                                    JSONObject hospitalRecord = hospitalRecords.getJSONObject(i);

                                    String urlRaw = hospitalRecord.getJSONObject("data").getJSONObject("json").getString("url_get_dokter");
                                    URL url = new URL(urlRaw);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setRequestProperty("Accept", "application/json");

                                    if (conn.getResponseCode() != 200) {
                                        throw new RuntimeException("HTTP Error: " + conn.getResponseCode());
                                    }

                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String inputLine;
                                    StringBuilder hospitalDoctorsRaw = new StringBuilder();

                                    while ((inputLine = in.readLine()) != null) {
                                        hospitalDoctorsRaw.append(inputLine);
                                    }
                                    in.close();

                                    JSONObject hospitalDoctorsResponse = new JSONObject(hospitalDoctorsRaw.toString());
                                    Log.d("MULTICHAIN_JSON", hospitalDoctorsRaw.toString());
                                    JSONArray hospitalDoctors = hospitalDoctorsResponse.getJSONArray("data");

                                    for(int j = 0; j < hospitalDoctors.length(); j++) {
                                        JSONObject doctor = hospitalDoctors.getJSONObject(j);
                                        doctor.put("hospital", hospitalRecord.getJSONObject("data").getJSONObject("json").getString("nama"));
                                        doctorRecords.put(doctor);
                                    }
                                }

                                try {
                                    Log.d("MULTICHAIN_NAKES", doctorRecords.toString());

                                    for (int i = 0; i < doctorRecords.length(); i++) {
                                        JSONObject dokter = doctorRecords.getJSONObject(i);
                                        String name = dokter.getJSONObject("User").getString("nama");
                                        String role = dokter.getJSONObject("User").getString("role");
                                        String hospital = dokter.getString("hospital");
                                        itemList.add(new obat_card(name, role, hospital));
                                    }

                                    runOnUiThread(() -> {
                                        obatCardAdapter adapter = new obatCardAdapter(itemList);
                                        recyclerView.setAdapter(adapter);

                                        SearchView obatSearch = findViewById(R.id.search);

                                        obatSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String query) {
                                                adapter.getFilter().filter(query);
                                                return false;
                                            }

                                            @Override
                                            public boolean onQueryTextChange(String newText) {
                                                adapter.getFilter().filter(newText);
                                                return false;
                                            }
                                        });
                                    });
                                } catch (JSONException e) {
                                    Log.e("ERROR", "JSON Parsing Error: " + e.getMessage(), e);
                                }
                            }catch(JSONException e) {
                                e.printStackTrace();
                            }

                        }catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("TESTING", "Request failed: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
}