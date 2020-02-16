package com.example.sebastian.medi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class UserMenuActivity extends AppCompatActivity {

    TextView helloText;
    Button addDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        helloText = (TextView) findViewById(R.id.helloTextView);
        addDataBtn = (Button) findViewById(R.id.addDataButton);


    }
}
