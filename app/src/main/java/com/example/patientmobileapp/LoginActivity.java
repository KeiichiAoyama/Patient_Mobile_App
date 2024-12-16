package com.example.patientmobileapp;

import android.content.DialogInterface;
import android.os.Bundle;
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
        EditText passwordInput = findViewById(R.id.editTextTextEmailAddress);

        Button loginButton = findViewById(R.id.button);
        TextView forgotPasswordButton = findViewById(R.id.textView15);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                MultiChain client = new MultiChain();

                try {
                    JSONArray params = new JSONArray();
                    params.put(email);

                    client.callMultiChain("liststreamitems", params, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
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

                                    if (userAccount != null) {
                                        //compare password
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                showAlertDialog("Login Failed", "Your Account Doesn't Exist");
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