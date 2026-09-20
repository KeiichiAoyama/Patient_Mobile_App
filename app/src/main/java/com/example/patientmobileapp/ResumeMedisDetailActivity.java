package com.example.patientmobileapp;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class ResumeMedisDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resume_medis_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarRsDetail);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        TextView tvDetailPatientName = findViewById(R.id.tvDetailPatientName);
        TextView tvDetailDatePoli = findViewById(R.id.tvDetailDatePoli);
        TextView tvDetailDoctorName = findViewById(R.id.tvDetailDoctorName);

        CardView cardDiagnosis = findViewById(R.id.cardDiagnosis);
        TextView textDetail = findViewById(R.id.textDetail);

        CardView cardObat = findViewById(R.id.cardObat);
        TextView textDetail2 = findViewById(R.id.textDetail2);

        CardView cardTindakan = findViewById(R.id.cardTindakan);
        TextView textDetail3 = findViewById(R.id.textDetail3);

        CardView cardFisik = findViewById(R.id.cardFisik);
        TextView textDetail4 = findViewById(R.id.textDetail4);

        CardView cardtindakanakhir = findViewById(R.id.cardtindakanakhir);
        TextView textDetail5 = findViewById(R.id.textDetail5);

        setupExpandableCard(cardDiagnosis, textDetail);
        setupExpandableCard(cardObat, textDetail2);
        setupExpandableCard(cardTindakan, textDetail3);
        setupExpandableCard(cardFisik, textDetail4);
        setupExpandableCard(cardtindakanakhir, textDetail5);

        MyApp app = (MyApp) getApplicationContext();
        User user = app.getUser();
        if (user != null && user.nama != null && tvDetailPatientName != null) {
            tvDetailPatientName.setText(user.nama);
        }

        String recordDataString = getIntent().getStringExtra("reminder_record");

        if (recordDataString != null) {
            try {
                JSONObject recordData = new JSONObject(recordDataString);
                Log.d("record_data", recordDataString);

                JSONObject json = null;
                if (recordData.has("data")) {
                    JSONObject data = recordData.optJSONObject("data");
                    if (data != null && data.has("json")) {
                        json = data.optJSONObject("json");
                    }
                } else if (recordData.has("json")) {
                    json = recordData.optJSONObject("json");
                } else {
                    json = recordData;
                }

                if (json != null) {
                    JSONObject medRecord = json.optJSONObject("medical_record");
                    if (medRecord == null) medRecord = json;

                    String doctorName = "";
                    if (medRecord.has("Dokter")) {
                        JSONObject dokter = medRecord.optJSONObject("Dokter");
                        if (dokter != null) doctorName = dokter.optString("nama", "");
                    }
                    if (doctorName.isEmpty()) doctorName = medRecord.optString("dokter", "");
                    if (!doctorName.isEmpty() && tvDetailDoctorName != null) {
                        tvDetailDoctorName.setText(doctorName);
                    }

                    String poliName = "";
                    if (medRecord.has("Poliklinik")) {
                        JSONObject poliklinik = medRecord.optJSONObject("Poliklinik");
                        if (poliklinik != null) poliName = poliklinik.optString("nama_poliklinik", "");
                    }
                    if (poliName.isEmpty()) poliName = medRecord.optString("poliklinik", "");
                    String createdAt = medRecord.optString("created_at", "");
                    String datePoli = (createdAt.isEmpty() ? "" : createdAt) + (!poliName.isEmpty() ? " | " + poliName : "");
                    if (!datePoli.isEmpty() && tvDetailDatePoli != null) {
                        tvDetailDatePoli.setText(datePoli);
                    }

                    String diagnosisStr = medRecord.optString("diagnosis", medRecord.optString("Diagnosis", ""));
                    if (diagnosisStr.isEmpty() && medRecord.has("keluhan")) {
                        diagnosisStr = "Keluhan: " + medRecord.optString("keluhan");
                    }
                    if (!diagnosisStr.isEmpty() && textDetail != null) {
                        textDetail.setText(diagnosisStr);
                    }

                    String obatStr = medRecord.optString("obat", medRecord.optString("Obat", medRecord.optString("resep", "")));
                    if (!obatStr.isEmpty() && textDetail2 != null) {
                        textDetail2.setText(obatStr);
                    }

                    String tindakanStr = medRecord.optString("tindakan", medRecord.optString("Tindakan", ""));
                    if (!tindakanStr.isEmpty() && textDetail3 != null) {
                        textDetail3.setText(tindakanStr);
                    }

                    String fisikStr = medRecord.optString("observasi_fisik", medRecord.optString("Fisik", ""));
                    if (!fisikStr.isEmpty() && textDetail4 != null) {
                        textDetail4.setText(fisikStr);
                    }

                    String akhirStr = medRecord.optString("tindakan_akhir", medRecord.optString("TindakanAkhir", ""));
                    if (!akhirStr.isEmpty() && textDetail5 != null) {
                        textDetail5.setText(akhirStr);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupExpandableCard(CardView card, TextView detail) {
        if (card != null && detail != null) {
            card.setOnClickListener(v -> {
                if (detail.getVisibility() == View.VISIBLE) {
                    detail.setVisibility(View.GONE);
                } else {
                    detail.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}