package com.example.sebastian.medi;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
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


import java.util.Calendar;

public class UserMenuActivity extends AppCompatActivity {

    TextView helloText;
    Button addDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        helloText = (TextView) findViewById(R.id.helloTextView);
        addDataBtn = (Button) findViewById(R.id.addDataButton);

        String jwt = getIntent().getStringExtra("jwtToken");
        JWT jwtToken = new JWT(jwt);

        long currentTime = Calendar.getInstance().getTimeInMillis();


        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCalendarEvent();
            }
        });


    }

    public void addCalendarEvent(){

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2020, 02, 20, 6, 00);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2020, 02, 20, 14, 00);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "Jazzercise");
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        values.put(CalendarContract.Events.ORGANIZER, "google_calendar@gmail.com");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.i("Calendar", "Event Created, the event id is: " + eventID);
            System.out.println("Jazzercise event added!");
        } else {
            System.out.println("Error event creating.");
        }

    }

}
