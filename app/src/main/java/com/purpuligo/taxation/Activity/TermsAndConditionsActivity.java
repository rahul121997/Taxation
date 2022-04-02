package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.taxation.R;

public class TermsAndConditionsActivity extends AppCompatActivity {

    private ImageView back_return;
    private TextView termsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        back_return = (ImageView) findViewById(R.id.back_about);
        termsAndConditions = (TextView) findViewById(R.id.termsAndConditionsTextView);

        back_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        String htmlAsString1 = getString(R.string.termsAndConditions);
        Spanned htmlAsSpanned1 = Html.fromHtml(htmlAsString1);
        termsAndConditions.setText(htmlAsSpanned1);

    }
}