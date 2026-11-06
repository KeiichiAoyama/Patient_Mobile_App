package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApp app = (MyApp) getApplicationContext();
        if (!app.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView2, new HomeFragment())
                    .commit();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            animateNavItem(itemId);

            if (itemId == R.id.navigation_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.navigation_fitur) {
                loadFragment(new FiturFragment());
                return true;
            } else if (itemId == R.id.navigation_bulletin) {
                loadFragment(new BuletinFragment());
                return true;
            } else if (itemId == R.id.navigation_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void animateNavItem(int itemId) {
        try {
            View itemView = bottomNavigation.findViewById(itemId);
            if (itemView != null) {
                View iconView = itemView.findViewById(com.google.android.material.R.id.navigation_bar_item_icon_view);
                View target = (iconView != null) ? iconView : itemView;
                target.setScaleX(0.88f);
                target.setScaleY(0.88f);
                target.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(240)
                        .setInterpolator(new OvershootInterpolator(2.0f))
                        .start();
            }
        } catch (Exception ignored) {
        }
    }

    public void selectNavigationTab(int navItemId) {
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(navItemId);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.tab_switch_enter, R.anim.tab_switch_exit)
                .replace(R.id.fragmentContainerView2, fragment)
                .commit();
    }
}
