package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Form16UploadPaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    private TextView upload_count, show_pdf, message_success ;
    private Button  imageAndPdfUpload, payment;
    private CardView imageClick, capture_pdf;
    private ImageView imageView;
    private String taxPairProfile_id, types, orderID, createdAt, orderAmount, orderCurrency, str_encodedImage;
    private String userID, userTelephone, userEmail, userName;
    private String currentPhotoPath, encodedPDF;
    private NetworkState networkState;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PDF = 2;
    static int number_of_click = 0;
    static int number;
    private int count = 0;


    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form16_upload_payment);
        upload_count = (TextView) findViewById(R.id.image_upload_count);
        imageClick = (CardView) findViewById(R.id.camera_click);
        capture_pdf = (CardView) findViewById(R.id.capture_pdf);
        imageView = (ImageView) findViewById(R.id.image_show);
        imageAndPdfUpload = (Button) findViewById(R.id.upload_btn);
        payment = (Button) findViewById(R.id.payment_btn);
        show_pdf = (TextView) findViewById(R.id.show_pdf);

        message_success = (TextView) findViewById(R.id.message_success);

        networkState = new NetworkState();

        //data from session
        userSessionManager = new UserSessionManager(getApplicationContext());
        userTelephone = userSessionManager.getPhoneNumber();
        userID = userSessionManager.getUserID();
        userEmail = userSessionManager.getEmailId();
        userName = userSessionManager.getUserName();

        //getDateIn nextPage
        taxPairProfile_id = getIntent().getStringExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_TAX_PAIR_ID);
        orderID = getIntent().getStringExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_ID);
        createdAt = getIntent().getStringExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_CREATE_AT);
        orderAmount = getIntent().getStringExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_AMOUNT);
        orderCurrency = getIntent().getStringExtra(Constants.GET_ITR_SUBMIT_DETAILS.KEY_ORDER_CURRENCY);
        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        //image capture
        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = 1;

                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(Form16UploadPaymentActivity.this,
                            "com.purpuligo.taxation.Activity.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //pdf capture
        capture_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number =2;
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf file"), REQUEST_PDF);
            }
        });
        imageAndPdfUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkState.isNetworkAvailable(Form16UploadPaymentActivity.this)) {
                    if(number == 1){
                        ImageUpload();
                        imageView.setVisibility(View.INVISIBLE);
                        capture_pdf.setEnabled(true);
                        capture_pdf.setCardBackgroundColor(Color.WHITE);
                        imageAndPdfUpload.setVisibility(View.INVISIBLE);
                    } else if(number == 2){
                        PdfUpload();
                        show_pdf.setVisibility(View.INVISIBLE);
                        imageClick.setEnabled(true);
                        imageClick.setCardBackgroundColor(Color.WHITE);
                        imageAndPdfUpload.setVisibility(View.INVISIBLE);

                    }else{
                        Toast.makeText(Form16UploadPaymentActivity.this,"Field Is Empty",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Form16UploadPaymentActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkState.isNetworkAvailable(Form16UploadPaymentActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Form16UploadPaymentActivity.this);
                    builder.setTitle("Confirmation")
                            .setIcon(R.drawable.alert_icon)
                            .setMessage(R.string.alertMsgPayment)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startPayment();
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
                    Toast.makeText(Form16UploadPaymentActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(bitmap);
            imageStore(bitmap);
            upload_count.setText("");
            message_success.setText("");
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
                show_pdf.setText(pathData.getLastPathSegment());
                imageClick.setEnabled(false);
                imageClick.setCardBackgroundColor(Color.GRAY);

                InputStream inputStream = Form16UploadPaymentActivity.this.getContentResolver().openInputStream(pathData);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                upload_count.setText("");
                message_success.setText("");
                show_pdf.setVisibility(View.VISIBLE);
                imageAndPdfUpload.setVisibility(View.VISIBLE);
                Log.d("encodePdf","EncodePdf: "+ encodedPDF);
                //show_pdf.setText(data.getDataString()
                 //       .substring(data.getDataString().lastIndexOf("/") + 1));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //base64
    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStreamObject);

        byte[] imageBytes = byteArrayOutputStreamObject.toByteArray();
        str_encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void ImageUpload() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        progressDialog.show();
        //https://purpuligo.com/carsm/index.php/User_mobile/tax_itr_image_data_submit
        String JSON_URL_SUBMIT = Url.IMAGE_URL;
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.KEY_TAX_PAIR_ID, taxPairProfile_id);
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
                    upload_count.setText("From-16 Document has been Uploaded Successfully");
                    message_success.setText(R.string.uploadPicture);
                    payment.setVisibility(View.VISIBLE);

                    Log.d("response", "response: " + jsonParams);
                    Toast.makeText(Form16UploadPaymentActivity.this, "Page " + number_of_click + " Uploaded", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(Form16UploadPaymentActivity.this);
        requestQueue.add(postRequest);

    }
    public void PdfUpload() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        progressDialog.show();
        //https://purpuligo.com/carsm/index.php/User_mobile/tax_itr_image_data_submit2
        String JSON_URL_SUBMIT = Url.PDF_URL;
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put(Constants.IMAGE_DATA_SUBMIT.KEY_TAX_PAIR_ID, taxPairProfile_id);
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
                    upload_count.setText("From-16 Document has been Uploaded Successfully");
                    //upload_count.setText("From-16 Pdf has been Successfully Uploaded");
                    message_success.setText(R.string.uploadPdf);
                    payment.setVisibility(View.VISIBLE);

                    Log.d("response", "response: " + jsonParams);
                    Toast.makeText(Form16UploadPaymentActivity.this, "PDF Uploaded", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(Form16UploadPaymentActivity.this);
        requestQueue.add(postRequest);

    }

    public void startPayment() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        Checkout checkout = new Checkout();
        //checkout.setKeyID("rzp_test_iujdpTBMUKyyns"); //testing
        checkout.setKeyID("rzp_live_GcpW0uX60OtWbR"); //production
        checkout.setImage(R.drawable.taxception_logo);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Taxception");
            options.put("description", createdAt);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", orderID);//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", orderCurrency);
            options.put("amount", orderAmount); //500*100
            options.put("prefill.email", userEmail);
            options.put("prefill.contact", userTelephone);

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 5);
            options.put("retry", retryObj);

            checkout.open(Form16UploadPaymentActivity.this, options);

            progressDialog.dismiss();
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        //String ORDER_CON =  "https://purpuligo.com/carsm/index.php/RPay/tax_payment_details_update";
        String JSON_URL_ORDER_CONFIRM = Url.ORDER_CONFIRM;

        Map<String, String> jsonParams2 = new HashMap<String, String>();
        jsonParams2.put("razorpay_order_id", orderID);
        jsonParams2.put("p_tax_tr_id", taxPairProfile_id);
        jsonParams2.put("razorpay_payment_id", razorpayPaymentID);
        JSONObject jasonParam3 = new JSONObject(jsonParams2);
        Log.d("jsonParams2", "jasonParam2 : " + jasonParam3.toString());

        JsonObjectRequest postRequest3 = new JsonObjectRequest(Request.Method.POST, JSON_URL_ORDER_CONFIRM,
                jasonParam3, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("response", "response : " + response.toString());
//                Toast.makeText(ITR_1_Activity.this,"Successful Payment Id : "+razorpayPaymentID, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);

                Log.e("payment success", "Successful Payment Id :" + razorpayPaymentID);
                Log.e("payment success2", "Successful Payment Id2 :" + paymentData);
                Intent intent = new Intent(getApplicationContext(), PaymentDisplayActivity.class);
                intent.putExtra("taxPairProfile_id", taxPairProfile_id);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("ErrorSendPaymentDetails", "ErrorSendPaymentDetails : " + error);
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
        RequestQueue requestQueue3 = Volley.newRequestQueue(Form16UploadPaymentActivity.this);
        requestQueue3.add(postRequest3);

    }

    @Override
    public void onPaymentError(int i, String response, PaymentData paymentData) {
        //Toast.makeText(ITR_1_Activity.this,"Failed and Cause is : "+response, Toast.LENGTH_SHORT).show();
        Log.e("Field payment", "Failed and Cause is :" + i);
        Log.e("Field payment1", "Failed and Cause is1 :" + response);
        Log.e("Field payment2", "Failed and Cause is2 :" + paymentData);
        //ALERT MSG
        AlertDialog.Builder builder = new AlertDialog.Builder(Form16UploadPaymentActivity.this);
        builder.setMessage("Payment Failed Cause is : " + response)
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startPayment();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count == 1) {
            Snackbar.make(payment, "Do you really want back", Snackbar.LENGTH_LONG).show();
        } else if (count == 2) {
            Intent intent = new Intent(this, MainActivity.class);
            number_of_click = 0;
            this.startActivity(intent);
            finish();
        }
    }
}