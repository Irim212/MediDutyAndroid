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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;
import com.example.sebastian.medi.Model.DateTime;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        String date1 = "2020-02-11T04:30:00";

        helloText = (TextView) findViewById(R.id.helloTextView);
        addDataBtn = (Button) findViewById(R.id.addDataButton);

        String jwtTokenEncoded = getIntent().getStringExtra("jwtToken");
        JWT jwtTokenDecoded = new JWT(jwtTokenEncoded);

        DateTime readedDataTime;
        readedDataTime = readDate(date1);

        System.out.println("Rok: " + readedDataTime.getYear() + ". Miesiac: " + readedDataTime.getMonth() + ", Dzien: " + readedDataTime.getDay());
        System.out.println("Godzina: " + readedDataTime.getHours() + ". Minuty: " + readedDataTime.getMinutes() + ", Sekundy: " + readedDataTime.getSeconds());

        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();
                String reqResUrl = "https://reqres.in/api/unknown";
                String testUrl = "http://10.0.2.2:5001/api/";

                MediaType MEDIA_TYPE = MediaType.parse("application/json");

                //RequestBody body = RequestBody.create(postdata.toString(), MEDIA_TYPE);

                Request request = new Request.Builder()
                        .url(reqResUrl)
                        //.post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Failure");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            System.out.println("Login success code: " + response.code() );
                            //Login Success
                            final String myResponse = response.body().string();

                            UserMenuActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(myResponse);

                                }
                            });
                        }else{
                            System.out.println("Login error code: " + response.code() );
                        }
                    }
                });
                //addCalendarEvent();
            }
        });


    }

    public void addCalendarEvent(String eventTitle, String eventDescription){

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2020, 00, 20, 6, 00);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2020, 00, 21, 14, 00);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, eventTitle);
        values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        values.put(CalendarContract.Events.ORGANIZER, "google_calendar@gmail.com");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.i("Calendar", "Event Created, the event id is: " + eventID);
            System.out.println(eventTitle + " event added!");
        } else {
            System.out.println("Error event creating.");
        }

    }

    public DateTime readDate(String date){
        String[] values = date.split("T");
        System.out.println("C Z A S : " + values[1]);
        String[] day = values[0].split("-");
        String[] time = values[1].split(":");

        DateTime dateTime = new DateTime(Integer.parseInt(day[0]), Integer.parseInt(day[1]), Integer.parseInt(day[2]),
                Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        return dateTime;
    }
    

}
