package com.purpuligo.taxation.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.taxation.Activity.Form16UploadPaymentActivity;
import com.purpuligo.taxation.Activity.ITR_1_Activity;
import com.purpuligo.taxation.Activity.ITR_2_Activity;
import com.purpuligo.taxation.Activity.MainActivity;
import com.purpuligo.taxation.Activity.ProfileActivity;
import com.purpuligo.taxation.Activity.ProfileSubmitActivity;
import com.purpuligo.taxation.Activity.SendOtpActivity;
import com.purpuligo.taxation.Adapter.SliderAdapter;
import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.Global.Url;
import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView itr_1_textview,itr_2_textview, itr1_price, itr2_price, price_itr;
    private NetworkState networkState;

    private String [] text;
    private SliderView sliderView;
    private SliderAdapter adapter;
    private String priceITR1, priceITR2;
    private CardView Itr1CardView, Itr2CardView;
    private LinearLayout profileImg;
    UserSessionManager userSessionManager;
    private String slider1, slider2, slider3, details, details2, details3;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait..");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        itr_1_textview = (TextView) view.findViewById(R.id.itr_1_textview);
        itr_2_textview = (TextView) view.findViewById(R.id.itr_2_textview);
        itr1_price = (TextView) view.findViewById(R.id.itr_1_price_textView);
        itr2_price = (TextView) view.findViewById(R.id.itr_2_price_textView);
        Itr1CardView = (CardView) view.findViewById(R.id.ITR1_CardView);
        Itr2CardView = (CardView) view.findViewById(R.id.ITR2_CardView);
        price_itr = (TextView) view.findViewById(R.id.price_itr);

        profileImg = (LinearLayout) view.findViewById(R.id.profile_img);

        sliderView = view.findViewById(R.id.sliderView);

        this.networkState = new NetworkState();
        slider1 = getString(R.string.slider1);
        slider2 = getString(R.string.slider2);
        slider3 = getString(R.string.slider3);

        details = getString(R.string.details);
        details2 = getString(R.string.details2);
        details3 = getString(R.string.details3);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        //slider image
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.FILL);
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.setScrollTimeInSec(1);

        //images = new int[]{R.drawable.account_icon,R.drawable.profile_name,R.drawable.account_icon};
        if (networkState.isNetworkAvailable(getContext())) {
            text = new String[]{slider1 , slider2, slider3};
            adapter = new SliderAdapter(text);
            sliderView.setSliderAdapter(adapter);
            sliderView.startAutoCycle();

            amount();




            Itr1CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    Intent intent = new Intent(getActivity(), ITR_1_Activity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            Itr2CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    Intent intent = new Intent(getActivity(), ITR_2_Activity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            });
        }else {
            Toast.makeText(getContext(), "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void amount(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
//        progressDialog.show();

        //String AMOUNT_URL = "https://purpuligo.com/carsm/index.php/User_mobile/tax_return_type_master_list";
        String JSON_URL_AMOUNT = Url.AMOUNT_URL;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL_AMOUNT, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        progressDialog.dismiss();
                        try {
                            //JSONArray jsonArray = response.getJSONArray("");
                            for (int i=0;i<response.length();i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String price = jsonObject.optString("type");
                                if (price.equals("ITR1") == true) {
                                    priceITR1 = jsonObject.optString("price");
                                    Log.d("Price1", "Price1: " + priceITR1);
                                   // price_itr1.setText(priceITR1);
                                    itr1_price.setText(priceITR1);
                                }
                                if(price.equals("ITR2") == true){
                                    priceITR2 = jsonObject.optString("price");
                                    Log.d("Price2", "Price2: " + priceITR2);
                                    //price_itr2.setText(priceITR2);
                                    itr2_price.setText(priceITR2);
                                }
                                price_itr.setText(details+""+priceITR1+" "+details2+" "+details+" "+priceITR2+" "+details3);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}