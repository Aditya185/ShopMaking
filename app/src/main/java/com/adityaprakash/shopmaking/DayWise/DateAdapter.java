package com.adityaprakash.shopmaking.DayWise;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adityaprakash.shopmaking.R;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyViewHolder> {
    private ArrayList<String> dateList;

    private Context mContext;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView DateView;
        public CardView mCardView;
        public MyViewHolder(View v) {
            super(v);
            DateView = v.findViewById(R.id.date_view);

            mCardView = v.findViewById(R.id.cardView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DateAdapter(Context context, ArrayList<String> dateList) {
        this.dateList = dateList;

        mContext =context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_list_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String date = dateList.get(position);
        holder.DateView.setText("Date - "+date);


        //starting the calender activity
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(mContext,DayWiseCustomers.class);
                intent.putExtra("dateId",date);
                Log.d("Successful",date);

                mContext.startActivity(intent);



            }
        });

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dateList.size();
    }
}

