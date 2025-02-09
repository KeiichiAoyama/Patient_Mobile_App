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

        EditText emailInput = findViewById(R.id.editTextTextEmailAddress);
        EditText passwordInput = findViewById(R.id.editTextTextPassword);

        Button loginButton = findViewById(R.id.button);
        TextView forgotPasswordButton = findViewById(R.id.textView15);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TESTING", "Button Clicked");

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

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
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Log.d("TESTING", "Received Response");

                            if (response.isSuccessful()) {
                                Log.d("TESTING", "Response Successful");

                                String responseBody = response.body().string();

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

                                        Log.d("TESTING", "Password: " + password);

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

                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                runOnUiThread(() -> showAlertDialog("Login Failed", "Incorrect Password"));
                                            }
                                        } catch (JSONException e) {
                                            Log.e("ERROR", "JSON Parsing Error: " + e.getMessage(), e);
                                        }
                                    } else {
                                        runOnUiThread(() -> showAlertDialog("Login Failed", "Your Data is Missing"));
                                    }
                                } catch (JSONException e) {
                                    Log.e("ERROR", "JSON Parsing Error: " + e.getMessage(), e);
                                }
                            } else {
                                runOnUiThread(() -> showAlertDialog("Login Failed", "Your Account Doesn't Exist"));
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //Forgot Password Method
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