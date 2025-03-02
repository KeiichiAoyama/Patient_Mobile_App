package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MyApp app = (MyApp) requireActivity().getApplicationContext();
        User user = app.getUser();
        Log.d("TESTING", user.toString());

        TextView namaPengguna = view.findViewById(R.id.namaPengguna);
        TextView tanggal = view.findViewById(R.id.tanggal);

        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        String formattedDate = sdf.format(today);
        tanggal.setText(formattedDate);

        namaPengguna.setText(user.nama);

        LinearLayout resume_medis = view.findViewById(R.id.resume_medis);
        LinearLayout kesehatan_kehamilan = view.findViewById(R.id.kesehatan_kehamilan);
        LinearLayout pertumbuhan_anak = view.findViewById(R.id.pertumbuhan_anak);
        LinearLayout rawat_inap = view.findViewById(R.id.rawat_inap);
        LinearLayout tracker_obat = view.findViewById(R.id.tracker_obat);
        LinearLayout cari_nakes = view.findViewById(R.id.cari_nakes);
        LinearLayout cari_obat = view.findViewById(R.id.cari_obat);
        LinearLayout lainnya = view.findViewById(R.id.lainnya);

        resume_medis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_SHORT).show();
                Log.d("TESTING", "Button clicked!");
                Intent intent = new Intent(getActivity(), ResumeMedisActivity.class);
                startActivity(intent);
            }
        });

        rawat_inap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CariRawatInap.class);
                startActivity(intent);
            }
        });

        tracker_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrackerMinumObat.class);
                startActivity(intent);
            }
        });

        cari_nakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CariNakesActivity.class);
                startActivity(intent);
            }
        });

        cari_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CariObat.class);
                startActivity(intent);
            }
        });

        return view;
    }
}