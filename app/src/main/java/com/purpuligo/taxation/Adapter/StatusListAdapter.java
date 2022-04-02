package com.purpuligo.taxation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.purpuligo.taxation.Model.DataModel;
import com.purpuligo.taxation.R;
import java.util.List;

public class StatusListAdapter extends ArrayAdapter<DataModel> {
    Context context;
    List<DataModel> arrayListData;

    public StatusListAdapter(@NonNull Context context, List<DataModel> arrayListData) {
        super(context, R.layout.status_view_layout, arrayListData);
        this.context = context;
        this.arrayListData = arrayListData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_view_layout,null,true);

        TextView submitId = view.findViewById(R.id.submit_id);
        TextView submitType = view.findViewById(R.id.submit_types);
        TextView submitYear = view.findViewById(R.id.submit_financialYear);
        TextView submitDate = view.findViewById(R.id.submit_date);
        TextView submitStatus = view.findViewById(R.id.application_status);
        TextView submitName = view.findViewById(R.id.submit_name);

        submitId.setText(arrayListData.get(position).getTransaction_id());
        submitType.setText(arrayListData.get(position).getType());
        submitYear.setText(arrayListData.get(position).getFinancialYear());
        submitDate.setText(arrayListData.get(position).getDate());
        submitStatus.setText(arrayListData.get(position).getStatus());
        submitName.setText(arrayListData.get(position).getName());

        return view;
    }
}
