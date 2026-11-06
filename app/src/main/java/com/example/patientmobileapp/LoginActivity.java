package com.example.patientmobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MyApp app = (MyApp) getApplicationContext();
        if (app.isLoggedIn()) {
            findViewById(android.R.id.content).post(() -> {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
            return;
        }

        EditText emailInput = findViewById(R.id.editTextTextEmailAddress);
        EditText passwordInput = findViewById(R.id.editTextTextPassword);
        com.google.android.material.textfield.TextInputLayout tilEmail = findViewById(R.id.tilEmail);
        com.google.android.material.textfield.TextInputLayout tilPassword = findViewById(R.id.tilPassword);

        Button loginButton = findViewById(R.id.button);
        TextView registerButton = findViewById(R.id.regisdulu);

        emailInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilEmail != null) tilEmail.setError(null);
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        passwordInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilPassword != null) tilPassword.setError(null);
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        passwordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_GO ||
                (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER && event.getAction() == android.view.KeyEvent.ACTION_DOWN)) {
                loginButton.performClick();
                return true;
            }
            return false;
        });

        Runnable resetLoginButton = () -> {
            loginButton.setEnabled(true);
            loginButton.setText("Masuk ke Akun");
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TESTING", "Button Clicked");

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty()) {
                    if (tilEmail != null) {
                        tilEmail.setError("Email tidak boleh kosong");
                    } else {
                        emailInput.setError("Email cannot be empty");
                    }
                    emailInput.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    if (tilPassword != null) {
                        tilPassword.setError("Kata sandi tidak boleh kosong");
                    } else {
                        passwordInput.setError("Password cannot be empty");
                    }
                    passwordInput.requestFocus();
                    return;
                }

                loginButton.setEnabled(false);
                loginButton.setText("Memproses...");

                MultiChain client = new MultiChain();

                try {
                    JSONArray params = new JSONArray();
                    params.put(email);

                    Log.d("TESTING", "Shoot Request ");

                    client.callMultiChain("liststreamitems", params, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.e("TESTING", "Request failed: " + e.getMessage());
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                resetLoginButton.run();
                                showAlertDialog("Koneksi Bermasalah", "Gagal terhubung ke server: " + e.getMessage());
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Log.d("TESTING", "Received Response");

                            if (response.isSuccessful()) {
                                Log.d("TESTING", "Response Successful");

                                String responseBody = response.body() != null ? response.body().string() : "";

                                try {
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    JSONArray resultArray = jsonResponse.getJSONArray("result");

                                    List<JSONObject> items = new ArrayList<>();
                                    for (int i = 0; i < resultArray.length(); i++) {
                                        items.add(resultArray.getJSONObject(i));
                                    }

                                    JSONObject userAccount = items.stream()
                                            .max(Comparator.comparingLong(item -> item.optLong("blocktime", 0)))
                                            .orElse(null);

                                    Log.d("TESTING", "Check User Account");

                                    if (userAccount != null) {
                                        JSONObject credentials = userAccount.getJSONObject("data")
                                                .getJSONObject("json")
                                                .getJSONObject("credentials");

                                        String hashedPassword = credentials.getString("password");

                                        BCrypt.Result result = BCrypt.verifyer().verify(
                                                password.toCharArray(),
                                                hashedPassword.getBytes()
                                        );

                                        Log.d("TESTING", "Check Password");

                                        try{
                                            Log.d("TESTING", "Password Verified: " + result.verified);
                                            if(result.verified) {
                                                JSONObject patientData = userAccount.getJSONObject("data")
                                                        .getJSONObject("json")
                                                        .getJSONObject("patient_data");

                                                User currentUser = User.fromJSON(patientData);

                                                MyApp app = (MyApp) getApplicationContext();
                                                app.setUser(currentUser);

                                                runOnUiThread(() -> {
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                });
                                            } else {
                                                runOnUiThread(() -> {
                                                    resetLoginButton.run();
                                                    showAlertDialog("Gagal Masuk", "Kata sandi yang Anda masukkan salah");
                                                });
                                            }
                                        } catch (JSONException e) {
                                            Log.e("ERROR", "JSON Parsing Error: " + e.getMessage(), e);
                                            runOnUiThread(resetLoginButton::run);
                                        }
                                    } else {
                                        runOnUiThread(() -> {
                                            resetLoginButton.run();
                                            showAlertDialog("Gagal Masuk", "Data akun tidak ditemukan");
                                        });
                                    }
                                } catch (JSONException e) {
                                    Log.e("ERROR", "JSON Parsing Error: " + e.getMessage(), e);
                                    runOnUiThread(resetLoginButton::run);
                                }
                            } else {
                                runOnUiThread(() -> {
                                    resetLoginButton.run();
                                    showAlertDialog("Gagal Masuk", "Akun Anda belum terdaftar");
                                });
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    resetLoginButton.run();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction(() -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(120).start();
                }).start();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        playEntranceAnimations();
    }

    private void playEntranceAnimations() {
        View brand = findViewById(R.id.loginBrandRow);
        View headline = findViewById(R.id.loginHeadlineSection);
        View card = findViewById(R.id.loginCardContainer);
        View registerRow = findViewById(R.id.loginRegisterRow);

        if (brand != null) {
            brand.setAlpha(0f);
            brand.setTranslationY(-20f);
            brand.animate().alpha(1f).translationY(0f).setDuration(400).start();
        }
        if (headline != null) {
            headline.setAlpha(0f);
            headline.setTranslationY(-20f);
            headline.animate().alpha(1f).translationY(0f).setStartDelay(100).setDuration(400).start();
        }
        if (card != null) {
            card.setAlpha(0f);
            card.setScaleX(0.94f);
            card.setScaleY(0.94f);
            card.animate().alpha(1f).scaleX(1f).scaleY(1f).setStartDelay(180).setDuration(450)
                    .setInterpolator(new android.view.animation.OvershootInterpolator(1.2f)).start();
        }
        if (registerRow != null) {
            registerRow.setAlpha(0f);
            registerRow.animate().alpha(1f).setStartDelay(300).setDuration(350).start();
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}