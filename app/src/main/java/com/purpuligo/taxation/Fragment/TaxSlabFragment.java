package com.purpuligo.taxation.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purpuligo.taxation.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaxSlabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaxSlabFragment extends Fragment {

    private TextView profile_textView, moreInformation, sourceFrom;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaxSlabFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static TaxSlabFragment newInstance(String param1, String param2) {
        TaxSlabFragment fragment = new TaxSlabFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tax_slab, container, false);

        profile_textView = (TextView) view.findViewById(R.id.profile_textView);
        moreInformation = (TextView) view.findViewById(R.id.moreInformation);
        sourceFrom = (TextView) view.findViewById(R.id.source);

        String htmlAsString = getString(R.string.profile_textView);
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        profile_textView.setText(htmlAsSpanned);

        moreInformation.setMovementMethod(LinkMovementMethod.getInstance());
        sourceFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUri("https://www.incometax.gov.in");
            }
        });

        return view;
    }
    private void gotoUri(String s){
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}
