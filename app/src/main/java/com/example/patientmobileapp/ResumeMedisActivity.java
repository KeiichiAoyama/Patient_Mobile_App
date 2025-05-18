package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private ToggleButton buttonRawatJalan, buttonTesLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_medis);

        Log.d("TESTING", "ResumeMedisActivity Started Successfully");

        MyApp app = (MyApp) getApplicationContext();
        User user = app.getUser();

        MultiChain client = new MultiChain();
        try {
            String medicalRecordObjectName = "patient-" + user.nik + "-record";

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
                            JSONArray medicalRecords = jsonResponse.getJSONArray("result");
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
        int currentYear = Year.now().getValue();

        userAge.setText(String.valueOf(currentYear - birthYear));

        buttonRawatJalan = findViewById(R.id.button_rawat_jalan);
        buttonTesLab = findViewById(R.id.button_tes_lab);

        loadFragment(new RawatJalanFragment());
        buttonRawatJalan.setChecked(true);

        buttonRawatJalan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonTesLab.setChecked(false);
                loadFragment(new RawatJalanFragment());
            }
        });

        buttonTesLab.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonRawatJalan.setChecked(false);
                loadFragment(new TesLabFragment());
            }
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

    private void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frame_resume, fragment)
//                .commit();
    }
}