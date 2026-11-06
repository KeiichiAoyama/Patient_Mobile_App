package com.example.patientmobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class FragmentbtnCovid extends Fragment {

    public interface OnBedTypeSelectedListener {
        void onBedTypeSelected(boolean isCovid);
    }

    private TextView buttonCovid;
    private TextView buttonNonCovid;
    private boolean isCovidSelected = false; // default NON COVID-19
    private OnBedTypeSelectedListener listener;

    public FragmentbtnCovid() {
        // Required empty public constructor
    }

    public static FragmentbtnCovid newInstance(String param1, String param2) {
        FragmentbtnCovid fragment = new FragmentbtnCovid();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnBedTypeSelectedListener(OnBedTypeSelectedListener listener) {
        this.listener = listener;
    }

    public boolean isCovidSelected() {
        return isCovidSelected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragmentbtn_covid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonCovid = view.findViewById(R.id.buttoncovid);
        buttonNonCovid = view.findViewById(R.id.buttonnoncovid);

        if (buttonCovid != null) {
            buttonCovid.setOnClickListener(v -> selectOption(true));
        }

        if (buttonNonCovid != null) {
            buttonNonCovid.setOnClickListener(v -> selectOption(false));
        }

        updateUI();
    }

    public void selectOption(boolean covid) {
        isCovidSelected = covid;
        updateUI();

        if (listener != null) {
            listener.onBedTypeSelected(isCovidSelected);
        }
    }

    private void updateUI() {
        if (getContext() == null || buttonCovid == null || buttonNonCovid == null) return;

        int activeBg = R.drawable.shape_segment_pill_active;
        int inactiveBg = R.drawable.shape_segment_pill_inactive;
        int activeColor = ContextCompat.getColor(getContext(), R.color.white);
        int inactiveColor = ContextCompat.getColor(getContext(), R.color.text_secondary);

        if (isCovidSelected) {
            buttonCovid.setBackgroundResource(activeBg);
            buttonCovid.setTextColor(activeColor);

            buttonNonCovid.setBackgroundResource(inactiveBg);
            buttonNonCovid.setTextColor(inactiveColor);

            animateSelect(buttonCovid);
        } else {
            buttonNonCovid.setBackgroundResource(activeBg);
            buttonNonCovid.setTextColor(activeColor);

            buttonCovid.setBackgroundResource(inactiveBg);
            buttonCovid.setTextColor(inactiveColor);

            animateSelect(buttonNonCovid);
        }
    }

    private void animateSelect(View view) {
        if (view == null) return;
        view.setScaleX(0.96f);
        view.setScaleY(0.96f);
        view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(180)
                .start();
    }
}
