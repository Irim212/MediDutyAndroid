package com.example.sebastian.medi;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.sebastian.medi.Model.DateTime;
import com.example.sebastian.medi.Model.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserMenuActivity extends AppCompatActivity {

    TextView helloText;
    Button addDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        helloText = (TextView) findViewById(R.id.helloTextView);
        addDataBtn = (Button) findViewById(R.id.addDataButton);

        final String jwtTokenEncoded = getIntent().getStringExtra("jwtToken");
        final JWT jwtTokenDecoded = new JWT(jwtTokenEncoded);
        Claim claimUserId = jwtTokenDecoded.getClaim("primarysid");
        Claim claimName = jwtTokenDecoded.getClaim("unique_name");
        final String userId = claimUserId.asString();

        String text = "<font color=#cc0029>Witaj </font><font color=#000000>" + claimName.asString() + "</font>";
        helloText.setText(Html.fromHtml(text));

        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();
                String reqResUrl = "https://reqres.in/api/unknown";
                String testUrlEmu = "http://10.0.2.2:5001/api/Scheduler/userId/" + userId;
                String testUrlPho = "http://127.0.0.1:5001/api/Scheduler/userId/" + userId;

                JSONObject postdata = new JSONObject();
                try {
                    postdata.put("userId", userId);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Request request = new Request.Builder()
                        .url(testUrlEmu)
                        .addHeader("authorization", "Bearer " + jwtTokenEncoded)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Failure");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            System.out.println("Login success code: " + response.code());
                            //Login Success
                            final String myResponse = response.body().string();

                            UserMenuActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(myResponse);

                                    StringBuilder newJson = new StringBuilder(myResponse);
                                    newJson.deleteCharAt(0)
                                            .deleteCharAt(newJson.length() - 1);

                                    Gson gson = new Gson();
                                    Type eventType = new TypeToken<ArrayList<Event>>() {}.getType();

                                    ArrayList<Event> scheduleEvent = gson.fromJson(newJson.toString(), eventType);

                                    for (Event e : scheduleEvent) {
                                        addCalendarEvent(e.getTitle(), readDate(e.getStartDate()), readDate(e.getEndDate()));
                                    }
                                }
                            });
                        } else {
                            System.out.println("Login error code: " + response.code());
                        }
                    }
                });
            }
        });
    }

    public void addCalendarEvent(String eventDescription, DateTime startDate, DateTime endDate) {

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startDate.getYear(), startDate.getMonth() - 1, startDate.getDay(), startDate.getHours(), startDate.getMinutes());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(endDate.getYear(), endDate.getMonth() - 1, endDate.getDay(), endDate.getHours(), endDate.getMinutes());
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, eventDescription);
        values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.i("Calendar", "Event Created, the event id is: " + eventID);
            System.out.println(eventDescription + " event added!");
        } else {
            System.out.println("Error event creating.");
        }
    }

    public DateTime readDate(String date) {
        String[] values = date.split("T");
        String[] day = values[0].split("-");
        String[] time = values[1].split(":");

        DateTime dateTime = new DateTime(Integer.parseInt(day[0]), Integer.parseInt(day[1]), Integer.parseInt(day[2]),
                Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        return dateTime;
    }
}
