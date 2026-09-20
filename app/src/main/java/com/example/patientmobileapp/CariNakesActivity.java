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

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.nakes);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

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
            obatCardAdapter adapter = new obatCardAdapter(itemList);
            recyclerView.setAdapter(adapter);

            SearchView obatSearch = findViewById(R.id.search);
            if (obatSearch != null) {
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
            }

            client.callMultiChain("liststreamitems", params, new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("TESTING", "Received Response");

                    if (response.isSuccessful()) {
                        Log.d("TESTING", "Response Successful");

                        String responseBody = response.body() != null ? response.body().string() : "";

                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            JSONArray hospitalRecords = jsonResponse.getJSONArray("result");

                            for (int i = 0; i < hospitalRecords.length(); i++) {
                                try {
                                    JSONObject hospitalRecord = hospitalRecords.getJSONObject(i);
                                    JSONObject data = hospitalRecord.optJSONObject("data");
                                    if (data == null) continue;
                                    JSONObject json = data.optJSONObject("json");
                                    if (json == null) continue;

                                    String urlRaw = json.optString("url_get_dokter", "");
                                    String hospitalName = json.optString("nama", "");
                                    if (urlRaw.isEmpty()) continue;

                                    URL url = new URL(urlRaw);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setRequestProperty("Accept", "application/json");
                                    conn.setConnectTimeout(5000);
                                    conn.setReadTimeout(5000);

                                    if (conn.getResponseCode() != 200) {
                                        Log.w("CariNakes", "HTTP error " + conn.getResponseCode() + " for " + urlRaw);
                                        continue;
                                    }

                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String inputLine;
                                    StringBuilder hospitalDoctorsRaw = new StringBuilder();

                                    while ((inputLine = in.readLine()) != null) {
                                        hospitalDoctorsRaw.append(inputLine);
                                    }
                                    in.close();

                                    JSONObject hospitalDoctorsResponse = new JSONObject(hospitalDoctorsRaw.toString());
                                    JSONArray hospitalDoctors = hospitalDoctorsResponse.optJSONArray("data");
                                    if (hospitalDoctors != null) {
                                        for (int j = 0; j < hospitalDoctors.length(); j++) {
                                            JSONObject doctor = hospitalDoctors.getJSONObject(j);
                                            doctor.put("hospital", hospitalName);
                                            doctorRecords.put(doctor);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("CariNakes", "Error fetching doctor: " + e.getMessage());
                                }
                            }

                            for (int i = 0; i < doctorRecords.length(); i++) {
                                JSONObject dokter = doctorRecords.getJSONObject(i);
                                JSONObject userObj = dokter.optJSONObject("User");
                                String name = userObj != null ? userObj.optString("nama", "Dokter") : dokter.optString("nama", "Dokter");
                                String role = userObj != null ? userObj.optString("role", "Spesialis") : dokter.optString("role", "Spesialis");
                                String hospital = dokter.optString("hospital", "");
                                itemList.add(new obat_card(name, role, hospital));
                            }

                            runOnUiThread(() -> {
                                obatCardAdapter newAdapter = new obatCardAdapter(itemList);
                                recyclerView.setAdapter(newAdapter);
                                if (obatSearch != null) {
                                    obatSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                        @Override
                                        public boolean onQueryTextSubmit(String query) {
                                            newAdapter.getFilter().filter(query);
                                            return false;
                                        }

                                        @Override
                                        public boolean onQueryTextChange(String newText) {
                                            newAdapter.getFilter().filter(newText);
                                            return false;
                                        }
                                    });
                                }
                            });
                        } catch (JSONException e) {
                            Log.e("ERROR", "JSON Parsing Error: " + e.getMessage(), e);
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