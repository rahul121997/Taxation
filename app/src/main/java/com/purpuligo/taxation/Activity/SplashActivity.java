package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.R;

public class SplashActivity extends AppCompatActivity {

    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userSessionManager = new UserSessionManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);
                progressDialog.setMessage("Please Wait..");
                progressDialog.show();

                if (userSessionManager.getLogin()){
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(SplashActivity.this, SendOtpActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },2000);
    }
}