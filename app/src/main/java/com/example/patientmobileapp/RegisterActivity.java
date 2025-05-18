package com.example.patientmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private Map<String, Object> newUser = new HashMap<>();
    private RegisFragment1 regisFragment1;
    private fragment_regis2 regisFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            regisFragment1 = new RegisFragment1();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, regisFragment1)
                    .commit();

        }

        Button nextButton = findViewById(R.id.button2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

                if (currentFragment instanceof RegisFragment1) {
                    nextButton.setText("Register");
                    collectUserDataRegisFragment1();
                    regisFragment2 = new fragment_regis2();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, regisFragment2)
                            .commit();
                } else if (currentFragment instanceof fragment_regis2) {
                    collectUserDataRegisFragment2();
                    MultiChain client = new MultiChain();

                    try {
                        JSONArray params = new JSONArray();
                        params.put("stream");
                        params.put(newUser.get("email"));
                        params.put(true);

                        client.callMultiChain("create", params, new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e("TESTING", "Request failed: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Log.d("TESTING", "Received Response");

                                if (response.isSuccessful()) {
                                    JSONArray subscribeParams = new JSONArray();
                                    subscribeParams.put(newUser.get("email"));

                                    try {
                                        client.callMultiChain("subscribe", subscribeParams, new Callback() {

                                            @Override
                                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                JSONArray publishParams = new JSONArray();
                                                publishParams.put(newUser.get("email"));
                                                publishParams.put("identity");
                                                try {
                                                    publishParams.put(Helper.stringToHex(User.buildPublishPayload(newUser)));
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }

                                                try {
                                                    client.callMultiChain("publish", publishParams, new Callback() {

                                                        @Override
                                                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                            User user = new User(newUser);
                                                            MyApp app = (MyApp) getApplicationContext();
                                                            app.setUser(user);

                                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                        }

                                                        @Override
                                                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                            Log.e("TESTING", "Request failed: " + e.getMessage());
                                                            e.printStackTrace();
                                                        }
                                                    });
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                Log.e("TESTING", "Request failed: " + e.getMessage());
                                                e.printStackTrace();
                                            }
                                        });
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    Log.d("TESTING", "Response Failed");
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void collectUserDataRegisFragment1() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (fragment instanceof RegisFragment1) {
            this.newUser = ((RegisFragment1) fragment).collectInputData(this.newUser);
        } else {
            Log.e("RegisterActivity", "RegisFragment1 not found");
        }
    }


    private void collectUserDataRegisFragment2() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (fragment instanceof fragment_regis2) {
            this.newUser = ((fragment_regis2) fragment).collectInputData(this.newUser);
        } else {
            Log.e("RegisterActivitxy", "fragment_regis2 not found");
        }
    }
}