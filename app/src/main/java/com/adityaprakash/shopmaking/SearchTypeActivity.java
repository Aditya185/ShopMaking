package com.adityaprakash.shopmaking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchTypeActivity extends AppCompatActivity {

    private Button itemButton;
    private Button itemLocation;
    private Button itemBarCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_type);
        initializer();

        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(SearchTypeActivity.this,SearchActivity.class);
                startActivity(searchIntent);

            }
        });
        itemLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(SearchTypeActivity.this,SearchLocation.class);
                startActivity(searchIntent);

            }
        });
        itemBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(SearchTypeActivity.this,BarcodeScannerActivity.class);
                startActivity(searchIntent);

            }
        });


    }

    private void initializer() {
        itemButton = (Button)findViewById(R.id.search_item_name);
        itemLocation = (Button)findViewById(R.id.search_item_location);
        itemBarCode = (Button)findViewById(R.id.search_item_barcode);


    }
}
