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
        String nik_test = "3172063008850005";

        JSONArray medicalRecords = new JSONArray();

        MultiChain client = new MultiChain();
        try {
            String medicalRecordObjectName = "patient-" + nik_test + "-record";

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView username = findViewById(R.id.username);
        TextView userAge = findViewById(R.id.userAge);

        username.setText(user.nama);

        int birthYear = Integer.parseInt(user.tahun_lahir);
        @SuppressLint({"NewApi", "LocalSuppress"}) int currentYear = Year.now().getValue();

        userAge.setText(String.valueOf(currentYear - birthYear));

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

        Toolbar toolbar = findViewById(R.id.toolbar3);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResumeMedisActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}