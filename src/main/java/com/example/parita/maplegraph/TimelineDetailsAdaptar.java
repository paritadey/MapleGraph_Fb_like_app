package com.example.parita.maplegraph;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by parita on 06-02-2018.
 */

public class TimelineDetailsAdaptar extends RecyclerView.Adapter<TimelineDetailsAdaptar.ViewHolder> {
    private Context mCtx;
    List<Details> productList = Collections.emptyList();
    Details current;

    public TimelineDetailsAdaptar(Context mCtx, List<Details> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

    }

    @Override
    public TimelineDetailsAdaptar.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.timeline_details, null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder myHolder = (ViewHolder) holder;
        Details current = productList.get(position);

        myHolder.timeuploaded.setText("Time : " + current.time);
        myHolder.detailsuploaded.setText("Content : " + current.details);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView timeuploaded, detailsuploaded;
        public ViewHolder(View itemView) {
            super(itemView);
            timeuploaded = itemView.findViewById(R.id.timeuploaded);
            detailsuploaded = itemView.findViewById(R.id.detailsuploaded);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
