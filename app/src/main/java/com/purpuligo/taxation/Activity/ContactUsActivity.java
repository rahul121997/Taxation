package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.purpuligo.taxation.R;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    private ImageView back_return;
    private TextView contactUsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        back_return = (ImageView) findViewById(R.id.back_contact);
        contactUsTextView = (TextView) findViewById(R.id.contactUsTextView);

        back_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        String htmlAsString1 = getString(R.string.contactUs);
        Spanned htmlAsSpanned1 = Html.fromHtml(htmlAsString1);
        contactUsTextView.setText(htmlAsSpanned1);

    }
}