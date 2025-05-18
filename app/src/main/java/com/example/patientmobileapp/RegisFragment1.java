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

    public Map<String, Object> collectInputData(Map<String, Object> newUser) {
        newUser.put("nik", inputNik.getText().toString());
        newUser.put("nama", inputName.getText().toString());
        newUser.put("tempat_lahir", inputTempat.getText().toString());

        String dateRaw = inputDob.getText().toString();
        String[] dateSplit = dateRaw.split("/");
        newUser.put("tanggal_lahir", dateSplit[0]);
        newUser.put("bulan_lahir", dateSplit[1]);
        newUser.put("tahun_lahir", dateSplit[2]);

        newUser.put("jenis_kelamin", inputSex.getText().toString());
        newUser.put("golongan_darah", inputGoldar.getText().toString());
        newUser.put("alamat", inputAddress.getText().toString());
        return newUser;
    }
}