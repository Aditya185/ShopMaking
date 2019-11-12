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
import com.adityaprakash.shopmaking.invoiceGenerator.Cart;

import java.util.ArrayList;

public class CustomerAllDetailsAdapter extends RecyclerView.Adapter<CustomerAllDetailsAdapter.MyViewHolder> {
    private ArrayList<Cart> cartList;

    private Context mContext;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView itemPrice;
        public TextView itemQty;
        public CardView mCardView;
        public MyViewHolder(View v) {
            super(v);
            itemName = v.findViewById(R.id.item_name);
            itemPrice = v.findViewById(R.id.price);
            itemQty = v.findViewById(R.id.qty);

            mCardView = v.findViewById(R.id.cardView5);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomerAllDetailsAdapter(Context context, ArrayList<Cart> cartList) {
        this.cartList = cartList;

        mContext =context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_list_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Cart cart = cartList.get(position);
        holder.itemName.setText(cart.getItemName());
        holder.itemPrice.setText("MRP: "+cart.getItemPrice());
        holder.itemQty.setText("Qty: "+cart.getItemQty());


        //starting the calender activity
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                Intent intent = new Intent(mContext,DayWiseCustomers.class);
//                intent.putExtra("dateId",date);
//                Log.d("Successful",date);
//
//                mContext.startActivity(intent);



            }
        });

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cartList.size();
    }
}


