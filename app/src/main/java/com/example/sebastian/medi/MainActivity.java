package com.example.sebastian.medi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView appName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appName = (TextView) findViewById(R.id.textView3);
        loginButton = (Button)findViewById(R.id.loginButton);
        String text = "<font color=#cc0029>Medi</font><font color=#000000>Duty</font>";
        appName.setText(Html.fromHtml(text));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(1==1){

                    Intent userMenu = new Intent(MainActivity.this, UserMenuActivity.class);
                    startActivity(userMenu);

                }else{

                }
            }
        });
    }


}
