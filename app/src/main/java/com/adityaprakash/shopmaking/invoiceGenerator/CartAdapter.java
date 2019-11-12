package com.adityaprakash.shopmaking.invoiceGenerator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adityaprakash.shopmaking.R;
import com.adityaprakash.shopmaking.invoice.Items;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private ArrayList<Cart> itemsArrayList;

    private Context mContext;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserId=mAuth.getUid();
    LayoutInflater mInflater;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView ItemsName,Quantity,Price;
        public CardView mCardView;
        public MyViewHolder(View v) {
            super(v);
            ItemsName = v.findViewById(R.id.item_name);
            Quantity = v.findViewById(R.id.qty);
            Price = v.findViewById(R.id.price);
            mCardView = v.findViewById(R.id.cardView5);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CartAdapter(Context context, ArrayList<Cart> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
        mContext =context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_list_view, parent, false);

        CartAdapter.MyViewHolder vh = new CartAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Cart item = itemsArrayList.get(position);
        holder.ItemsName.setText(item.getItemName());
        holder.Price.setText("Price: "+item.getItemPrice());
        holder.Quantity.setText("Qty: "+item.getItemQty());



        //starting the main activity
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Delete",
                                "Edit"
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Modify Item Details");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {


                            databaseReference.child("Users").child(currentUserId).child(item.getItemName()).removeValue();

                        }
                        if(i == 1){

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Add Item");
                            mInflater = LayoutInflater.from(mContext);
                            View alertLayout = mInflater.inflate(R.layout.update_item_view, null);

                            builder.setView(alertLayout);
                            // disallow cancel of AlertDialog on click of back button and outside touch
                            builder.setCancelable(false);

                            //final EditText courseNameField = new EditText(CustomerActivity.this);
//                                    final TextInputEditText eItemName = alertLayout.findViewById(R.id.item_name);
                            final TextInputEditText price = alertLayout.findViewById(R.id.price);
                            final TextInputEditText quantity = alertLayout.findViewById(R.id.quantity);
                            final TextInputEditText location1 = alertLayout.findViewById(R.id.location_row);
                            final TextInputEditText location2 = alertLayout.findViewById(R.id.location_col);


                            builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
//                                            String firstEntry = eItemName.getText().toString();
                                    String secondEny = quantity.getText().toString();
                                    String thirdEny = price.getText().toString();
                                    String loc_row = location1.getText().toString();
                                    String loc_col = location2.getText().toString();
                                    String fourthEny = "row: "+loc_row+" col: "+loc_col;

//                                            Items items = new Items(firstEntry,secondEntry,thirdEntry);



                                    if (TextUtils.isEmpty(secondEny))
                                    {
                                        Toast.makeText(mContext, "Please Enter Quantity...", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (TextUtils.isEmpty(thirdEny))
                                    {
                                        Toast.makeText(mContext, "Please Enter Price of one Item...", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (TextUtils.isEmpty(fourthEny))
                                    {
                                        Toast.makeText(mContext, "Please Enter New Location of Item...", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {


                                        databaseReference.child("Users").child(currentUserId).child(item.getItemName()).child("quantity").setValue(secondEny);
                                        databaseReference.child("Users").child(currentUserId).child(item.getItemName()).child("price").setValue(thirdEny);
                                        databaseReference.child("Users").child(currentUserId).child(item.getItemName()).child("location").setValue(fourthEny);

                                    }
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    dialogInterface.cancel();
                                }
                            });

                            builder.show();


                        }
                    }
                });
                builder.show();
            }
        });

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }
}


