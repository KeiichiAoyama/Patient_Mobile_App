package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FiturFragment extends Fragment {

    public FiturFragment() {}

    public static FiturFragment newInstance(String param1, String param2) {
        return new FiturFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitur, container, false);

        View resumeMedis = view.findViewById(R.id.btnResumemedisFitur);
        View kesehatanKehamilan = view.findViewById(R.id.btnKesehatanKehamilanFitur);
        View kesehatanAnak = view.findViewById(R.id.btnKesehatanAnakFitur);
        View diariKesehatan = view.findViewById(R.id.btnDiariKesehatanFitur);
        View minumObat = view.findViewById(R.id.btnMinumObatFitur);
        View cariNakes = view.findViewById(R.id.btnCariNakesFitur);
        View cariobat = view.findViewById(R.id.btnCariObatFitur);
        View vaksinImun = view.findViewById(R.id.btnVaksinImunFitur);
        View hasilCovid = view.findViewById(R.id.btnCovidFitur);
        View rawatInap = view.findViewById(R.id.btnRawatInapFitur);
        View pelayananKesehatan = view.findViewById(R.id.btnPelayananKesehatanFitur);

        setAnimatedClickListener(resumeMedis, () -> {
            Intent intent = new Intent(getActivity(), ResumeMedisActivity.class);
            startActivity(intent);
        });

        setAnimatedClickListener(kesehatanKehamilan, () -> {
            Intent intent = new Intent(getActivity(), KesehatanKehamilan.class);
            startActivity(intent);
        });

        setAnimatedClickListener(kesehatanAnak, () -> {
            Intent intent = new Intent(getActivity(), PertumbuhanAnak.class);
            startActivity(intent);
        });

        setAnimatedClickListener(diariKesehatan, () -> {
            Intent intent = new Intent(getActivity(), DiariKesehatan.class);
            startActivity(intent);
        });

        setAnimatedClickListener(minumObat, () -> {
            Intent intent = new Intent(getActivity(), TrackerMinumObat.class);
            startActivity(intent);
        });

        setAnimatedClickListener(cariNakes, () -> {
            Intent intent = new Intent(getActivity(), CariNakesActivity.class);
            startActivity(intent);
        });

        setAnimatedClickListener(cariobat, () -> {
            Intent intent = new Intent(getActivity(), CariObat.class);
            startActivity(intent);
        });

        setAnimatedClickListener(vaksinImun, () -> {
            Intent intent = new Intent(getActivity(), VaksinImunisasi.class);
            startActivity(intent);
        });

        setAnimatedClickListener(hasilCovid, () -> {
            Intent intent = new Intent(getActivity(), HasilTesCovid.class);
            startActivity(intent);
        });

        setAnimatedClickListener(rawatInap, () -> {
            Intent intent = new Intent(getActivity(), CariRawatInap.class);
            startActivity(intent);
        });

        setAnimatedClickListener(pelayananKesehatan, () -> {
            Intent intent = new Intent(getActivity(), PelayananKesehatan.class);
            startActivity(intent);
        });

        // Entrance animation
        animateEntrance(view);

        return view;
    }

    private void animateEntrance(View root) {
        View row1 = root.findViewById(R.id.sectionRow1);
        View row2 = root.findViewById(R.id.sectionRow2);
        View row3 = root.findViewById(R.id.sectionRow3);

        if (row1 != null) {
            row1.setTranslationY(30f);
            row1.setAlpha(0f);
            row1.animate().translationY(0f).alpha(1f).setDuration(350).setInterpolator(new DecelerateInterpolator()).start();
        }

        if (row2 != null) {
            row2.setTranslationY(30f);
            row2.setAlpha(0f);
            row2.animate().translationY(0f).alpha(1f).setStartDelay(100).setDuration(350).setInterpolator(new DecelerateInterpolator()).start();
        }

        if (row3 != null) {
            row3.setTranslationY(30f);
            row3.setAlpha(0f);
            row3.animate().translationY(0f).alpha(1f).setStartDelay(200).setDuration(350).setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    private void setAnimatedClickListener(View view, Runnable action) {
        if (view == null) return;
        view.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.92f)
                    .scaleY(0.92f)
                    .setDuration(90)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(160)
                                .setInterpolator(new OvershootInterpolator(2.2f))
                                .withEndAction(() -> {
                                    if (action != null) action.run();
                                })
                                .start();
                    })
                    .start();
        });
    }
}
