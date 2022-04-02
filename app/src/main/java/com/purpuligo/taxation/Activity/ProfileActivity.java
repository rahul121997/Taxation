package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.R;

public class ProfileActivity extends AppCompatActivity {

    TextView phoneNumber,userName, userId, userEmailId;
    Button logoutBtn;
    ImageView backBtn;
    UserSessionManager userSessionManager;
    private String str_userName, str_userPhoneNumber, str_userId, str_emailId;
    private NetworkState networkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        phoneNumber = (TextView) findViewById(R.id.phone_number);
        userName = (TextView) findViewById(R.id.user_Name);
        userEmailId = (TextView) findViewById(R.id.user_emailId);
        //userId = (TextView) view.findViewById(R.id.userId);

        logoutBtn = (Button) findViewById(R.id.logout_btn);
        backBtn = (ImageView) findViewById(R.id.back_btn);

        this.networkState = new NetworkState();

        userSessionManager = new UserSessionManager(getApplicationContext());
        str_userName = userSessionManager.getUserName();
        str_userPhoneNumber= userSessionManager.getPhoneNumber();
        str_emailId = userSessionManager.getEmailId();

        if (networkState.isNetworkAvailable(getApplicationContext())) {
            userName.setText(str_userName);
            phoneNumber.setText(str_userPhoneNumber);
            userEmailId.setText(str_emailId);

            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Log Out");
                    builder.setMessage("Are you sure to Log out?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userSessionManager.setLogin(false);
                            userSessionManager.setPhoneNumber("");
                            userSessionManager.setUserName("");
                            userSessionManager.setEmailId("");
                            userSessionManager.setUserID("");
                            startActivity(new Intent(getApplicationContext(), SendOtpActivity.class));
                            finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }

    }
}