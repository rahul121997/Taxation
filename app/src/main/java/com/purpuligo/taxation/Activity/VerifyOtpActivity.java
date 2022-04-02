package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.purpuligo.taxation.Constants.Constants;
import com.purpuligo.taxation.Fragment.HomeFragment;
import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.Global.Url;
import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private TextView textMobileNumber, resendOtp;
    private Button verifyButton;
    String str_code, otp;
    private String status, customer_mobile_no, id, name, emailId;
    private int counter = 0;
    private NetworkState networkState;

    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        inputCode1 = (EditText) findViewById(R.id.inputCode1);
        inputCode2 = (EditText) findViewById(R.id.inputCode2);
        inputCode3 = (EditText) findViewById(R.id.inputCode3);
        inputCode4 = (EditText) findViewById(R.id.inputCode4);
        inputCode5 = (EditText) findViewById(R.id.inputCode5);
        inputCode6 = (EditText) findViewById(R.id.inputCode6);

        userSessionManager = new UserSessionManager(getApplicationContext());

        textMobileNumber = (TextView) findViewById(R.id.textMobileNumber);
        resendOtp = (TextView) findViewById(R.id.resendOtp);

        verifyButton = (Button) findViewById(R.id.verifyButton);
        networkState = new NetworkState();

        // show mobile number
        textMobileNumber.setText(String.format(
                "+91-%s", getIntent().getStringExtra(Constants.GET_DATA_SEND_OTP.KEY_MOBILE_NUMBER)
        ));

        setUpOtpInputs();

        //getDateIn nextPage
        status = getIntent().getStringExtra("status");
        customer_mobile_no = getIntent().getStringExtra("mobile_no");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        //customerOtp = getIntent().getStringExtra("customerOtp");

//        Log.d("telephone", "telephone = " + customer_mobile_no);
//       Log.d("customerStatus", "customerStatus = " + status);
//        Log.d("customerOtp ", "customerOtp " + customerOtp);


        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkState.isNetworkAvailable(VerifyOtpActivity.this)) {
                    progressDialog.show();

                    //String SEND_OTP_URL = "https://purpuligo.com/carsm/index.php/User_mobile/json_user_otp_generate";
                    String JSON_URL = Url.SEND_OTP_URL;

                    Map<String, String> jsonParams = new HashMap<String, String>();

                    jsonParams.put(Constants.SEND_OTP.KEY_Phone, customer_mobile_no);
                    JSONObject jasonParam = new JSONObject(jsonParams);

                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,
                            jasonParam, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOtpActivity.this,"Resend OTP In your Mobile",Toast.LENGTH_SHORT).show();
                                status = response.getString(Constants.GET_DATA_SEND_OTP.KEY_STATUS);
                                customer_mobile_no = response.getString(Constants.GET_DATA_SEND_OTP.KEY_MOBILE_NUMBER);
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
                    RequestQueue requestQueue = Volley.newRequestQueue(VerifyOtpActivity.this);
                    requestQueue.add(postRequest);

                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//        if (userSessionManager.getLogin()){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }


    public void verifyBt(View view) {
        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        // String otp = String.valueOf(customerOtp);
        str_code = inputCode1.getText().toString() + inputCode2.getText().toString() +
                inputCode3.getText().toString() + inputCode4.getText().toString() +
                inputCode5.getText().toString() + inputCode6.getText().toString();
        Log.d("code", "code" + str_code);

        if (inputCode1.getText().toString().trim().isEmpty()
                || inputCode2.getText().toString().trim().isEmpty()
                || inputCode3.getText().toString().trim().isEmpty()
                || inputCode4.getText().toString().trim().isEmpty()
                || inputCode5.getText().toString().trim().isEmpty()
                || inputCode6.getText().toString().trim().isEmpty()) {

            Toast.makeText(VerifyOtpActivity.this, "Please enter code", Toast.LENGTH_SHORT).show();
        } else {
            if (networkState.isNetworkAvailable(VerifyOtpActivity.this)) {
                //if(str_code.equals(otp) ){
                progressDialog.show();

               // String VERIFICATION_OTP_URL = "https://purpuligo.com/carsm/index.php/User_mobile/json_user_otp_verification";
                String JSON_URL = Url.VERIFICATION_OTP_URL;
                Map<String, String> jsonParams = new HashMap<String, String>();

                jsonParams.put(Constants.VERIFY_OTP.KEY_Phone, customer_mobile_no);
                jsonParams.put(Constants.VERIFY_OTP.KEY_OTP, str_code);
                JSONObject jasonParam = new JSONObject(jsonParams);

                Log.d("json", "json" + jasonParam);

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,
                        jasonParam, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();

                            Log.d("data", "Data from server Verify" + response.toString());
                            //{"mobile_no":"8343033946","status":"1","user_id":"1"}

                            //getData
                            customer_mobile_no = response.getString(Constants.GET_DATA_VERIFY_OTP.KEY_MOBILE_NUMBER);
                            status = response.getString(Constants.GET_DATA_VERIFY_OTP.KEY_STATUS);
                            id = response.getString(Constants.GET_DATA_VERIFY_OTP.KEY_USER_ID);
                            Log.d("myId","myID = "+id);
                            name = response.getString("user_name");
                            emailId = response.getString("email_id");

                            if (status.equals("1")) {
                                //{"mobile_no":"8343033946","status":"1","user_id":"10"}
                                if (id.equals("0")) {
                                    userSessionManager.setLogin(true);
                                    //userSessionManager.setPhoneNumber(customer_mobile_no);

                                    Toast.makeText(VerifyOtpActivity.this, "OTP Verification Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ProfileSubmitActivity.class);
                                    //sendNextPage
                                    intent.putExtra(Constants.GET_DATA_VERIFY_OTP.KEY_MOBILE_NUMBER, customer_mobile_no);
                                    startActivity(intent);
                                } else {
                                    userSessionManager.setLogin(true);
                                    userSessionManager.setPhoneNumber(customer_mobile_no);
                                    userSessionManager.setUserID(id);
                                    userSessionManager.setUserName(name);
                                    userSessionManager.setEmailId(emailId);
                                    Toast.makeText(VerifyOtpActivity.this, "OTP Verification Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    //sendNextPage
                                    //intent.putExtra(Constants.GET_DATA_VERIFY_OTP.KEY_MOBILE_NUMBER, customer_mobile_no);
                                    startActivity(intent);
                                }
                                finish();
                            } else {
                                //{"mobile_no":"8343033946","status":"0",user_id":"0"}
                                Toast.makeText(VerifyOtpActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                                return;
                            }
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
                RequestQueue requestQueue = Volley.newRequestQueue(VerifyOtpActivity.this);
                requestQueue.add(postRequest);
            } else {
                Toast.makeText(VerifyOtpActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }
                    // progressDialog.dismiss();
                   // Toast.makeText(VerifyOtpActivity.this,"Verification Successfully",Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(getApplicationContext(), ProfileSubmitActivity.class);

                    //sendNextPage
                   // intent.putExtra("telephone", customerTelephone);
                    //intent.putExtra("customerId",customerId);
                    //intent.putExtra("customerOtp",customerOtp);

                    //startActivity(intent);
                    //finish();

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1){
            Snackbar.make(verifyButton,"Do you really want exit",Snackbar.LENGTH_LONG).show();
        }else if (counter == 2){
            finish();
        }
    }


    private void setUpOtpInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    inputCode1.requestFocus();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}