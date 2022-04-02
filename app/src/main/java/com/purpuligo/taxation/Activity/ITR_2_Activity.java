package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.taxation.Constants.Constants;
import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.Global.Url;
import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.R;
import com.razorpay.Checkout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ITR_2_Activity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteCategoryITR2, autoCompleteSelectStateITR2,
            autoCompleteSelectHouseTypeITR2, autoCompleteSelectBankNameITR2, autoCompleteFinancialYearITR2;
    private EditText nameCustomer2, dateOfBirth2, addressCustomer2, cityTownCustomer2, pinCodeCustomer2, panCardCustomer2, aadhaarCustomer2,
            detailsPropertyCustomer2, interestHBLCustomer2, incomeSbInterestCustomer2, accountHolderNameCustomer2,
            IFSCCodeCustomer2, accountHolderNumberCustomer2, dividend2, taxRelevant2;
    private String str_name2, str_dateOfBirth2, str_address2, str_pinCode2, str_panCard2, str_aadhaar2,
            str_propertyDetails2, str_financialYear2, str_interestHBL2, str_incomeSbInterest2, str_accountHolderName2,
            str_ifscCode2, str_accountNumber2, str_category2, str_cityTown2, str_state2, str_bankName2, str_houseType2,
            str_dividend2, str_taxRelevant, taxPairProfile_id, types,  orderID, createdAt, orderAmount, orderCurrency,
            userID, userTelephone, userEmail, userName, price, totalPrice;
    DatePickerDialog.OnDateSetListener setListener;

    ArrayList<String > bankList = new ArrayList<>();
    ArrayAdapter<String> bankAdapter;
    private ImageView  backImgBtn, calenderImageBtn2;
    private int counter = 0;

    private NetworkState networkState;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itr2);
        Checkout.preload(getApplicationContext());

        autoCompleteCategoryITR2 = (AutoCompleteTextView) findViewById(R.id.selectCategoryITR2);
        autoCompleteSelectStateITR2 = (AutoCompleteTextView) findViewById(R.id.selectStateITR2);
