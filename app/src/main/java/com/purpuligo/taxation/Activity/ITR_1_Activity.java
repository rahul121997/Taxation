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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ITR_1_Activity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteCategoryITR1, autoCompleteSelectStateITR1,
            autoCompleteSelectHouseTypeITR1, autoCompleteSelectBankNameITR1, autoCompleteFinancialYearITR1;
    private EditText nameCustomer, dateOfBirth, addressCustomer, cityTownCustomer, pinCodeCustomer, panCardCustomer, aadhaarCustomer,
            detailsPropertyCustomer,housePropertyCustomer, incomeSbInterestCustomer, accountHolderNameCustomer,
            IFSCCodeCustomer, accountHolderNumberCustomer,dividend,taxRelevant;
    private String str_name, str_dateOfBirth, str_address, str_pinCode, str_panCard, str_aadhaar, str_propertyDetails,
            str_housePropertyDetails, str_financialYear, str_incomeSbInterest,str_accountHolderName,str_ifscCode,
            str_accountNumber, str_category, str_cityTown, str_state, str_bankName, str_houseType, str_dividend,
            str_taxRelevant, taxPairProfile_id, orderID, createdAt, orderAmount, orderCurrency,
            userID, userTelephone, userEmail, userName, price, totalPrice;
    private ImageView  backImgBtn, calenderImageBtn ;

    ArrayList<String > bankList = new ArrayList<>();
    ArrayAdapter<String> bankAdapter;
    private int counter = 0;
    private NetworkState networkState;

    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itr1);

        //Checkout checkout = new Checkout();
        //checkout.setKeyID("rzp_test_YPfvy77ZFQ3LDt");
        Checkout.preload(getApplicationContext());

        autoCompleteCategoryITR1 = (AutoCompleteTextView) findViewById(R.id.selectCategoryITR1);
        autoCompleteSelectStateITR1 = (AutoCompleteTextView) findViewById(R.id.selectStateITR1);
     //   autoCompleteSelectDistrictITR1 = (AutoCompleteTextView) findViewById(R.id.selectDistrictITR1);
        autoCompleteSelectBankNameITR1 = (AutoCompleteTextView) findViewById(R.id.selectBankNameITR1);
        autoCompleteFinancialYearITR1 = (AutoCompleteTextView) findViewById(R.id.financialYearITR1);
        autoCompleteSelectHouseTypeITR1 = (AutoCompleteTextView) findViewById(R.id.houseTypeITR1);


        nameCustomer = (EditText) findViewById(R.id.name_ITR1);
        dateOfBirth = (EditText) findViewById(R.id.dateOfBirth_ITR1);
        addressCustomer = (EditText) findViewById(R.id.address_ITR1);
        cityTownCustomer = (EditText) findViewById(R.id.cityTown_ITR1);
        pinCodeCustomer = (EditText) findViewById(R.id.pinCode_ITR1);
        panCardCustomer = (EditText) findViewById(R.id.panCardNumber_ITR1);
        aadhaarCustomer = (EditText) findViewById(R.id.aadhaarCardNumber_ITR1);
        detailsPropertyCustomer = (EditText) findViewById(R.id.detailsPropertyITR1);
        dividend = (EditText) findViewById(R.id.dividendITR1);
        taxRelevant = (EditText) findViewById(R.id.taxRelevantITR1);
        housePropertyCustomer = (EditText) findViewById(R.id.house_property_infoITR1);
        incomeSbInterestCustomer = (EditText) findViewById(R.id.incomeSbInterestITR1);
        accountHolderNameCustomer = (EditText) findViewById(R.id.account_holder_name_ITR1);
        IFSCCodeCustomer = (EditText) findViewById(R.id.account_IFSC_code_ITR1);
        accountHolderNumberCustomer = (EditText) findViewById(R.id.account_holder_number_ITR1);
        calenderImageBtn = (ImageView) findViewById(R.id.calenderImageBtn);
        //accountHolderReEnterNumberCustomer = (EditText) findViewById(R.id.account_holder_reEnter_number_ITR1);

        backImgBtn = (ImageView) findViewById(R.id.back_img_btn);
        networkState = new NetworkState();

        //calender
        calenderImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender();
            }
        });
        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                   calender();
                }
            }
        });


        //getData next page
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
                Intent intent = new Intent(ITR_1_Activity.this, MainActivity.class);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ITR_1_Activity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                String str_month = null;
                String str_day = null;
                month = month+1;
                if((month+"").length()<2){
                    str_month = "0"+month;
                }else {
                    str_month = ""+month;
                }
                if ((day+"").length()<2){
                    str_day = "0"+day;
                }else {
                    str_day = ""+day;
                }
                String date = str_day+"-"+str_month+"-"+year;
                dateOfBirth.setText(date);
                //dateOfBirth.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()))
            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    //button Click
    public void itr1_submitBt(View view) {
        stringAll();

        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        //validation
        if(str_category.length() == 0) {
            autoCompleteCategoryITR1.requestFocus();
            autoCompleteCategoryITR1.setError("Chose any Category");
        }else if (str_name.length() == 0) {
            nameCustomer.requestFocus();
            nameCustomer.setError("Enter Your Name");
        }else if (str_dateOfBirth.length() == 0){
            dateOfBirth.requestFocus();
            dateOfBirth.setError("Enter Your DateOfBirth");
        } else if (str_address.length() == 0) {
            addressCustomer.requestFocus();
            addressCustomer.setError("Enter Your Address");
        } else if (str_state.length() == 0) {
            autoCompleteSelectStateITR1.requestFocus();
            autoCompleteSelectStateITR1.setError("Chose Your State");
        }else if(str_cityTown.length() == 0){
            cityTownCustomer.requestFocus();
            cityTownCustomer.setError("Enter Your City/Town");
        } else if (str_pinCode.length() == 0) {
            pinCodeCustomer.requestFocus();
            pinCodeCustomer.setError("Enter Your PinCode");
        } else if (str_pinCode.length() <= 5) {
            pinCodeCustomer.requestFocus();
            pinCodeCustomer.setError("Enter Valid PinCode");
        } else if (str_panCard.length() == 0) {
            panCardCustomer.requestFocus();
            panCardCustomer.setError("Enter Your PanCard Number");
        } else if (str_panCard.length() <= 9) {
            panCardCustomer.requestFocus();
            panCardCustomer.setError("Enter Valid PanCard Number");
        } else if (str_aadhaar.length() == 0) {
            aadhaarCustomer.requestFocus();
            aadhaarCustomer.setError("Enter Your Aadhaar Card Number");
        } else if (str_aadhaar.length() <= 11) {
            aadhaarCustomer.requestFocus();
            aadhaarCustomer.setError("Enter Your Aadhaar Card Number");
        } else if (str_financialYear.length() == 0) {
            autoCompleteFinancialYearITR1.requestFocus();
            autoCompleteFinancialYearITR1.setError("Chose Financial Year");
        } else if (str_houseType.length() == 0) {
            autoCompleteSelectHouseTypeITR1.requestFocus();
            autoCompleteSelectHouseTypeITR1.setError("Chose House Type");
        } else if (str_propertyDetails.length() == 0) {
            detailsPropertyCustomer.requestFocus();
            detailsPropertyCustomer.setError("Enter Your Property");
        } else if (str_housePropertyDetails.length() == 0) {
            housePropertyCustomer.requestFocus();
            housePropertyCustomer.setError("Enter Your House Property");
        } else if (str_incomeSbInterest.length() == 0) {
            incomeSbInterestCustomer.requestFocus();
            incomeSbInterestCustomer.setError("Enter Your Sb Interest");
        }else if (str_dividend.length() == 0) {
            dividend.requestFocus();
            dividend.setError("Enter Dividend");
        }else if (str_taxRelevant.length() == 0) {
            taxRelevant.requestFocus();
            taxRelevant.setError("Enter Tax Relevant");
        } else if (str_bankName.length() == 0) {
            autoCompleteSelectBankNameITR1.requestFocus();
            autoCompleteSelectBankNameITR1.setError("Chose Your Bank");
        } else if (str_accountHolderName.length() == 0) {
            accountHolderNameCustomer.requestFocus();
            accountHolderNameCustomer.setError("Enter Account Holder Name");
        } else if (str_ifscCode.length() == 0) {
            IFSCCodeCustomer.requestFocus();
            IFSCCodeCustomer.setError("Enter Your IFSC Code");
        } else if (str_accountNumber.length() == 0) {
            accountHolderNumberCustomer.requestFocus();
            accountHolderNumberCustomer.setError("Enter Account Number");
        } else if (str_accountNumber.length() <= 7) {
            accountHolderNumberCustomer.requestFocus();
            accountHolderNumberCustomer.setError("Enter Valid Account Number");
//        } else if (str_reEnterAccountNumber.length() == 0) {
//            accountHolderReEnterNumberCustomer.requestFocus();
//            accountHolderReEnterNumberCustomer.setError("Enter Account Number");
//        } else if (str_reEnterAccountNumber.length() <= 7) {
//            accountHolderReEnterNumberCustomer.requestFocus();
//            accountHolderReEnterNumberCustomer.setError("Enter Valid Account Number");
//        } else if (!str_accountNumber.equals(str_reEnterAccountNumber)) {
//            accountHolderReEnterNumberCustomer.requestFocus();
//            accountHolderReEnterNumberCustomer.setError("Account Number is Not Match");
        } else {
            if (networkState.isNetworkAvailable(ITR_1_Activity.this)) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ITR_1_Activity.this);
                builder.setTitle("Confirmation")
                        .setIcon(R.drawable.alert_icon)
                        .setMessage(R.string.alertMsgITR1)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.show();

                                //String DATA_SUBMIT_URL_ITR1 =  "https://purpuligo.com/carsm/index.php/User_mobile/json_itr_profile_data_submit";
                                String JSON_URL_SUBMIT = Url.DATA_SUBMIT_URL_ITR;

                                Map<String, String> jsonParams = new HashMap<String, String>();

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_USER_ID, userID);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_TYPE, "ITR1");
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_CATEGORY, str_category);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_NAME, str_name);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_DOB,str_dateOfBirth);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_ADDRESS, str_address);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_PIN_NO, str_pinCode);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_STATE, str_state);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_CITY_TOWN, str_cityTown);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_PAN_NO, str_panCard);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_AADHAAR_NO, str_aadhaar);

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_HOUSE_PROPERTY, str_propertyDetails);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_INTEREST_HOUSE_PROPERTY, str_housePropertyDetails);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_INCOME, str_incomeSbInterest);

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_BANK_NAME, str_bankName);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_ACCOUNT_HOLDER_NAME, str_accountHolderName);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_IFSC_CODE, str_ifscCode);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_ACCOUNT_NO, str_accountNumber);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_FINANCIAL_YEAR, str_financialYear);

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_DIVIDEND,str_dividend);
                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_TAX_RELEVANT,str_taxRelevant);

                                //houseType convert
                                String int_houseType;
                                if(str_houseType.equals("Rent")){
                                    int_houseType="0";
                                    jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_HOUSE_TYPE, int_houseType);
                                }
                                if(str_houseType.equals("Self(Own)")) {
                                    int_houseType="1";
                                    jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_HOUSE_TYPE, int_houseType);
                                }

                                jsonParams.put(Constants.ITR_SUBMIT_DETAILS.KEY_IMAGE, "");

                                JSONObject jasonParam = new JSONObject(jsonParams);

                                Log.d("json", "jsonSubmit: " + jasonParam);

                                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL_SUBMIT,
                                        jasonParam, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                           // progressDialog.dismiss();
                                            Log.d("data", "Data from server ITR1 Submit" + response.toString());

                                            //getData
                                            taxPairProfile_id = response.getString(Constants.GET_DATA_ITR_SUBMIT.KEY_TAX_PAIR_PROFILE_ID);

                                            //types = response.getString("type");
                                            //api -response- {"tax_pair_profile_id":"1"}
                                            Log.d("tax_id", "tax_id = " + taxPairProfile_id);
                                            //Toast.makeText(ITR_1_Activity.this, "Submit Successfully", Toast.LENGTH_SHORT).show();

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
                                                                    String ITR1 = jsonObject.optString("type");
                                                                    if (ITR1.equals("ITR1") == true) {
                                                                        price = jsonObject.optString("price");
                                                                        Log.d("Price1", "Price1: " + price);
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
                                                                    jsonParams2.put(Constants.ORDER_ID_GEN.KEY_TYPE, "ITR1");

                                                                    JSONObject jasonParam2 = new JSONObject(jsonParams2);
                                                                    Log.d("json", "json_ORDER_ID" + jasonParam2);

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

                                                                                Intent intent = new Intent(getApplicationContext(), Form16UploadPaymentActivity.class);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_TAX_PAIR_ID, taxPairProfile_id);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_ID, orderID);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_CREATE_AT, createdAt);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_AMOUNT, orderAmount);
                                                                                intent.putExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_CURRENCY, orderCurrency);
                                                                                startActivity(intent);
                                                                                finish();
                                                                               // startPayment();
