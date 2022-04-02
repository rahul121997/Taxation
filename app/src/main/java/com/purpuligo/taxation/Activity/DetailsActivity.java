package com.purpuligo.taxation.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.purpuligo.taxation.Adapter.CommunicationAdapter;
import com.purpuligo.taxation.Constants.Constants;
import com.purpuligo.taxation.Fragment.HomeFragment;
import com.purpuligo.taxation.Fragment.StatusFragment;
import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.Global.Url;
import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.Model.CommunicationModel;
import com.purpuligo.taxation.Model.DataModel;
import com.purpuligo.taxation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private String transaction_id, name_get, currentPhotoPath, userID;

    private TextView name_textView, dob_textView,transaction_id_textView, financial_year_textView, income_textView, house_property_details_textView,
            interest_house_property_textView, bank_name_textView, account_holder_name_textView, bank_ifsc_code_textView,
            account_no_textView, category_textView, type_textView, submit_date_textView, address_textView, pin_no_textView,
            cityTown_textView, pan_no_textView, aadhaar_no_textView, house_type_textView, dividend_textview, taxRelevant_textView, show_pdf_textView;

    private EditText description_text;
    private Button  submit_btn, imageAndPdfUpload;
    private CardView imageClick, capture_pdf;
    private ImageView imageView;
    private NetworkState networkState;
    private String description_str, str_encodedImage, encodedPDF;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PDF = 2;
    static int number_of_click = 0;
    private int counter = 0;
    static int number;

    ImageView backImgBtn;

    ListView listViewCommunication;
    CommunicationAdapter communicationAdapter;
    CommunicationModel communicationModel;
    public static List<CommunicationModel> dataCommunicationArrayList = new ArrayList<>();
    String date, details, name, currentDate, communicationFlag;

    CardView cardViewSubmit, cardViewComm;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name_textView = (TextView) findViewById(R.id.name_textView);
        dob_textView = (TextView) findViewById(R.id.dob_textView);
        transaction_id_textView = (TextView) findViewById(R.id.transaction_id_textView);
        financial_year_textView = (TextView) findViewById(R.id.financial_year_textView);
        income_textView = (TextView) findViewById(R.id.income_textView);
        house_property_details_textView = (TextView) findViewById(R.id.house_property_details_textView);
        interest_house_property_textView = (TextView) findViewById(R.id.interest_house_property_textView);
        bank_name_textView = (TextView) findViewById(R.id.bank_name_textView);
        account_holder_name_textView = (TextView) findViewById(R.id.account_holder_name_textView);
        bank_ifsc_code_textView = (TextView) findViewById(R.id.bank_ifsc_code_textView);
        account_no_textView = (TextView) findViewById(R.id.account_no_textView);
        category_textView = (TextView) findViewById(R.id.category_textView);
        type_textView = (TextView) findViewById(R.id.type_textView);
        submit_date_textView = (TextView) findViewById(R.id.submit_date_textView);
        //email_id_textView = (TextView) findViewById(R.id.email_id_textView);
        address_textView = (TextView) findViewById(R.id.address_textView);
        pin_no_textView = (TextView) findViewById(R.id.pin_no_textView);
        cityTown_textView = (TextView) findViewById(R.id.cityTown_textView);
        pan_no_textView = (TextView) findViewById(R.id.pan_no_textView);
        aadhaar_no_textView = (TextView) findViewById(R.id.aadhaar_no_textView);
        //transaction_id_textView = (TextView) findViewById(R.id.transaction_id_textView);
        house_type_textView = (TextView) findViewById(R.id.house_type_textView);
        dividend_textview = (TextView) findViewById(R.id.dividend_textView);
        taxRelevant_textView = (TextView) findViewById(R.id.taxRelevant_textView);

        description_text = (EditText) findViewById(R.id.description_text);
        imageClick = (CardView) findViewById(R.id.camera_btn_Description);
        capture_pdf = (CardView) findViewById(R.id.capture_pdf_Description);
        show_pdf_textView = (TextView) findViewById(R.id.show_pdf_Description);
        imageView = (ImageView) findViewById(R.id.camera_image_Description);
        imageAndPdfUpload = (Button) findViewById(R.id.upload_btn_Description);
        submit_btn = (Button) findViewById(R.id.submit_btn_Description);

        backImgBtn = (ImageView) findViewById(R.id.back_img_btn_return);

        cardViewSubmit = (CardView) findViewById(R.id.cardViewSubmit);
        cardViewComm = (CardView) findViewById(R.id.cardViewComm);

        networkState = new NetworkState();

        listViewCommunication = (ListView) findViewById(R.id.listViewCommunication);
        communicationAdapter = new CommunicationAdapter(getApplicationContext(),dataCommunicationArrayList);
        listViewCommunication.setAdapter(communicationAdapter);

        userSessionManager = new UserSessionManager(getApplicationContext());
        userID = userSessionManager.getUserID();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = dateFormat.format(date);

        transaction_id = getIntent().getStringExtra("tax_transaction_id");
        name_get = getIntent().getStringExtra("get_name");

        //position = getIntent().getExtras().getInt("position");

        retrieveDetailsData();
        communication();

