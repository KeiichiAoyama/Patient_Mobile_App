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

    private final List<obat_reminder_card> reminderList = new ArrayList<>();
    private obatReminderCardAdapter adapter;
    private RecyclerView recyclerView;

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

        recyclerView = view.findViewById(R.id.rawat_view);
        adapter = new obatReminderCardAdapter(
                requireContext(),
                reminderList,
                new obatReminderCardAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(obat_reminder_card item) {
                        if (item != null && item.record != null && getActivity() != null) {
                            android.content.Intent intent = new android.content.Intent(getActivity(), ResumeMedisDetailActivity.class);
                            intent.putExtra("reminder_record", item.record.toString());
                            startActivity(intent);
                        }
                    }
                }
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null) {
            String medicalRecordsString = args.getString("medicalRecords");
            if (medicalRecordsString != null) {
                populateRecords(medicalRecordsString);
            }
        }

        return view;
    }

    public void updateMedicalRecords(String medicalRecordsString) {
        if (medicalRecordsString != null) {
            populateRecords(medicalRecordsString);
        }
    }

    private void populateRecords(String medicalRecordsString) {
        try {
            JSONArray medicalRecords = new JSONArray(medicalRecordsString);
            reminderList.clear();

            for (int i = 0; i < medicalRecords.length(); i++) {
                JSONObject record = medicalRecords.getJSONObject(i);

                JSONObject data = record.optJSONObject("data");
                if (data == null) continue;
                JSONObject json = data.optJSONObject("json");
                if (json == null) continue;

                String origin = json.optString("origin", "");

                JSONObject medicalRecord = json.optJSONObject("medical_record");
                if (medicalRecord == null) continue;

                String createdAt = medicalRecord.optString("created_at", "");

                String namaPoliklinik = "";
                if (medicalRecord.has("Poliklinik")) {
                    JSONObject poliklinik = medicalRecord.optJSONObject("Poliklinik");
                    if (poliklinik != null) namaPoliklinik = poliklinik.optString("nama_poliklinik", "");
                }

                String namaDokter = "";
                if (medicalRecord.has("Dokter")) {
                    JSONObject dokter = medicalRecord.optJSONObject("Dokter");
                    if (dokter != null) namaDokter = dokter.optString("nama", "");
                }

                reminderList.add(new obat_reminder_card(
                        origin,
                        namaPoliklinik,
                        namaDokter,
                        createdAt,
                        record
                ));
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}