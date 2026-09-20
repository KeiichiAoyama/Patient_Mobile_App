package com.example.patientmobileapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KodeIdentitasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kode_identitas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        MyApp app = (MyApp) getApplicationContext();
        User user = app.getUser();
        if (user != null) {
            android.widget.TextView tvNama = findViewById(R.id.tvKodeIdentitasNama);
            android.widget.TextView tvNik = findViewById(R.id.tvKodeIdentitasNik);
            if (tvNama != null && user.nama != null && !user.nama.isEmpty()) {
                tvNama.setText(user.nama);
            }
            if (tvNik != null && user.nik != null && !user.nik.isEmpty()) {
                tvNik.setText(user.nik);
            }
        }
    }
}