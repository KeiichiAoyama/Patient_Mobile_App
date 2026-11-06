package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BuletinFragment extends Fragment {

    public BuletinFragment() {}

    public static BuletinFragment newInstance(String param1, String param2) {
        return new BuletinFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buletin, container, false);

        SearchView searchView = view.findViewById(R.id.search);
        View featuredCard = view.findViewById(R.id.cardFeaturedBuletin);
        View article1 = view.findViewById(R.id.cardArticle1);
        View article2 = view.findViewById(R.id.cardArticle2);
        View article3 = view.findViewById(R.id.cardArticle3);

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent(getActivity(), BuletinSearch.class);
                    intent.putExtra("QUERY", query);
                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        setAnimatedClickListener(featuredCard, () -> {
            Intent intent = new Intent(getActivity(), BuletinDetail.class);
            startActivity(intent);
        });

        setAnimatedClickListener(article1, () -> {
            Intent intent = new Intent(getActivity(), BuletinDetail.class);
            startActivity(intent);
        });

        setAnimatedClickListener(article2, () -> {
            Intent intent = new Intent(getActivity(), BuletinDetail.class);
            startActivity(intent);
        });

        setAnimatedClickListener(article3, () -> {
            Intent intent = new Intent(getActivity(), BuletinDetail.class);
            startActivity(intent);
        });

        // Entrance animation
        View buletinScroll = view.findViewById(R.id.buletinScroll);
        if (buletinScroll != null) {
            buletinScroll.setAlpha(0f);
            buletinScroll.setTranslationY(25f);
            buletinScroll.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(350)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        return view;
    }

    private void setAnimatedClickListener(View view, Runnable action) {
        if (view == null) return;
        view.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.94f)
                    .scaleY(0.94f)
                    .setDuration(90)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(160)
                                .setInterpolator(new OvershootInterpolator(2.0f))
                                .withEndAction(() -> {
                                    if (action != null) action.run();
                                })
                                .start();
                    })
                    .start();
        });
    }
}
