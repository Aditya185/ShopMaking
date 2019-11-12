package com.adityaprakash.shopmaking.DayWise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adityaprakash.shopmaking.R;
import com.adityaprakash.shopmaking.invoiceGenerator.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerAllDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference dateRef;
    private CustomerAllDetailsAdapter mAdapter;

    private ArrayList<Cart> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_all_details);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_customer_view);
        Bundle extras = getIntent().getExtras();
        String cusomer_name = extras.getString("customerName");
        Log.d("Name",cusomer_name);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String date = sharedPref.getString("date", "Not Available");

        dateRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Dates").child(date).child("Customers").child(cusomer_name).child("purchased_items");

        updateItemList();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    private void updateItemList() {

        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Cart p = dataSnapshot1.getValue(Cart.class);
                    Log.d("MyDate",p.toString());
                    cartList.add(p);
                }
                mAdapter = new CustomerAllDetailsAdapter(CustomerAllDetails.this,cartList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyItemInserted(cartList.size());            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}
