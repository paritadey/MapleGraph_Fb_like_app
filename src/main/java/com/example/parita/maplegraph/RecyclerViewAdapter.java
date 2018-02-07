package com.example.parita.maplegraph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by parita on 06-02-2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    List<DataAdapter> dataAdapters;


    public RecyclerViewAdapter(List<DataAdapter> getDataAdapter, Context context){

        super();

        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        DataAdapter dataAdapter =  dataAdapters.get(position);

        viewHolder.TextViewEmail.setText(dataAdapter.getEmail());

        viewHolder.TextViewTime.setText(String.valueOf(dataAdapter.getTime()));

        viewHolder.TextViewContent.setText(dataAdapter.getDetails());
        final String post=viewHolder.TextViewContent.getText().toString().trim();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),SharedPostUpload.class);
                i.putExtra("details",post);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView TextViewEmail;
        public TextView TextViewTime;
        public TextView TextViewContent;
        public Button like, dislike;

        public ViewHolder(View itemView) {

            super(itemView);

            TextViewEmail = (TextView) itemView.findViewById(R.id.textView2);
            TextViewTime = (TextView) itemView.findViewById(R.id.textView4);
            TextViewContent = (TextView) itemView.findViewById(R.id.textView6);

            like=(Button)itemView.findViewById(R.id.like);
            dislike=(Button)itemView.findViewById(R.id.dislike);


            like.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    dislike.setEnabled(false);
                    like.setBackgroundResource(R.drawable.likeditem);
                }
            });
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.setEnabled(false);
                    dislike.setBackgroundResource(R.drawable.dislikeitem);
                }
            });


        }

    }


}