//        autoCompleteSelectDistrictITR2 = (AutoCompleteTextView) findViewById(R.id.selectDistrictITR2);
        autoCompleteSelectBankNameITR2 = (AutoCompleteTextView) findViewById(R.id.selectBankNameITR2);
        autoCompleteFinancialYearITR2 = (AutoCompleteTextView) findViewById(R.id.financialYearITR2);
        autoCompleteSelectHouseTypeITR2 = (AutoCompleteTextView) findViewById(R.id.houseTypeITR2);

        nameCustomer2 = (EditText) findViewById(R.id.name_ITR2);
        dateOfBirth2 = (EditText) findViewById(R.id.dateOfBirth_ITR2);
        addressCustomer2 = (EditText) findViewById(R.id.address_ITR2);
        cityTownCustomer2 = (EditText) findViewById(R.id.cityTown_ITR2);
        pinCodeCustomer2 = (EditText) findViewById(R.id.pinCode_ITR2);
        panCardCustomer2 = (EditText) findViewById(R.id.panCardNumber_ITR2);
        aadhaarCustomer2 = (EditText) findViewById(R.id.aadhaarCardNumber_ITR2);
        detailsPropertyCustomer2 = (EditText) findViewById(R.id.house_property_infoITR2);
        interestHBLCustomer2 = (EditText) findViewById(R.id.interest_HBL_ITR2);
        incomeSbInterestCustomer2 = (EditText) findViewById(R.id.incomeSbInterestITR2);
        accountHolderNameCustomer2 = (EditText) findViewById(R.id.account_holder_name_ITR2);
        IFSCCodeCustomer2 = (EditText) findViewById(R.id.account_IFSC_code_ITR2);
        accountHolderNumberCustomer2 = (EditText) findViewById(R.id.account_holder_number_ITR2);
       // accountHolderReEnterNumberCustomer2 = (EditText) findViewById(R.id.account_holder_reEnter_number_ITR2);
        dividend2 = (EditText) findViewById(R.id.dividendITR2);
        taxRelevant2 = (EditText) findViewById(R.id.taxRelevantITR2);
        calenderImageBtn2 = (ImageView) findViewById(R.id.calenderImageBtn2);

        backImgBtn = (ImageView) findViewById(R.id.back_img_btn);
        networkState = new NetworkState();

        //calender
        calenderImageBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender();
            }
        });
        dateOfBirth2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                   calender();
                }
            }
        });

        //get Date
        userSessionManager = new UserSessionManager(getApplicationContext());
        userTelephone = userSessionManager.getPhoneNumber();
        userID = userSessionManager.getUserID();
        userEmail = userSessionManager.getEmailId();
        userName = userSessionManager.getUserName();
        Log.d("userTelephone", "userTelephone = " + userTelephone);
        Log.d("userId", "userId = " + userID);
        Log.d("userEmail", "userEmail = " + userEmail);
        Log.d("userName", "userName = " + userName);


        category();
        state();
        houseType();
        bank();
        financialYear();


        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ITR_2_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void calender(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ITR_2_Activity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String str_month = null;
                String str_day = null;
                month = month + 1;
                if ((month + "").length() < 2) {
                    str_month = "0" + month;
                } else {
                    str_month = "" + month;
                }
                if ((day + "").length() < 2) {
                    str_day = "0" + day;
                } else {
                    str_day = "" + day;
                }
                String date = str_day + "-" + str_month + "-" + year;
                //String date = day+"/"+month+"/"+year;
                dateOfBirth2.setText(date);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }



    //button click
    public void itr2_submitBt(View view) {
        stringAll();

        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        //validation
        if(str_category2.length() == 0) {
            autoCompleteCategoryITR2.requestFocus();
            autoCompleteCategoryITR2.setError("Chose any Category");
        }else if (str_name2.length() == 0) {
            nameCustomer2.requestFocus();
            nameCustomer2.setError("Enter Your Name");
        }else if (str_dateOfBirth2.length() == 0){
            dateOfBirth2.requestFocus();
            dateOfBirth2.setError("Enter Your DateOfBirth");
        } else if (str_address2.length() == 0) {
            addressCustomer2.requestFocus();
            addressCustomer2.setError("Enter Your Address");
        } else if (str_state2.length() == 0) {
            autoCompleteSelectStateITR2.requestFocus();
            autoCompleteSelectStateITR2.setError("Chose Your State");
        }else if(str_cityTown2.length() == 0){
            cityTownCustomer2.requestFocus();
            cityTownCustomer2.setError("Enter Your City/Town");
        } else if (str_pinCode2.length() == 0) {
            pinCodeCustomer2.requestFocus();
            pinCodeCustomer2.setError("Enter Your PinCode");
        } else if (str_pinCode2.length() <= 5) {
            pinCodeCustomer2.requestFocus();
            pinCodeCustomer2.setError("Enter Valid PinCode");
        } else if (str_panCard2.length() == 0) {
            panCardCustomer2.requestFocus();
            panCardCustomer2.setError("Enter Your PanCard Number");
        } else if (str_panCard2.length() <= 9) {
            panCardCustomer2.requestFocus();
            panCardCustomer2.setError("Enter Valid PanCard Number");
        } else if (str_aadhaar2.length() == 0) {
            aadhaarCustomer2.requestFocus();
            aadhaarCustomer2.setError("Enter Your Aadhaar Card Number");
        } else if (str_aadhaar2.length() <= 11) {
            aadhaarCustomer2.requestFocus();
            aadhaarCustomer2.setError("Enter Your Aadhaar Card Number");
        } else if (str_financialYear2.length() == 0) {
            autoCompleteFinancialYearITR2.requestFocus();
            autoCompleteFinancialYearITR2.setError("Chose Financial Year");
        } else if (str_houseType2.length() == 0) {
            autoCompleteSelectHouseTypeITR2.requestFocus();
            autoCompleteSelectHouseTypeITR2.setError("Chose House Type");
        } else if (str_propertyDetails2.length() == 0) {
            detailsPropertyCustomer2.requestFocus();
            detailsPropertyCustomer2.setError("Enter Your Property");
        } else if (str_interestHBL2.length() == 0) {
            interestHBLCustomer2.requestFocus();
            interestHBLCustomer2.setError("Enter Your Interest HBL");
        } else if (str_incomeSbInterest2.length() == 0) {
            incomeSbInterestCustomer2.requestFocus();
            incomeSbInterestCustomer2.setError("Enter Your Sb Interest");
        }else if (str_dividend2.length() == 0) {
            dividend2.requestFocus();
            dividend2.setError("Enter Dividend");
        }else if (str_taxRelevant.length() == 0) {
            taxRelevant2.requestFocus();
            taxRelevant2.setError("Enter Dividend");
        } else if (str_bankName2.length() == 0) {
            autoCompleteSelectBankNameITR2.requestFocus();
            autoCompleteSelectBankNameITR2.setError("Chose Your Bank");
        } else if (str_accountHolderName2.length() == 0) {
            accountHolderNameCustomer2.requestFocus();
            accountHolderNameCustomer2.setError("Enter Account Holder Name");
        } else if (str_ifscCode2.length() == 0) {
            IFSCCodeCustomer2.requestFocus();
            IFSCCodeCustomer2.setError("Enter Your IFSC Code");
        } else if (str_accountNumber2.length() == 0) {
            accountHolderNumberCustomer2.requestFocus();
            accountHolderNumberCustomer2.setError("Enter Account Number");
        } else if (str_accountNumber2.length() <= 7) {
            accountHolderNumberCustomer2.requestFocus();
            accountHolderNumberCustomer2.setError("Enter Valid Account Number");
//        } else if (str_reEnterAccountNumber2.length() == 0) {
//            accountHolderReEnterNumberCustomer2.requestFocus();
//            accountHolderReEnterNumberCustomer2.setError("Enter Account Number");
//        } else if (str_reEnterAccountNumber2.length() <= 7) {
//            accountHolderReEnterNumberCustomer2.requestFocus();
//            accountHolderReEnterNumberCustomer2.setError("Enter Valid Account Number");
//        } else if (!str_accountNumber2.equals(str_reEnterAccountNumber2)) {
//            accountHolderReEnterNumberCustomer2.requestFocus();
//            accountHolderReEnterNumberCustomer2.setError("Account Number is Not Match");
        } else {
            if (networkState.isNetworkAvailable(ITR_2_Activity.this)) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ITR_2_Activity.this);
                builder.setTitle("Confirmation")
                        .setIcon(R.drawable.alert_icon)
                        .setMessage(R.string.alertMsgITR2)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.show();

                                //String DATA_SUBMIT_URL_ITR1 =  "https://purpuligo.com/carsm/index.php/User_mobile/json_itr_profile_data_submit";
                                String JSON_URL_SUBMIT = Url.DATA_SUBMIT_URL_ITR;

                                Map<String, String> jsonParams = new HashMap<String, String>();
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_USER_ID, userID);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_TYPE, "ITR2");
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_CATEGORY, str_category2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_NAME, str_name2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_DOB,str_dateOfBirth2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_ADDRESS, str_address2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_PIN_NO, str_pinCode2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_STATE, str_state2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_CITY_TOWN, str_cityTown2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_PAN_NO, str_panCard2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_AADHAAR_NO, str_aadhaar2);

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_HOUSE_PROPERTY, str_propertyDetails2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_INTEREST_HOUSE_PROPERTY, str_incomeSbInterest2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_INCOME, str_incomeSbInterest2);

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_BANK_NAME, str_bankName2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_ACCOUNT_HOLDER_NAME, str_accountHolderName2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_IFSC_CODE, str_ifscCode2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_ACCOUNT_NO, str_accountNumber2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_FINANCIAL_YEAR, str_financialYear2);

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_DIVIDEND,str_dividend2);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_TAX_RELEVANT,str_taxRelevant);

                                //houseType convert
                                String int_houseType;
                                if(str_houseType2.equals("Rent")){
                                    int_houseType="0";
                                    jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_HOUSE_TYPE,int_houseType);
                                }
                                if(str_houseType2.equals("Self(Own)")) {
                                    int_houseType="1";
                                    jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_HOUSE_TYPE,int_houseType);
                                }

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_IMAGE, "");

                                JSONObject jasonParam = new JSONObject(jsonParams);
                                Log.d("json", "json" + jasonParam);

                                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL_SUBMIT,
                                        jasonParam, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                           // progressDialog.dismiss();
                                            Log.d("data", "Data from server ITR2 Submit" + response.toString());

                                            //getData
                                            taxPairProfile_id = response.getString(Constants.GET_DATA_ITR_SUBMIT.KEY_TAX_PAIR_PROFILE_ID);
                                            //types = response.getString("type");
                                            //api -response- {"tax_pair_profile_id":"1"}
                                            Log.d("tax_id", "tax_id = " + taxPairProfile_id);
                                           // Toast.makeText(ITR_2_Activity.this, "submit successfully", Toast.LENGTH_SHORT).show();


                                            //amount
                                            //String AMOUNT_URL = "https://purpuligo.com/carsm/index.php/User_mobile/tax_return_type_master_list";
                                            String JSON_URL_AMOUNT = Url.AMOUNT_URL;

                                            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_AMOUNT, null,
                                                    new Response.Listener<JSONArray>() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {

                                                            try {
                                                                //JSONArray jsonArray = response.getJSONArray("");
                                                                for (int i=0;i<response.length();i++) {
                                                                    JSONObject jsonObject = response.getJSONObject(i);
                                                                    String ITR2 = jsonObject.optString("type");
                                                                    if (ITR2.equals("ITR2") == true) {
                                                                        price = jsonObject.optString("price");
                                                                        Log.d("Price2", "Price2: " + price);
                                                                        // amount in the smallest currency unit
                                                                        int n = Integer.parseInt(price);
                                                                        int sum = n*100;
                                                                        totalPrice = Integer.toString(sum);
                                                                    }
                                                                }
                                                                    //order id
                                                                    //String ORDER_ID =  "https://purpuligo.com/carsm/index.php/RPay/generate_order";
                                                                    String JSON_URL_ORDER_ID = Url.ORDER_ID;

                                                                    Map<String, String> jsonParams2 = new HashMap<String, String>();
                                                                    jsonParams2.put(Constants.ORDER_ID_GEN.KEY_AMOUNT, totalPrice);
                                                                    jsonParams2.put(Constants.ORDER_ID_GEN.KEY_ORDER_USER_ID, taxPairProfile_id);
                                                                    jsonParams2.put(Constants.ORDER_ID_GEN.KEY_TYPE, "ITR2");

                                                                    JSONObject jasonParam2 = new JSONObject(jsonParams2);
                                                                    Log.d("json", "json" + jasonParam2);
                                                                    Log.d("user_id", "user_id : " + jsonParams2.toString());
                                                                    Log.d("type", "type : " + jsonParams2.toString());

                                                                    JsonObjectRequest postRequest2 = new JsonObjectRequest(Request.Method.POST, JSON_URL_ORDER_ID,
                                                                            jasonParam2, new Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response2) {

                                                                            try {
                                                                                //progressDialog.show();
                                                                                Log.d("order", "order id: " + response2.toString());

                                                                                orderID = response2.getString("id");
                                                                                orderAmount = response2.getString("amount");
                                                                                orderCurrency = response2.getString("currency");
                                                                                createdAt = response2.getString("created_at");
                                                                                //Log.d("id2","id2 : "+orderID);
                                                                                Intent intent = new Intent(getApplicationContext(), Form16UploadPaymentActivity.class);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_TAX_PAIR_ID, taxPairProfile_id);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_ID, orderID);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_CREATE_AT, createdAt);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_AMOUNT, orderAmount);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_CURRENCY, orderCurrency);
                                                                                startActivity(intent);
                                                                                finish();
                                                                               // startPayment();

                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            progressDialog.dismiss();
                                                                            Log.d("Error", "ErrorItr2Submit " + error);
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
                                                                    RequestQueue requestQueue2 = Volley.newRequestQueue(ITR_2_Activity.this);
                                                                    requestQueue2.add(postRequest2);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("VolleyError","VolleyError: "+error.toString());

                                                }
                                            });
                                            RequestQueue requestQueue = Volley.newRequestQueue(ITR_2_Activity.this);
                                            requestQueue.add(jsonObjectRequest);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Log.d("Error", "ErrorSubmitITR2" + error);
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
                                //progressDialog.dismiss();
                                RequestQueue requestQueue = Volley.newRequestQueue(ITR_2_Activity.this);
                                requestQueue.add(postRequest);
                            }
                        }).setNegativeButton("NO",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Toast.makeText(ITR_2_Activity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stringAll(){
        str_name2 = nameCustomer2.getText().toString();
        str_dateOfBirth2 = dateOfBirth2.getText().toString();
        str_address2 = addressCustomer2.getText().toString();
        str_pinCode2 = pinCodeCustomer2.getText().toString().trim();
        str_panCard2 = panCardCustomer2.getText().toString().trim();
        str_aadhaar2 = aadhaarCustomer2.getText().toString().trim();
        str_propertyDetails2 = detailsPropertyCustomer2.getText().toString();
        str_interestHBL2 = interestHBLCustomer2.getText().toString();
        str_incomeSbInterest2 = incomeSbInterestCustomer2.getText().toString();
        str_accountHolderName2 = accountHolderNameCustomer2.getText().toString();
        str_ifscCode2 = IFSCCodeCustomer2.getText().toString();
        str_accountNumber2 = accountHolderNumberCustomer2.getText().toString();
        //str_reEnterAccountNumber2 = accountHolderReEnterNumberCustomer2.getText().toString();
        str_cityTown2 = cityTownCustomer2.getText().toString();

        str_dividend2 = dividend2.getText().toString();
        str_taxRelevant = taxRelevant2.getText().toString();

        str_category2 = autoCompleteCategoryITR2.getText().toString();
//        str_district2 = autoCompleteSelectDistrictITR2.getText().toString();
        str_state2 = autoCompleteSelectStateITR2.getText().toString();
        str_financialYear2 = autoCompleteFinancialYearITR2.getText().toString();
        str_bankName2 = autoCompleteSelectBankNameITR2.getText().toString();

        str_houseType2 = autoCompleteSelectHouseTypeITR2.getText().toString();


    }

    @Override
    public void onBackPressed() {
//        counter++;
//        if (counter == 1){
//            Snackbar.make(autoCompleteCategoryITR2,"Do you really want back",Snackbar.LENGTH_LONG).show();
//        }else if (counter == 2){
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            finish();
        //}
    }

    public void category(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ITR_2_Activity.this,
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.selectCategory));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteCategoryITR2.setAdapter(arrayAdapter);
    }
    public void state(){
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(ITR_2_Activity.this,
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.selectState));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteSelectStateITR2.setAdapter(arrayAdapter1);
    }
    public void bank() {

        //String BANK_URL = "https://purpuligo.com/carsm/index.php/User_mobile/bank_master_list";
        String JSON_URL_BANK = Url.BANK_URL;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_BANK, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            //JSONArray jsonArray = response.getJSONArray("");
                            for (int i=0;i<response.length();i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String bankName = jsonObject.optString("bank_name");
                                bankList.add(bankName);
                                bankAdapter = new ArrayAdapter<>(ITR_2_Activity.this
                                        ,android.R.layout.simple_spinner_item,bankList);
                                bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                autoCompleteSelectBankNameITR2.setAdapter(bankAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError","VolleyError: "+error.toString());

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ITR_2_Activity.this);
        requestQueue.add(jsonObjectRequest);
//        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(ITR_2_Activity.this,
//                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.selectBankName));
//        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        autoCompleteSelectBankNameITR2.setAdapter(arrayAdapter3);
    }
    public void financialYear(){
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(ITR_2_Activity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.selectFinancialYear));
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteFinancialYearITR2.setAdapter(arrayAdapter4);
    }
    public void houseType(){
        ArrayAdapter<String> houseTypeArrayAdapter = new ArrayAdapter<String>(ITR_2_Activity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.selectHouseType));
        houseTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteSelectHouseTypeITR2.setAdapter(houseTypeArrayAdapter);
    }


}