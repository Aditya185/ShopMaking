package com.adityaprakash.shopmaking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchDetailsActivity extends AppCompatActivity {
    private TextView itemName,mrp,itemqty,itemLocation;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;



    private DatabaseReference itemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_details);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Item Details");

        initilaizer();

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("itemName");
        String qty = extras.getString("itemQty");
        String price = extras.getString("itemPrice");
        String location = extras.getString("itemLocation");

        itemName.setText("Item: "+name);
        mrp.setText("MRP: "+price);
        itemqty.setText("Quantity: "+qty);
        itemLocation.setText("Location: "+location);





    }

    private void initilaizer() {

        itemName = (TextView) findViewById(R.id.name);
        mrp = (TextView) findViewById(R.id.itemPrice);
        itemqty = (TextView) findViewById(R.id.itemQty);
        itemLocation = (TextView) findViewById(R.id.itemLocation);
        mAuth = FirebaseAuth.getInstance();
        itemDatabase = FirebaseDatabase.getInstance().getReference();

    }
}
