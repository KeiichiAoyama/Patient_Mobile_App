package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FiturFragment extends Fragment {

    public FiturFragment() {}

    public static FiturFragment newInstance(String param1, String param2) {
        FiturFragment fragment = new FiturFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitur, container, false);

        LinearLayout resumeMedis = view.findViewById(R.id.btnResumemedisFitur);
        LinearLayout kesehatanKehamilan = view.findViewById(R.id.btnKesehatanKehamilanFitur);
        LinearLayout kesehatanAnak = view.findViewById(R.id.btnKesehatanAnakFitur);
        LinearLayout diariKesehatan = view.findViewById(R.id.btnDiariKesehatanFitur);
        LinearLayout minumObat = view.findViewById(R.id.btnMinumObatFitur);
        LinearLayout cariNakes = view.findViewById(R.id.btnCariNakesFitur);
        LinearLayout cariobat = view.findViewById(R.id.btnCariObatFitur);
        LinearLayout vaksinImun = view.findViewById(R.id.btnVaksinImunFitur);
        LinearLayout hasilCovid = view.findViewById(R.id.btnCovidFitur);
        LinearLayout rawatInap = view.findViewById(R.id.btnRawatInapFitur);
        LinearLayout pelayananKesehatan = view.findViewById(R.id.btnPelayananKesehatanFitur);

        resumeMedis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResumeMedisActivity.class);
                startActivity(intent);
            }
        });

        kesehatanKehamilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KesehatanKehamilan.class);
                startActivity(intent);
            }
        });

        kesehatanAnak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PertumbuhanAnak.class);
                startActivity(intent);
            }
        });

        diariKesehatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiariKesehatan.class);
                startActivity(intent);
            }
        });

        minumObat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrackerMinumObat.class);
                startActivity(intent);
            }
        });

        cariNakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CariNakesActivity.class);
                startActivity(intent);
            }
        });

        cariobat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CariObat.class);
                startActivity(intent);
            }
        });

        vaksinImun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VaksinImunisasi.class);
                startActivity(intent);
            }
        });

        hasilCovid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HasilTesCovid.class);
                startActivity(intent);
            }
        });

        rawatInap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        pelayananKesehatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PelayananKesehatan.class);
                startActivity(intent);
            }
        });

        return view;
    }
}