package com.example.patientmobileapp;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MyApp app = (MyApp) requireActivity().getApplicationContext();
        User user = app.getUser();
        if (user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return view;
        }
        Log.d("TESTING", user.toString());

        TextView namaPengguna = view.findViewById(R.id.namaPengguna);
        TextView tanggal = view.findViewById(R.id.tanggal);
        TextView ucapan = view.findViewById(R.id.ucapan);

        // Date Setup
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", new Locale("id", "ID"));
        String formattedDate = sdf.format(today);
        if (tanggal != null) {
            tanggal.setText(formattedDate);
        }

        // Contextual Greeting Setup
        if (ucapan != null) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour >= 4 && hour < 11) {
                ucapan.setText("Selamat Pagi,");
            } else if (hour >= 11 && hour < 15) {
                ucapan.setText("Selamat Siang,");
            } else if (hour >= 15 && hour < 18) {
                ucapan.setText("Selamat Sore,");
            } else {
                ucapan.setText("Selamat Malam,");
            }
        }

        if (namaPengguna != null) {
            namaPengguna.setText(user.nama + " 👋");
        }

        // View References
        Button resume_medis_header = view.findViewById(R.id.btnResumeMedisHeader);
        View resume_medis = view.findViewById(R.id.resume_medis);
        View kesehatan_kehamilan = view.findViewById(R.id.kesehatan_kehamilan);
        View pertumbuhan_anak = view.findViewById(R.id.pertumbuhan_anak);
        View rawat_inap = view.findViewById(R.id.rawat_inap);
        View tracker_obat = view.findViewById(R.id.tracker_obat);
        View cari_nakes = view.findViewById(R.id.cari_nakes);
        View cari_obat = view.findViewById(R.id.cari_obat);
        View lainnya = view.findViewById(R.id.lainnya);

        ImageButton qr_scan = view.findViewById(R.id.btnQR);
        ImageButton notif_page = view.findViewById(R.id.btnNotif);
        View notifBadgeDot = view.findViewById(R.id.notifBadgeDot);

        View bulletinOne = view.findViewById(R.id.bulletinHomeOne);
        View bulletinTwo = view.findViewById(R.id.bulletinHomeTwo);
        TextView bulletinHomePage = view.findViewById(R.id.btnBulletinHome);

        View statusCardBpjs = view.findViewById(R.id.statusCardBpjs);
        View statusCardReminder = view.findViewById(R.id.statusCardReminder);

        // Standard click listeners with spring scale animation (100% scroll friendly)
        setAnimatedClickListener(qr_scan, () -> {
            Intent intent = new Intent(getActivity(), QrActivity.class);
            startActivity(intent);
        });

        setAnimatedClickListener(notif_page, () -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        if (resume_medis_header != null) {
            setAnimatedClickListener(resume_medis_header, () -> {
                Intent intent = new Intent(getActivity(), ResumeMedisActivity.class);
                startActivity(intent);
            });
        }

        setAnimatedClickListener(resume_medis, () -> {
            Intent intent = new Intent(getActivity(), ResumeMedisActivity.class);
            startActivity(intent);
        });

        setAnimatedClickListener(kesehatan_kehamilan, () -> {
            Intent intent = new Intent(getActivity(), KesehatanKehamilan.class);
            startActivity(intent);
        });

        setAnimatedClickListener(pertumbuhan_anak, () -> {
            Intent intent = new Intent(getActivity(), PertumbuhanAnak.class);
            startActivity(intent);
        });

        setAnimatedClickListener(rawat_inap, () -> {
            Intent intent = new Intent(getActivity(), CariRawatInap.class);
            startActivity(intent);
        });

        setAnimatedClickListener(tracker_obat, () -> {
            Intent intent = new Intent(getActivity(), TrackerMinumObat.class);
            startActivity(intent);
        });

        setAnimatedClickListener(cari_nakes, () -> {
            Intent intent = new Intent(getActivity(), CariNakesActivity.class);
            startActivity(intent);
        });

        setAnimatedClickListener(cari_obat, () -> {
            Intent intent = new Intent(getActivity(), CariObat.class);
            startActivity(intent);
        });

        setAnimatedClickListener(lainnya, () -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).selectNavigationTab(R.id.navigation_fitur);
            } else {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView2, new FiturFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        setAnimatedClickListener(bulletinOne, () -> {
            Intent intent = new Intent(getActivity(), BuletinDetail.class);
            startActivity(intent);
        });

        setAnimatedClickListener(bulletinTwo, () -> {
            Intent intent = new Intent(getActivity(), BuletinDetail.class);
            startActivity(intent);
        });

        if (bulletinHomePage != null) {
            setAnimatedClickListener(bulletinHomePage, () -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).selectNavigationTab(R.id.navigation_bulletin);
                } else {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView2, new BuletinFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (statusCardBpjs != null) {
            setAnimatedClickListener(statusCardBpjs, () -> {
                Toast.makeText(getContext(), "Status BPJS & Fasilitas Kesehatan: Aktif & Terverifikasi", Toast.LENGTH_SHORT).show();
            });
        }

        if (statusCardReminder != null) {
            setAnimatedClickListener(statusCardReminder, () -> {
                Intent intent = new Intent(getActivity(), TrackerMinumObat.class);
                startActivity(intent);
            });
        }

        // Trigger dynamic entrance animations and badge breathing pulse
        startEntranceAnimations(view);
        startBadgePulseAnimation(notifBadgeDot);

        return view;
    }

    /**
     * Staggered entrance animation for 2020s fluid UI experience
     */
    private void startEntranceAnimations(View root) {
        View headerArea = root.findViewById(R.id.headerArea);
        View heroCardContainer = root.findViewById(R.id.heroCardContainer);
        View statusStrip = root.findViewById(R.id.statusStripContainer);
        View featureRow1 = root.findViewById(R.id.featureRow1);
        View featureRow2 = root.findViewById(R.id.featureRow2);
        View bulletinHeader = root.findViewById(R.id.bulletinHeader);
        View bulletinOne = root.findViewById(R.id.bulletinHomeOne);
        View bulletinTwo = root.findViewById(R.id.bulletinHomeTwo);

        // Header slide down
        if (headerArea != null) {
            headerArea.setTranslationY(-30f);
            headerArea.setAlpha(0f);
            headerArea.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        // Hero card spring scale
        if (heroCardContainer != null) {
            heroCardContainer.setScaleX(0.92f);
            heroCardContainer.setScaleY(0.92f);
            heroCardContainer.setAlpha(0f);
            heroCardContainer.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(1.0f)
                    .setStartDelay(100)
                    .setDuration(450)
                    .setInterpolator(new OvershootInterpolator(1.4f))
                    .start();
        }

        // Status strip slide in
        if (statusStrip != null) {
            statusStrip.setTranslationY(30f);
            statusStrip.setAlpha(0f);
            statusStrip.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setStartDelay(180)
                    .setDuration(380)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        // Feature rows staggered cascade
        if (featureRow1 != null) {
            featureRow1.setTranslationY(40f);
            featureRow1.setAlpha(0f);
            featureRow1.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setStartDelay(240)
                    .setDuration(400)
                    .setInterpolator(new OvershootInterpolator(1.2f))
                    .start();
        }

        if (featureRow2 != null) {
            featureRow2.setTranslationY(40f);
            featureRow2.setAlpha(0f);
            featureRow2.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setStartDelay(320)
                    .setDuration(400)
                    .setInterpolator(new OvershootInterpolator(1.2f))
                    .start();
        }

        // Bulletin section fade and rise
        if (bulletinHeader != null) {
            bulletinHeader.setAlpha(0f);
            bulletinHeader.animate().alpha(1f).setStartDelay(380).setDuration(350).start();
        }

        if (bulletinOne != null) {
            bulletinOne.setTranslationY(35f);
            bulletinOne.setAlpha(0f);
            bulletinOne.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setStartDelay(420)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        if (bulletinTwo != null) {
            bulletinTwo.setTranslationY(35f);
            bulletinTwo.setAlpha(0f);
            bulletinTwo.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setStartDelay(480)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    /**
     * Breathing pulse animation on notification badge
     */
    private void startBadgePulseAnimation(View badge) {
        if (badge == null) return;
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.35f, 1.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.35f, 1.0f);
        ObjectAnimator pulse = ObjectAnimator.ofPropertyValuesHolder(badge, pvhX, pvhY);
        pulse.setDuration(1600);
        pulse.setRepeatCount(ValueAnimator.INFINITE);
        pulse.setInterpolator(new DecelerateInterpolator());
        pulse.start();
    }

    /**
     * Standard OnClickListener with dynamic spring bounce feedback (100% scrollable)
     */
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
