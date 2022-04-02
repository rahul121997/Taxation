package com.purpuligo.taxation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.taxation.R;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {

    //private int[] images;
    private String [] text;

    public SliderAdapter( String[] text) {
       // this.images = images;
        this.text = text;
    }

    //string userd in glider
    //private String[] images;
    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_layout,null);

        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {

        //used set image glide when url used
       // viewHolder.imageView.setImageResource(images[position]);
        viewHolder.textView.setText(text[position]);

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public int getCount() {
        return text.length;
    }

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder{

       // private ImageView imageView;
        private TextView textView;
        public SliderViewHolder(View itemView) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.textDescription);
        }
    }
}
