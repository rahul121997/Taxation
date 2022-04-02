package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.purpuligo.taxation.Constants.Constants;
import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.Global.Url;
import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendOtpActivity extends AppCompatActivity {

    private EditText telephone;
    private Button getOtpButton;
    String str_telephone,str_id;
    private int counter = 0;
    private String status, customer_mobile_no;
    private NetworkState networkState;

    //UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        telephone = (EditText) findViewById(R.id.mobileNoInput);
        getOtpButton = (Button) findViewById(R.id.getOtpButton);
        networkState = new NetworkState();
       // userSessionManager = new UserSessionManager(getApplicationContext());


//        getOtpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getOtp();
//            }
//        });
//        if (userSessionManager.getLogin()){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
    }
    public void getOtp(View view) {
//
        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        str_telephone = telephone.getText().toString().trim();


        if (telephone.getText().toString().equals("")) {
            telephone.requestFocus();
            telephone.setError("Field Can not Be Empty");
            //Toast.makeText(this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
        } else if (telephone.length() != 10) {
            telephone.requestFocus();
            telephone.setError("Enter Valid 10 Digits Phone Number");
            //Toast.makeText(this,"Enter Valid Phone Number",Toast.LENGTH_SHORT).show();
        } else {
            if (networkState.isNetworkAvailable(SendOtpActivity.this)) {
            progressDialog.show();

            //String SEND_OTP_URL = "https://purpuligo.com/carsm/index.php/User_mobile/json_user_otp_generate";
            String JSON_URL = Url.SEND_OTP_URL;

            Map<String, String> jsonParams = new HashMap<String, String>();

            jsonParams.put(Constants.SEND_OTP.KEY_Phone, str_telephone);
            JSONObject jasonParam = new JSONObject(jsonParams);

            //JSONObject jsonObject = new JSONObject(response.toString());
            //JSONArray jsonArray = jasonParam.getJSONArray(jso);

           //Log.d("json", "json" + jasonParam);
            //Log.d("telephone", "params" + jsonParams.toString());

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,
                    jasonParam, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                        //editor.putString(Constants.SEND_OTP.KEY_Phone,str_telephone);
                        //editor.apply();
                        progressDialog.dismiss();

                        Log.d("data", "Data from server sendOtp" + response.toString());
                        //{"status":"1","mobile_no":"8343033946"}

                        //getData
                        status = response.getString(Constants.GET_DATA_SEND_OTP.KEY_STATUS);
                        customer_mobile_no = response.getString(Constants.GET_DATA_SEND_OTP.KEY_MOBILE_NUMBER);
                        //Log.d("myStatus","myStatus = "+status);
                        //Log.d("myMobile","myMobile = "+customer_mobile_no);
                        //

                        Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                        //sendNextPage
                        //intent.putExtra("telephone", str_telephone);
                        intent.putExtra(Constants.GET_DATA_SEND_OTP.KEY_STATUS, status);
                        intent.putExtra(Constants.GET_DATA_SEND_OTP.KEY_MOBILE_NUMBER, customer_mobile_no);
//                        Intent intent2 = new Intent(getApplicationContext(), ITR_1_Activity.class);
//                        intent2.putExtra("mobile_no", customer_mobile_no);

                        startActivity(intent);
                        //Toast.makeText(SendOtpActivity.this, response, Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //   Handle Error
                    progressDialog.dismiss();
                    Log.d("Error", "ErrorSendOtp" + error.getMessage().toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("User-agent", System.getProperty("http.agent"));
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SendOtpActivity.this);
            requestQueue.add(postRequest);


        } else {
            Toast.makeText(SendOtpActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1){
            Snackbar.make(getOtpButton,"Do you really want back",Snackbar.LENGTH_LONG).show();
        }else if (counter == 2){
            finish();
        }
    }
}