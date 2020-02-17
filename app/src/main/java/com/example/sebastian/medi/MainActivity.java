package com.example.sebastian.medi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView appName, emailEditText, passwordEditText;
    String url = "http://192.168.1.100:5000/api/";
    static String JWTToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appName = (TextView) findViewById(R.id.textView3);
        loginButton = (Button)findViewById(R.id.loginButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.pwEditText);
        String text = "<font color=#cc0029>Medi</font><font color=#000000>Duty</font>";
        emailEditText.setText("doktor@mail.com");
        passwordEditText.setText("doktor");
        appName.setText(Html.fromHtml(text));


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String emailReq = "eve.holt@reqres.in";
                String password = passwordEditText.getText().toString();
                String passwordReq = "cityslicka";

                if(validateLogin(email, password)){
                    //do login
                    OkHttpClient client = new OkHttpClient();
                    String reqResUrl = "https://reqres.in/api/login";
                    String testUrl = "http://10.0.2.2:5001/api/Login?email=" + email + "&password=" + password;
                    final String jwtTkn = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6IlN0ZWZhbiIsImVtYWlsIjoiZG9rdG9yQG1haWwuY29tIiwicHJpbWFyeXNpZCI6IjYiLCJyb2xlIjoiRG9jdG9yIiwibmJmIjoxNTgxODc3MDY2LCJleHAiOjE1ODE4Nzc5NjYsImlhdCI6MTU4MTg3NzA2NiwiaXNzIjoic2VydmVyIiwiYXVkIjoic2VydmVyIn0.aWHYaO-ecsxIFRyzDlaGUJJbwQywXVTYRO4O5Y5Xsoc";


                    MediaType MEDIA_TYPE = MediaType.parse("application/json");

                    JSONObject postdata = new JSONObject();
                    try {
                        postdata.put("email", emailReq);
                        postdata.put("password", passwordReq);
                    } catch(JSONException e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    RequestBody body = RequestBody.create(postdata.toString(), MEDIA_TYPE);

                    Request request = new Request.Builder()
                            .url(reqResUrl)
                            .post(body)
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

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Login Success
                                        System.out.println(myResponse);
                                        Intent userMenu = new Intent(MainActivity.this, UserMenuActivity.class);
                                        userMenu.putExtra("jwtToken", jwtTkn); //myResponse
                                        startActivity(userMenu);
                                    }
                                });
                            }else{
                                System.out.println("Login error code: " + response.code() );
                            }
                        }
                    });
                }
            }});
        }

        private boolean validateLogin(String email, String password){
            if(email == null || email.trim().length() == 0){
                Toast.makeText(this, "Email jest wymagany.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(password == null || password.trim().length() == 0){
                Toast.makeText(this, "Password jest wymagane.", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

    }
