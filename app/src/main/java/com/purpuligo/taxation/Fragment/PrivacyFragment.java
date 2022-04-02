package com.purpuligo.taxation.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purpuligo.taxation.Activity.TermsAndConditionsActivity;
import com.purpuligo.taxation.Activity.ContactUsActivity;
import com.purpuligo.taxation.Activity.PrivacyPolicyActivity;
import com.purpuligo.taxation.Activity.ReturnPolicyActivity;
import com.purpuligo.taxation.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrivacyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyFragment extends Fragment {

    CardView termsAndConditions, privacyPolicy, contactUs, returnPolicy;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrivacyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivacyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivacyFragment newInstance(String param1, String param2) {
        PrivacyFragment fragment = new PrivacyFragment();
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
        //progressDialog.dismiss();
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_privacy, container, false);

        termsAndConditions = (CardView) view.findViewById(R.id.termsAndConditions);
        privacyPolicy = (CardView) view.findViewById(R.id.privacyPolicy);
        contactUs = (CardView) view.findViewById(R.id.contactUs);
        returnPolicy = (CardView) view.findViewById(R.id.returnPolicy);

        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                startActivity(intent);
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
            }
        });
        returnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                Intent intent = new Intent(getActivity(), ReturnPolicyActivity.class);
                startActivity(intent);
            }
        });
        //progressDialog.dismiss();

        return view;
    }
}