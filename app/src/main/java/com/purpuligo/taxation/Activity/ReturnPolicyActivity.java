package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.purpuligo.taxation.R;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ReturnPolicyActivity extends AppCompatActivity {

    private ImageView back_return;
    private TextView returnPolicyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_policy);

        back_return = (ImageView) findViewById(R.id.back_return);
        returnPolicyTextView = (TextView) findViewById(R.id.returnPolicyTextView);

        back_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        try {
        String htmlAsString1 = getString(R.string.returnPolicy);
        Spanned htmlAsSpanned1 = Html.fromHtml(htmlAsString1);
        returnPolicyTextView.setText(htmlAsSpanned1);
        }catch (Exception e){e.printStackTrace();}
    }
}