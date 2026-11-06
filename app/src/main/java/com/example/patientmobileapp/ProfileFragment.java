package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {}

    public static ProfileFragment newInstance(String param1, String param2) {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        MyApp app = (MyApp) requireActivity().getApplicationContext();
        User user = app.getUser();
        if (user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return view;
        }

        TextView namaPengguna = view.findViewById(R.id.profileName);
        TextView telpPengguna = view.findViewById(R.id.profilePhone);
        TextView cardNama = view.findViewById(R.id.profileCardName);

        if (namaPengguna != null && user.nama != null) {
            namaPengguna.setText(user.nama);
        }
        if (telpPengguna != null && user.no_telp != null) {
            telpPengguna.setText(user.no_telp);
        }
        if (cardNama != null && user.nama != null) {
            cardNama.setText(user.nama.toUpperCase());
        }

        View btnKodeIdentitas = view.findViewById(R.id.btnKodeIdentitasProfile);
        if (btnKodeIdentitas != null) {
            btnKodeIdentitas.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), KodeIdentitasActivity.class);
                startActivity(intent);
            });
        }

        View btnKartuVaksin = view.findViewById(R.id.btnKartuVaksinProfile);
        if (btnKartuVaksin != null) {
            btnKartuVaksin.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), HasilTesCovid.class);
                startActivity(intent);
            });
        }

        View.OnClickListener logoutClickListener = v -> {
            if (getContext() == null) return;
            new AlertDialog.Builder(requireContext())
                    .setTitle("Keluar dari Akun")
                    .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                    .setPositiveButton("Ya, Keluar", (dialog, which) -> {
                        app.logout();
                        Intent intent = new Intent(requireActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        };

        View btnLogout = view.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(logoutClickListener);
        }

        View btnLogoutRow = view.findViewById(R.id.btnLogoutRow);
        if (btnLogoutRow != null) {
            btnLogoutRow.setOnClickListener(logoutClickListener);
        }

        // Entrance animation
        View profileScroll = view.findViewById(R.id.profileScroll);
        if (profileScroll != null) {
            profileScroll.setAlpha(0f);
            profileScroll.setTranslationY(25f);
            profileScroll.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(350)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        return view;
    }
}
