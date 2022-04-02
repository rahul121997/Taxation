package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.purpuligo.taxation.R;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ImageView back_return;
    private TextView privacyPolicyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        back_return = (ImageView) findViewById(R.id.back_privacy);
        privacyPolicyTextView = (TextView) findViewById(R.id.privacyPolicyTextView);

        back_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        String htmlAsString1 = getString(R.string.privacyPolicy);
        Spanned htmlAsSpanned1 = Html.fromHtml(htmlAsString1);
        privacyPolicyTextView.setText(htmlAsSpanned1);
    }
}