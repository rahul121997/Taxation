package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.purpuligo.taxation.R;

public class PaymentDisplayActivity extends AppCompatActivity {
    private String conformPayment1, conformPayment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_display);

        conformPayment1 = getString(R.string.conformPayment1);
        conformPayment2 = getString(R.string.conformPayment2);

        String get = getIntent().getStringExtra("taxPairProfile_id");

                //ALERT MSG
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentDisplayActivity.this);
                builder.setTitle("Payment Successful")
                        .setMessage(conformPayment1+" "+get+" "+conformPayment2)
                        .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
    }
}