package com.example.sebastian.medi;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.provider.CalendarContract.Events.*;

public class UserMenuActivity extends AppCompatActivity {

    TextView helloText;
    CalendarView calendarView;
    Button addDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        helloText = (TextView) findViewById(R.id.helloTextView);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        addDataBtn = (Button) findViewById(R.id.addDataButton);

        String jwt = getIntent().getStringExtra("jwtToken");
        JWT jwtToken = new JWT(jwt);

        long currentTime = Calendar.getInstance().getTimeInMillis();
        calendarView.setDate(currentTime);


        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCalendarEvent();
            }
        });


    }

    public void addCalendarEvent(){
        long calID = 3; // Make sure to which calender you want to add event
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2020, 2, 20, 8, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2020, 2, 20, 16, 30);
        endMillis = endTime.getTimeInMillis();


        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(DTSTART, startMillis);
        values.put(DTEND, endMillis);
        values.put(TITLE, "Hackathon");
        values.put(DESCRIPTION, "do some code");
        values.put(CALENDAR_ID, calID);
        values.put(EVENT_TIMEZONE, TimeZone.getDefault().getID());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri = cr.insert(CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
    }

}