//        if(status_flags.equals("1")) {
//            cardViewSubmit.setVisibility(View.VISIBLE);
//        } else {
//            cardViewSubmit.setVisibility(View.INVISIBLE);
//        }

        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = 1;

                String fileName = "Photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile  =  File.createTempFile(fileName,".jpg",storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(DetailsActivity.this,
                            "com.purpuligo.taxation.Activity.fileprovider", imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        capture_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 2;
               Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf file"), REQUEST_PDF);
            }
        });

        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        imageAndPdfUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkState.isNetworkAvailable(DetailsActivity.this)) {
                    if(number == 1){
                        ImageUpload();
                        imageView.setVisibility(View.INVISIBLE);
                        capture_pdf.setEnabled(true);
                        capture_pdf.setCardBackgroundColor(Color.WHITE);
                        imageAndPdfUpload.setVisibility(View.INVISIBLE);
                    } else if(number == 2){
                        PdfUpload();
                        show_pdf_textView.setVisibility(View.INVISIBLE);
                        imageClick.setEnabled(true);
                        imageClick.setCardBackgroundColor(Color.WHITE);
                        imageAndPdfUpload.setVisibility(View.INVISIBLE);

                    }else{
                        Toast.makeText(DetailsActivity.this,"Field Is Empty",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(DetailsActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        submit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                communicationDataSubmit();
//                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //camera
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(bitmap);
            imageStore(bitmap);
            imageView.setVisibility(View.VISIBLE);
            capture_pdf.setEnabled(false);
            capture_pdf.setCardBackgroundColor(Color.GRAY);
            imageAndPdfUpload.setVisibility(View.VISIBLE);
        }
        //pdf
        if (requestCode == REQUEST_PDF && resultCode == RESULT_OK){
            //show_pdf.setText(path.getLastPathSegment());
            try {
                Uri pathData = data.getData();
                show_pdf_textView.setText(pathData.getLastPathSegment());
                imageClick.setEnabled(false);
                imageClick.setCardBackgroundColor(Color.GRAY);

                InputStream inputStream = DetailsActivity.this.getContentResolver().openInputStream(pathData);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                show_pdf_textView.setVisibility(View.VISIBLE);
                imageAndPdfUpload.setVisibility(View.VISIBLE);
                Log.d("encodePdf","EncodePdf: "+ encodedPDF);
                //show_pdf.setText(data.getDataString()
                //       .substring(data.getDataString().lastIndexOf("/") + 1));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStreamObject);

        byte[] imageBytes = byteArrayOutputStreamObject.toByteArray();

        str_encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void retrieveDetailsData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        progressDialog.show();


        //String TEXT_PAIR_DETAILS = "https://purpuligo.com/carsm/index.php/User_mobile/tax_pair_list_details_view";
        String JSON_URL_TEXT_PAIR = Url.TEXT_PAIR_DETAILS;

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("tax_transaction_id", transaction_id);

        JSONObject jsonParam = new JSONObject(jsonParams);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL_TEXT_PAIR,
                jsonParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    Log.d("data", "Data from server details" + response);

                    name_textView.setText(name_get);
                    dob_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_DOB));
                    transaction_id_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_TRANSACTION_ID));
                    financial_year_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_FINANCIAL_YEAR));
                    income_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_INCOME));
                    house_property_details_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_HOUSE_PROPERTY));
                    interest_house_property_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_HOUSE_INTEREST));
                    bank_name_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_BANK));
                    account_holder_name_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_ACC_NAME));
                    bank_ifsc_code_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_IFCS));
                    account_no_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_ACC_NUMBER));
                    category_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_CATEGORY));
                    type_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_TYPE));
                    submit_date_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_DATE));
                    //email_id_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_EMAIL_ID));
                    address_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_ADDRESS));
                    pin_no_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_PIN_NO));
                    cityTown_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_DIST));
                    pan_no_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_PAN_NUMBER));
                    aadhaar_no_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_AADHAAR));
                    dividend_textview.setText(response.getString(Constants.DETAILS_SHOW.KEY_DIVIDEND));
                    taxRelevant_textView.setText(response.getString(Constants.DETAILS_SHOW.KEY_TEX_RELEVANT));

                    communicationFlag = response.getString("communication_flag");


                    String house_type = response.getString(Constants.DETAILS_SHOW.KEY_HOUSE_TYPE);
                    if(house_type.equals("0")){ house_type_textView.setText("Rent"); }

                    if(house_type.equals("1")) { house_type_textView.setText("Self(Own)"); }


                    if(communicationFlag.equals("0")) {
                        cardViewSubmit.setVisibility(View.INVISIBLE);
                    }
                    if(communicationFlag.equals("1")){
                        cardViewSubmit.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("Error", "ErrorDetails" + error.getMessage().toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivity.this);
        requestQueue.add(postRequest);
    }

    public void communication() {

        //String COMMUNICATION_URL = "https://purpuligo.com/carsm/index.php/User_mobile/tax_admin_user_comunication_list";
        String JSON_URL_COMMUNICATION_LIST = Url.COMMUNICATION_URL_LIST;

        HashMap<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("tax_transaction_id",transaction_id);
        JSONArray jsonArray = new JSONArray(Arrays.asList(jsonParams));
        //Log.d("mJSONArray", "mJSONArray: " + jsonArray);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, JSON_URL_COMMUNICATION_LIST,
                jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dataCommunicationArrayList.clear();
                cardViewComm.setVisibility(View.VISIBLE);

                for (int i=0;i<response.length();i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        date = jsonObject.getString("update_date");
                        // date = dateFormat.format(new Date());
                        name = jsonObject.getString("name");
                        details = jsonObject.getString("text_description");
                        //status_flags = jsonObject.getString("status");


                        communicationModel = new CommunicationModel(date, name, details);
                        dataCommunicationArrayList.add(communicationModel);
                        communicationAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cardViewComm.setVisibility(View.INVISIBLE);
                Log.d("Error", "ErrorListView: " + error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    public void submitBt(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setTitle("Confirmation")
                .setIcon(R.drawable.alert_icon)
                .setMessage("Are You Sure you want to Submit ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        communicationDataSubmit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //description_text.getText().clear();
    }


    private void communicationDataSubmit() {

        description_str = description_text.getText().toString();

        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        progressDialog.show();

        //String COMMUNICATION_SUBMIT = "https://purpuligo.com/carsm/index.php/User_mobile/admin_user_comunication_data_submit";
        String JSON_URL_COMMUNICATION_SUBMIT = Url.COMMUNICATION_SUBMIT;

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("tax_transaction_id", transaction_id);
        jsonParams.put("user_id", userID);
        jsonParams.put("text_description",description_str);
        jsonParams.put("update_date",currentDate);
        jsonParams.put("img_path","");
        jsonParams.put("key_type","1");

        JSONObject jsonObject = new JSONObject(jsonParams);

        Log.d("updateSubmit", "updateSubmit: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL_COMMUNICATION_SUBMIT,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("Error", "CommunicationSubmit: " + error);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(DetailsActivity.this);
        requestQueue2.add(jsonObjectRequest);
    }


    //imageUpload
    public void ImageUpload() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        progressDialog.show();
        //https://purpuligo.com/carsm/index.php/User_mobile/tax_itr_image_data_submit
        String JSON_URL_SUBMIT = Url.IMAGE_URL;
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.KEY_TAX_PAIR_ID, transaction_id);
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.KEY_USER_ID, userID);
        //jsonParams.put(Constants.IMAGE_DATA_SUBMIT.IMAGE_PATH, encodedPDF);
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.IMAGE_PATH, str_encodedImage);
        JSONObject jasonParam = new JSONObject(jsonParams);

        Log.d("image_data_submit", "image_data_submit: " + jsonParams);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL_SUBMIT,
                jasonParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    number_of_click++;
                    //upload_count.setText("From-16 Page: " + number_of_click+" has been Uploaded Successfully");
                    //message_success.setText(R.string.uploadPicture);
                    //payment.setVisibility(View.VISIBLE);

                    Log.d("response", "response: " + jsonParams);
                    Toast.makeText(DetailsActivity.this, "Document has been Uploaded Successfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("Error", "From16PaymentError: " + error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivity.this);
        requestQueue.add(postRequest);

    }
    //pdfUpload
    public void PdfUpload() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        progressDialog.show();
        //https://purpuligo.com/carsm/index.php/User_mobile/tax_itr_image_data_submit2
        String JSON_URL_SUBMIT = Url.PDF_URL;
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.KEY_TAX_PAIR_ID, transaction_id);
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.KEY_USER_ID, userID);
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.IMAGE_PATH, encodedPDF);
        //jsonParams.put(Constants.IMAGE_DATA_SUBMIT.IMAGE_PATH, currentPhotoPath);
        JSONObject jasonParam = new JSONObject(jsonParams);

        Log.d("pdf_data_submit", "pdf_data_submit: " + jsonParams);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL_SUBMIT,
                jasonParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    number_of_click++;
                    //upload_count.setText("From-16 Pdf: "+number_of_click+" has been Uploaded Successfully");
                    //message_success.setText(R.string.uploadPdf);
                    //payment.setVisibility(View.VISIBLE);

                    Log.d("response", "response: " + jsonParams);
                    Toast.makeText(DetailsActivity.this, "Document has been Uploaded Successfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("Error", "From16PaymentError: " + error);
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivity.this);
        requestQueue.add(postRequest);

    }
}