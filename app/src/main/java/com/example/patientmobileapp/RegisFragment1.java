package com.example.patientmobileapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisFragment1 extends Fragment {
    EditText inputNik, inputName, inputTempat, inputDob, inputSex, inputGoldar, inputAddress;

    public static RegisFragment1 newInstance(String param1, String param2) {
        RegisFragment1 fragment = new RegisFragment1();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regis1, container, false);

        inputNik = view.findViewById(R.id.inputNIK);
        inputName = view.findViewById(R.id.inputnama);
        inputTempat = view.findViewById(R.id.inputtempat);
        inputDob = view.findViewById(R.id.inputtanggal);
        inputSex = view.findViewById(R.id.inputkelamin);
        inputGoldar = view.findViewById(R.id.inputdarah);
        inputAddress = view.findViewById(R.id.inputalamat);

        return view;
    }

    public boolean validateInput() {
        if (inputNik == null) return false;
        String nik = inputNik.getText().toString().trim();
        if (nik.isEmpty()) {
            inputNik.setError("NIK tidak boleh kosong");
            inputNik.requestFocus();
            return false;
        }

        String name = inputName.getText().toString().trim();
        if (name.isEmpty()) {
            inputName.setError("Nama tidak boleh kosong");
            inputName.requestFocus();
            return false;
        }

        String dob = inputDob.getText().toString().trim();
        if (dob.isEmpty() || !dob.contains("/")) {
            inputDob.setError("Format tanggal lahir: DD/MM/YYYY");
            inputDob.requestFocus();
            return false;
        }
        String[] parts = dob.split("/");
        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            inputDob.setError("Format tanggal lahir harus lengkap: DD/MM/YYYY");
            inputDob.requestFocus();
            return false;
        }

        return true;
    }

    public Map<String, Object> collectInputData(Map<String, Object> newUser) {
        newUser.put("nik", inputNik.getText().toString().trim());
        newUser.put("nama", inputName.getText().toString().trim());
        newUser.put("tempat_lahir", inputTempat.getText().toString().trim());

        String dateRaw = inputDob.getText().toString().trim();
        String[] dateSplit = dateRaw.split("/");
        if (dateSplit.length >= 3) {
            newUser.put("tanggal_lahir", dateSplit[0].trim());
            newUser.put("bulan_lahir", dateSplit[1].trim());
            newUser.put("tahun_lahir", dateSplit[2].trim());
        } else {
            newUser.put("tanggal_lahir", dateRaw);
            newUser.put("bulan_lahir", "");
            newUser.put("tahun_lahir", "");
        }

        newUser.put("jenis_kelamin", inputSex.getText().toString().trim());
        newUser.put("golongan_darah", inputGoldar.getText().toString().trim());
        newUser.put("alamat", inputAddress.getText().toString().trim());
        return newUser;
    }
}