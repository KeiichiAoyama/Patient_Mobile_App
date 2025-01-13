package com.example.patientmobileapp;

import android.os.Bundle;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ResumeMedisActivity extends AppCompatActivity {

    private ToggleButton buttonRawatJalan, buttonTesLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_medis);

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
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_resume, fragment)
                .commit();
    }
}