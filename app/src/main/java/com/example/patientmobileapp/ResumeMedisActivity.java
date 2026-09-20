package com.example.patientmobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Year;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResumeMedisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_medis);

        Log.d("TESTING", "ResumeMedisActivity Started Successfully");

        MyApp app = (MyApp) getApplicationContext();
        User user = app.getUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        String nik = (user.nik != null && !user.nik.trim().isEmpty()) ? user.nik.trim() : "3172063008850005";

        JSONArray medicalRecords = new JSONArray();

        TextView username = findViewById(R.id.username);
        TextView userAge = findViewById(R.id.userAge);

        username.setText(user.nama);

        if (user.tahun_lahir != null && !user.tahun_lahir.trim().isEmpty()) {
            try {
                int birthYear = Integer.parseInt(user.tahun_lahir.trim());
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                userAge.setText((currentYear - birthYear) + " Tahun");
            } catch (Exception e) {
                userAge.setText("-");
            }
        } else {
            userAge.setText("-");
        }

        Button buttonRawatJalan = findViewById(R.id.button_rawat_jalan);
        Button buttonTesLab = findViewById(R.id.button_tes_lab);

        buttonRawatJalan.setOnClickListener(v -> {
            RawatJalanFragment fragment = new RawatJalanFragment();
            Bundle args = new Bundle();
            args.putString("medicalRecords", medicalRecords.toString());
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.resumeMedisFrameLayout, fragment)
                    .commit();
        });

        buttonTesLab.setOnClickListener(v -> {
            TesLabFragment fragment = new TesLabFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.resumeMedisFrameLayout, fragment)
                    .commit();
        });

        // Load initial tab
        if (savedInstanceState == null) {
            buttonRawatJalan.performClick();
        }

        MultiChain client = new MultiChain();
        try {
            String medicalRecordObjectName = "patient-" + nik + "-record";

            JSONArray params = new JSONArray();
            params.put(medicalRecordObjectName);

            client.callMultiChain("liststreamitems", params, new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("TESTING", "Received Response");

                    if (response.isSuccessful()) {
                        Log.d("TESTING", "Response Successful");

                        String responseBody = response.body().string();

                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            JSONArray medicalRecordsRaw = jsonResponse.getJSONArray("result");

                            for (int i = 0; i < medicalRecordsRaw.length(); i++) {
                                JSONObject mrr = medicalRecordsRaw.getJSONObject(i);
                                medicalRecords.put(mrr);
                            }

                            runOnUiThread(() -> {
                                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.resumeMedisFrameLayout);
                                if (currentFragment instanceof RawatJalanFragment) {
                                    ((RawatJalanFragment) currentFragment).updateMedicalRecords(medicalRecords.toString());
                                }
                            });
                        } catch (JSONException e) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar3);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }
}