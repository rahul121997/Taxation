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
import android.widget.ProgressBar;
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

public class ProfileSubmitActivity extends AppCompatActivity {

    private EditText emailIdInput, nameInput;
    private Button submitButton;
    private NetworkState networkState;
    private ProgressBar progressBar;
    UserSessionManager userSessionManager;
    private int counter = 0;
    private String customerId, customerTelephone, customerEmail, customerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_submit);

        emailIdInput = (EditText) findViewById(R.id.emailIdInput);
        nameInput = (EditText) findViewById(R.id.nameInput);
        submitButton = (Button) findViewById(R.id.submitButton);
        networkState = new NetworkState();

        //getDateIn nextPage
        userSessionManager = new UserSessionManager(getApplicationContext());
        //customerTelephone = userSessionManager.getPhoneNumber();
        customerTelephone = getIntent().getStringExtra(Constants.GET_DATA_VERIFY_OTP.KEY_MOBILE_NUMBER);
        Log.d("myPhone", "myPhone = " + customerTelephone );

    }

    public void submitBt(View view) {
        customerEmail = emailIdInput.getText().toString().trim();
        customerName = nameInput.getText().toString().trim();
        //String str_id = "1";
        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

//        if (networkState.isNetworkAvailable(this)) {
        //validation
        if (customerEmail.length() == 0) {
            emailIdInput.requestFocus();
            emailIdInput.setError("Enter Your Email Id");

        } else if (!customerEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            emailIdInput.requestFocus();
            emailIdInput.setError("Enter Valid Email");
        } else if (customerName.length() == 0) {
            nameInput.requestFocus();
            nameInput.setError("Enter Your Name");
        } else {
            if (networkState.isNetworkAvailable(ProfileSubmitActivity.this)) {

                progressDialog.show();


                //String REGISTER_URL = "https://purpuligo.com/carsm/index.php/User_mobile/json_user_registration";
                String JSON_URL = Url.REGISTER_URL;
                Map<String, String> jsonParams = new HashMap<String, String>();

                jsonParams.put(Constants.PROFILE_SUBMIT_DETAILS.KEY_EMAIL_ID, customerEmail);
                jsonParams.put(Constants.PROFILE_SUBMIT_DETAILS.KEY_USER_NAME, customerName);
                jsonParams.put(Constants.PROFILE_SUBMIT_DETAILS.KEY_TELEPHONE, customerTelephone);

               // {"email_id":"its.jgm@gmail.com", "user_name":"demo", "telephone":"9999999999"}

                JSONObject jasonParam = new JSONObject(jsonParams);

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,
                        jasonParam, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            progressDialog.dismiss();

                            Log.d("data", "Data from server ProfileSubmit" + response.toString());
                            //{"user_id":"9","email_id":"abc@gmail.com"}
                            //getData
                            customerId = response.getString(Constants.GET_PROFILE_DETAILS.KEY_USER_ID);
                            customerEmail = response.getString(Constants.GET_PROFILE_DETAILS.KEY_EMAIL_ID);

                            userSessionManager.setLogin(true);
                            userSessionManager.setPhoneNumber(customerTelephone);
                            userSessionManager.setUserID(customerId);
                            userSessionManager.setEmailId(customerEmail);
                            userSessionManager.setUserName(customerName);

                            //  Log.d("myId","myID = "+customerIdSubmit);
                            //  Log.d("myEmail","myEmail = "+customerEmail);
                            //
//                            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString(KEY_EMAIL_ID,customerEmail);
//                            editor.apply();

                            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            //sendNextPage
//                            intent.putExtra("id", customerIdSubmit);
//                            intent.putExtra("telephone", customerTelephone);
//                            intent.putExtra("email", customerEmail);
//                            intent.putExtra("user_name", customerName);
                            startActivity(intent);
//                            Intent intent1 = new Intent(getApplicationContext(),ITR_1_Activity.class);
//                            intent1.putExtra("id", customerIdSubmit);
//                            intent1.putExtra("telephone", customerTelephone);
//                            intent1.putExtra("email", customerEmail);
//                            intent1.putExtra("user_name", customerName);
//                            Intent intent2 = new Intent(getApplicationContext(),ITR_1_Activity.class);
//                            intent2.putExtra("id", customerIdSubmit);
//                            intent2.putExtra("telephone", customerTelephone);
//                            intent2.putExtra("email", customerEmail);
//                            intent2.putExtra("user_name", customerName);
                            //Toast.makeText(SendOtpActivity.this, response, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d("Error", "ErrorSubmit" + error.getMessage().toString());
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
                RequestQueue requestQueue = Volley.newRequestQueue(ProfileSubmitActivity.this);
                requestQueue.add(postRequest);
            } else {
                Toast.makeText(ProfileSubmitActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1) {
            Snackbar.make(emailIdInput, "Do you really want exit", Snackbar.LENGTH_LONG).show();
        } else if (counter == 2) {
            Intent intent = new Intent(ProfileSubmitActivity.this, SendOtpActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
