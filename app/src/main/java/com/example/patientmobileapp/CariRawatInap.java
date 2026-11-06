package com.example.patientmobileapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class CariRawatInap extends AppCompatActivity {

    private Spinner spinnerTipeKamar;
    private Spinner spinnerProvinsi;
    private Spinner spinnerKota;
    private Button btnCariKamar;

    private final String[] bedTypesNonCovid = new String[]{
            "Semua Kelas Tempat Tidur",
            "VIP / VVIP",
            "Kelas 1",
            "Kelas 2",
            "Kelas 3",
            "ICU / ICCU / HCU",
            "Isolasi Non-COVID",
            "NICU / PICU"
    };

    private final String[] bedTypesCovid = new String[]{
            "Semua Tipe COVID-19",
            "Isolasi Tekanan Negatif",
            "Isolasi Tanpa Tekanan Negatif",
            "ICU Tekanan Negatif (Ventilator)",
            "ICU Tekanan Negatif (Non-Ventilator)",
            "ICU Tanpa Tekanan Negatif",
            "IGD Khusus COVID-19"
    };

    private final String[] provinces = new String[]{
            "DKI Jakarta",
            "Jawa Barat",
            "Banten",
            "Jawa Tengah",
            "DI Yogyakarta",
            "Jawa Timur",
            "Bali",
            "Sumatera Utara"
    };

    private final String[] cities = new String[]{
            "Semua Kabupaten / Kota",
            "Kota Jakarta Pusat",
            "Kota Jakarta Selatan",
            "Kota Jakarta Barat",
            "Kota Jakarta Timur",
            "Kota Jakarta Utara",
            "Kota Bandung",
            "Kota Bekasi",
            "Kota Tangerang",
            "Kota Surabaya"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cari_rawat_inap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.cariobat);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        spinnerTipeKamar = findViewById(R.id.spinnerTipeKamar);
        spinnerProvinsi = findViewById(R.id.spinnerProvinsi);
        spinnerKota = findViewById(R.id.spinnerKota);
        btnCariKamar = findViewById(R.id.btnCariKamar);

        setupSpinners();
        setupBedTypeToggleListener();

        if (btnCariKamar != null) {
            btnCariKamar.setOnClickListener(v -> {
                String selectedTipe = spinnerTipeKamar != null && spinnerTipeKamar.getSelectedItem() != null
                        ? spinnerTipeKamar.getSelectedItem().toString() : "Tempat Tidur";
                String selectedKota = spinnerKota != null && spinnerKota.getSelectedItem() != null
                        ? spinnerKota.getSelectedItem().toString() : "area pilihan";
                Toast.makeText(this, "Mencari " + selectedTipe + " di " + selectedKota + "...", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupSpinners() {
        // Bed Type (default: Non-Covid)
        updateBedTypeAdapter(false);

        // Province
        if (spinnerProvinsi != null) {
            ArrayAdapter<String> provAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    provinces
            );
            provAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProvinsi.setAdapter(provAdapter);
        }

        // City
        if (spinnerKota != null) {
            ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    cities
            );
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerKota.setAdapter(cityAdapter);
        }
    }

    private void updateBedTypeAdapter(boolean isCovid) {
        if (spinnerTipeKamar == null) return;
        String[] items = isCovid ? bedTypesCovid : bedTypesNonCovid;
        ArrayAdapter<String> bedAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        bedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipeKamar.setAdapter(bedAdapter);
    }

    private void setupBedTypeToggleListener() {
        getSupportFragmentManager().executePendingTransactions();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentBtnCovidContainer);
        if (fragment instanceof FragmentbtnCovid) {
            ((FragmentbtnCovid) fragment).setOnBedTypeSelectedListener(this::updateBedTypeAdapter);
        }
    }
}
