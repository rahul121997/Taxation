package com.purpuligo.taxation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.purpuligo.taxation.Model.CommunicationModel;
import com.purpuligo.taxation.R;

import java.util.List;

public class CommunicationAdapter extends ArrayAdapter<CommunicationModel> {
    Context context;
    List<CommunicationModel> arrayListCommunication;

    public CommunicationAdapter(@NonNull Context  context, List<CommunicationModel> arrayListCommunication) {
        super(context, R.layout.communication_view_layout, arrayListCommunication);
        this.context = context;
        this.arrayListCommunication = arrayListCommunication;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.communication_view_layout,null,false);

        TextView date = view.findViewById(R.id.date);
        TextView name = view.findViewById(R.id.name);
        TextView details = view.findViewById(R.id.details);

        date.setText(arrayListCommunication.get(position).getDate());
        name.setText(arrayListCommunication.get(position).getName());
        details.setText(arrayListCommunication.get(position).getDetails());

        return view;
    }
}

