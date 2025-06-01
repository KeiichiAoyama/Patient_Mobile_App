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

        TextView namaPengguna = view.findViewById(R.id.profileName);
        TextView telpPengguna = view.findViewById(R.id.profilePhone);

        namaPengguna.setText(user.nama);
        telpPengguna.setText(user.no_telp);

        return view;
    }
}