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

    public boolean validateInput() {
        if (inputRtRw == null) return false;
        String rtrw = inputRtRw.getText().toString().trim();
        if (rtrw.isEmpty() || !rtrw.contains("/")) {
            inputRtRw.setError("Format RT/RW: 001/002");
            inputRtRw.requestFocus();
            return false;
        }
        String[] parts = rtrw.split("/");
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            inputRtRw.setError("Format RT/RW harus lengkap: 001/002");
            inputRtRw.requestFocus();
            return false;
        }

        String email = inputEmail.getText().toString().trim();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Masukkan alamat email yang valid");
            inputEmail.requestFocus();
            return false;
        }

        String password = inputPassword.getText().toString().trim();
        if (password.length() < 6) {
            inputPassword.setError("Password minimal 6 karakter");
            inputPassword.requestFocus();
            return false;
        }

        return true;
    }

    public Map<String, Object> collectInputData(Map<String, Object> newUser) {
        String rtrw = inputRtRw.getText().toString().trim();
        String[] rtrwSplit = rtrw.split("/");
        if (rtrwSplit.length >= 2) {
            newUser.put("rt", rtrwSplit[0].trim());
            newUser.put("rw", rtrwSplit[1].trim());
        } else {
            newUser.put("rt", rtrw);
            newUser.put("rw", "");
        }

        newUser.put("kelurahan", inputKelurahan.getText().toString().trim());
        newUser.put("kecamatan", inputCamat.getText().toString().trim());
        newUser.put("agama", inputAgama.getText().toString().trim());
        newUser.put("pekerjaan", inputPekerjaan.getText().toString().trim());
        newUser.put("no_telp", inputNoTelp.getText().toString().trim());
        newUser.put("email", inputEmail.getText().toString().trim());
        newUser.put("password", inputPassword.getText().toString().trim());

        return newUser;
    }
}