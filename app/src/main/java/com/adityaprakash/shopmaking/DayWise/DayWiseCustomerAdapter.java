package com.adityaprakash.shopmaking.DayWise;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adityaprakash.shopmaking.R;

import java.util.ArrayList;

public class DayWiseCustomerAdapter extends RecyclerView.Adapter<DayWiseCustomerAdapter.MyViewHolder> {
    private ArrayList<String> customerList;

    private Context mContext;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView;
        public TextView phoneView;
        public CardView mCardView;
        public MyViewHolder(View v) {
            super(v);
            nameView = v.findViewById(R.id.customer_name);


            mCardView = v.findViewById(R.id.cardView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DayWiseCustomerAdapter(Context context, ArrayList<String> customerList) {
        this.customerList = customerList;

        mContext =context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String customer = customerList.get(position);
        holder.nameView.setText(customer);



        //starting the calender activity
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(mContext,CustomerAllDetails.class);
                intent.putExtra("customerName",customer);
                Log.d("Successful",customer);

                mContext.startActivity(intent);



            }
        });

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return customerList.size();
    }
}

