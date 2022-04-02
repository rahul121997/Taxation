package com.purpuligo.taxation.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.taxation.Activity.DetailsActivity;
import com.purpuligo.taxation.Adapter.StatusListAdapter;
import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.Global.Url;
import com.purpuligo.taxation.Global.UserSessionManager;
import com.purpuligo.taxation.Model.DataModel;
import com.purpuligo.taxation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.CamcorderProfile.get;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;
    public static List<DataModel> dataModelArrayList = new ArrayList<>();
    StatusListAdapter statusListAdapter;
    String transaction_id, type, financialYear, date, status, name;
    DataModel dataModel;
    private String userID;
    private NetworkState networkState;
    SwipeRefreshLayout refreshLayout;

    UserSessionManager userSessionManager;


    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
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
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        this.networkState = new NetworkState();

        if (networkState.isNetworkAvailable(getContext())) {



            statusListAdapter = new StatusListAdapter(getActivity(), dataModelArrayList);
            listView.setAdapter(statusListAdapter);

            userSessionManager = new UserSessionManager(getContext());
            userID = userSessionManager.getUserID();
            Log.d("userId", "userId = " + userID);

            retrieveData();


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DataModel dataModel = dataModelArrayList.get(position);

                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    //intent.putExtra(get(position),transaction_id);
                    intent.putExtra("tax_transaction_id", dataModel.getTransaction_id());
                    intent.putExtra("get_name", dataModel.getName());
                    //Log.d("transaction_id", "transaction_id : " + transaction_id);
                    startActivity(intent);
                }
            });

        }else {
            Toast.makeText(getContext(), "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public void retrieveData() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait..");

        //dataModelArrayList.clear();
        progressDialog.show();

        //String LIST_URL = "https://purpuligo.com/carsm/index.php/User_mobile/tax_pair_list_user_wise";
        String JSON_URL_LIST = Url.LIST_URL;

        HashMap<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("user_id", userID);
        //JSONObject jsonParam = new JSONObject(jsonParams);
        //JSONArray array=new JSONArray(jsonParam.toString());
        JSONArray mJSONArray = new JSONArray(Arrays.asList(jsonParams));
        Log.d("mJSONArray", "mJSONArray: " + mJSONArray);

        //JSONArray jsonArray = jsonParam.getJSONArray("");
        dataModelArrayList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, JSON_URL_LIST,
                mJSONArray, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //dataModelArrayList.clear();
                Log.d("data", "Data from server ListView: " + response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject2 = response.getJSONObject(i);
//                        DataModel dataModel = new DataModel();
//                        dataModel.setTransaction_id(jsonObject2.getString("tax_transaction_id"));
//                        dataModel.setType(jsonObject2.getString("type"));
//                        dataModel.setFinancialYear(jsonObject2.getString("financial_year"));
//                        dataModel.setDate(jsonObject2.getString("submit_date"));
//                        //dataModelList.add(dataModel);
                        transaction_id = jsonObject2.getString("tax_transaction_id");
                        type = jsonObject2.getString("type");
                        financialYear = jsonObject2.getString("financial_year");
                        date = jsonObject2.getString("submit_date");
                        status = jsonObject2.getString("status_description");
                        name = jsonObject2.getString("name");

                        dataModel = new DataModel(transaction_id, type, financialYear, date, status, name);
                        dataModelArrayList.add(dataModel);
                        statusListAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

//                dataModel = new DataModel(transaction_id, type, financialYear, date);
//                dataModelArrayList.add(dataModel);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"No Order List insert",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }
}