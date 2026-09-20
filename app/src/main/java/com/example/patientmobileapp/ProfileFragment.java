package com.example.patientmobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        MyApp app = (MyApp) requireActivity().getApplicationContext();
        User user = app.getUser();
        if (user == null) {
            android.content.Intent intent = new android.content.Intent(getActivity(), LoginActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return view;
        }

        TextView namaPengguna = view.findViewById(R.id.profileName);
        TextView telpPengguna = view.findViewById(R.id.profilePhone);
        TextView cardNama = view.findViewById(R.id.profileCardName);

        namaPengguna.setText(user.nama);
        telpPengguna.setText(user.no_telp);
        if (cardNama != null && user.nama != null) {
            cardNama.setText(user.nama.toUpperCase());
        }

        View btnKodeIdentitas = view.findViewById(R.id.btnKodeIdentitasProfile);
        if (btnKodeIdentitas != null) {
            btnKodeIdentitas.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getActivity(), KodeIdentitasActivity.class);
                startActivity(intent);
            });
        }

        View btnKartuVaksin = view.findViewById(R.id.btnKartuVaksinProfile);
        if (btnKartuVaksin != null) {
            btnKartuVaksin.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getActivity(), HasilTesCovid.class);
                startActivity(intent);
            });
        }

        View btnLogout = view.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Keluar dari Akun")
                        .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                        .setPositiveButton("Ya, Keluar", (dialog, which) -> {
                            app.logout();
                            android.content.Intent intent = new android.content.Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }

        return view;
    }
}