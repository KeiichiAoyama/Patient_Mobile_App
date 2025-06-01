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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CariObat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cari_obat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<obat_card> itemList = new ArrayList<>();

        MultiChain client = new MultiChain();

        try {
            JSONArray params = new JSONArray();
            params.put("obat-records");

            client.callMultiChain("liststreamitems", params, new Callback() {

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Log.d("MULTICHAIN_JSON", responseBody);

                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            JSONArray resultArray = jsonResponse.getJSONArray("result");

                            JSONObject latestItem = null;
                            long latestTime = 0;

                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject item = resultArray.getJSONObject(i);
                                long blocktime = item.optLong("blocktime", 0);

                                if (blocktime > latestTime) {
                                    latestTime = blocktime;
                                    latestItem = item;
                                }
                            }

                            if (latestItem != null) {
                                JSONObject dataObj = latestItem.getJSONObject("data");
                                JSONArray obatArray = dataObj.getJSONArray("json");

                                for (int i = 0; i < obatArray.length(); i++) {
                                    JSONObject obat = obatArray.getJSONObject(i);
                                    String name = obat.optString("name");
                                    String form = obat.optString("form");
                                    String strength = obat.optString("strength");
                                    itemList.add(new obat_card(name, form, strength));
                                }

                                runOnUiThread(() -> {
                                    obatCardAdapter adapter = new obatCardAdapter(itemList);
                                    recyclerView.setAdapter(adapter);

                                    SearchView obatSearch = findViewById(R.id.obatSearch);

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
                            }
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}