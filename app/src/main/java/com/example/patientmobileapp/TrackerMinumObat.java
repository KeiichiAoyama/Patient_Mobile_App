package com.example.patientmobileapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TrackerMinumObat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracker_minum_obat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MyApp app = (MyApp) getApplicationContext();
        User user = app.getUser();

        RecyclerView recyclerView = findViewById(R.id.trackerRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<obat_reminder_card> itemList = new ArrayList<>();

        MultiChain client = new MultiChain();

        try {
            String userObatListObjectName = "obat-" + user.nik;

            JSONArray params = new JSONArray();
            params.put(userObatListObjectName);

            client.callMultiChain("liststreamitems", params, new Callback() {

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("TESTING", "Received Response");

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Log.d("MULTICHAIN_JSON", responseBody);

                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            JSONArray resultArray = jsonResponse.getJSONArray("result");

                            JSONObject latestItem = null;
                            long latestTime = 0;

                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject item = resultArray.getJSONObject(i);
                                long blocktime = item.optLong("blocktime", 0);

                                if (blocktime > latestTime) {
                                    latestTime = blocktime;
                                    latestItem = item;
                                }
                            }

                            if (latestItem != null) {
                                JSONObject dataObj = latestItem.getJSONObject("data");
                                JSONArray obatArray = dataObj.getJSONArray("json");

                                for (int i = 0; i < obatArray.length(); i++) {
                                    JSONObject obat = obatArray.getJSONObject(i);
                                    String name = obat.optString("title");
                                    String dose = obat.optString("subtitle1");
                                    String when = obat.optString("subtitle2");
                                    String time = obat.optString("subtitle3");
                                    itemList.add(new obat_reminder_card(name, dose, when, time));
                                    scheduleAlarm(name, time);

                                }

                                runOnUiThread(() -> {
                                    obatReminderCardAdapter adapter = new obatReminderCardAdapter(
                                            TrackerMinumObat.this,
                                            itemList,
                                            item -> {}
                                    );
                                    recyclerView.setAdapter(adapter);
                                });

                            }
                        } catch (JSONException e) {
                            Log.e("ERROR", "JSON Parsing Error: " + e.getMessage(), e);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("TESTING", "Request failed: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch(JSONException e) {
            e.printStackTrace();
        }

    }

    private void scheduleAlarm(String title, String timeString) {
        try {
            String[] timeParts = timeString.split("\\.");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }

            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.putExtra("title", title);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    title.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
        } catch (Exception e) {
            Log.e("AlarmSetup", "Failed to parse time or set alarm: " + e.getMessage());
        }
    }
}