package com.example.patientmobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_regis2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_regis2 extends Fragment {
    EditText inputRtRw, inputKelurahan, inputCamat, inputAgama, inputPekerjaan, inputNoTelp, inputEmail, inputPassword;

    public static fragment_regis2 newInstance(String param1, String param2) {
        fragment_regis2 fragment = new fragment_regis2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regis2, container, false);

        inputRtRw = view.findViewById(R.id.inputrtrw);
        inputKelurahan = view.findViewById(R.id.inputdesakel);
        inputCamat = view.findViewById(R.id.inputkecamatan);
        inputAgama = view.findViewById(R.id.inputagama);
        inputPekerjaan = view.findViewById(R.id.inputpekerjaan);
        inputNoTelp = view.findViewById(R.id.inputnotelp);
        inputEmail = view.findViewById(R.id.inputemail);
        inputPassword = view.findViewById(R.id.inputpassword);

        return view;
    }

    public Map<String, Object> collectInputData(Map<String, Object> newUser) {
        String rtrw = inputRtRw.getText().toString();
        String[] rtrwSplit = rtrw.split("/");
        newUser.put("rt", rtrwSplit[0]);
        newUser.put("rw", rtrwSplit[1]);

        newUser.put("kelurahan", inputKelurahan.getText().toString());
        newUser.put("kecamatan", inputCamat.getText().toString());
        newUser.put("agama", inputAgama.getText().toString());
        newUser.put("pekerjaan", inputPekerjaan.getText().toString());
        newUser.put("no_telp", inputNoTelp.getText().toString());
        newUser.put("email", inputEmail.getText().toString());
        newUser.put("password", inputPassword.getText().toString());

        return newUser;
    }
}