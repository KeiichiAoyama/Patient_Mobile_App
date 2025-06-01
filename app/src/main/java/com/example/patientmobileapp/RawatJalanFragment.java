package com.example.patientmobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RawatJalanFragment extends Fragment {
    public static RawatJalanFragment newInstance(String param1, String param2) {
        RawatJalanFragment fragment = new RawatJalanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rawat_jalan, container, false);

        Bundle args = getArguments();

        List<obat_reminder_card> reminderList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rawat_view);

        if (args != null) {
            String medicalRecordsString = args.getString("medicalRecords");
            Log.d("medical", medicalRecordsString);

            try {
                JSONArray medicalRecords = new JSONArray(medicalRecordsString);

                for (int i = 0; i < medicalRecords.length(); i++) {
                    JSONObject record = medicalRecords.getJSONObject(i);

                    JSONObject data = record.getJSONObject("data");
                    JSONObject json = data.getJSONObject("json");

                    String origin = json.optString("origin", "");

                    JSONObject medicalRecord = json.optJSONObject("medical_record");
                    if (medicalRecord == null) continue;

                    String createdAt = medicalRecord.optString("created_at", "");

                    String namaPoliklinik = "";
                    if (medicalRecord.has("Poliklinik")) {
                        JSONObject poliklinik = medicalRecord.getJSONObject("Poliklinik");
                        namaPoliklinik = poliklinik.optString("nama_poliklinik", "");
                    }

                    String namaDokter = "";
                    if (medicalRecord.has("Dokter")) {
                        JSONObject dokter = medicalRecord.getJSONObject("Dokter");
                        namaDokter = dokter.optString("nama", "");
                    }

                    reminderList.add(new obat_reminder_card(
                            origin,
                            namaPoliklinik,
                            namaDokter,
                            createdAt,
                            record
                    ));
                }

                obatReminderCardAdapter adapter = new obatReminderCardAdapter(
                        requireContext(),
                        reminderList,
                        new obatReminderCardAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(obat_reminder_card item) {
                            }
                        }
                );
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return view;
    }
}