//

                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            progressDialog.dismiss();
                                                                            Log.d("Error", "ErrorItr1Submit" + error);
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
                                                                    RequestQueue requestQueue2 = Volley.newRequestQueue(ITR_1_Activity.this);
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(ITR_1_Activity.this);
                                            requestQueue.add(jsonObjectRequest);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Log.d("Error", "ErrorSubmitITR1: " + error);
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
                                RequestQueue requestQueue = Volley.newRequestQueue(ITR_1_Activity.this);
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
                Toast.makeText(ITR_1_Activity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stringAll(){
        str_name = nameCustomer.getText().toString();
        str_dateOfBirth = dateOfBirth.getText().toString();
        str_address = addressCustomer.getText().toString();
        str_pinCode = pinCodeCustomer.getText().toString().trim();
        str_panCard = panCardCustomer.getText().toString().trim();
        str_aadhaar = aadhaarCustomer.getText().toString().trim();
        str_propertyDetails = detailsPropertyCustomer.getText().toString();
        str_housePropertyDetails = housePropertyCustomer.getText().toString();
        str_incomeSbInterest = incomeSbInterestCustomer.getText().toString();
        str_accountHolderName = accountHolderNameCustomer.getText().toString();
        str_ifscCode = IFSCCodeCustomer.getText().toString();
        str_accountNumber = accountHolderNumberCustomer.getText().toString();
        //str_reEnterAccountNumber = accountHolderReEnterNumberCustomer.getText().toString();
        str_cityTown = cityTownCustomer.getText().toString();

        str_dividend = dividend.getText().toString();
        str_taxRelevant = taxRelevant.getText().toString();

        str_category = autoCompleteCategoryITR1.getText().toString();
    //    str_district = autoCompleteSelectDistrictITR1.getText().toString();
        str_state = autoCompleteSelectStateITR1.getText().toString();
        str_bankName = autoCompleteSelectBankNameITR1.getText().toString();
        str_financialYear = autoCompleteFinancialYearITR1.getText().toString();
        str_houseType = autoCompleteSelectHouseTypeITR1.getText().toString();

    }


    @Override
    public void onBackPressed() {
//        counter++;
//        if (counter == 1){
//            Snackbar.make(autoCompleteCategoryITR1,"Do you really want back",Snackbar.LENGTH_LONG).show();
//        }else if (counter == 2){
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            finish();
      //  }
    }

    public void category(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ITR_1_Activity.this,
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.selectCategory));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteCategoryITR1.setAdapter(arrayAdapter);
    }
    public void state(){
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(ITR_1_Activity.this,
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.selectState));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteSelectStateITR1.setAdapter(arrayAdapter1);
    }
    public void bank(){

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
                                bankAdapter = new ArrayAdapter<>(ITR_1_Activity.this
                                        ,android.R.layout.simple_spinner_item,bankList);
                                bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                autoCompleteSelectBankNameITR1.setAdapter(bankAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ITR_1_Activity.this);
        requestQueue.add(jsonObjectRequest);

//        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(ITR_1_Activity.this,
//                android.R.layout.simple_spinner_item,getResources().getStringArray());
//        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        autoCompleteSelectBankNameITR1.setAdapter(arrayAdapter3);
    }
    public void financialYear(){
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(ITR_1_Activity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.selectFinancialYear));
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteFinancialYearITR1.setAdapter(arrayAdapter4);
    }
    public void houseType(){
        ArrayAdapter<String> houseTypeArrayAdapter = new ArrayAdapter<String>(ITR_1_Activity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.selectHouseType));
        houseTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteSelectHouseTypeITR1.setAdapter(houseTypeArrayAdapter);
    }
}