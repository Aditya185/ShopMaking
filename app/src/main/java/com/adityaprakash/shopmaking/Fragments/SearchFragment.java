package com.adityaprakash.shopmaking.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adityaprakash.shopmaking.BarcodeScannerActivity;
import com.adityaprakash.shopmaking.R;
import com.adityaprakash.shopmaking.SearchActivity;
import com.adityaprakash.shopmaking.SearchLocation;
import com.adityaprakash.shopmaking.SearchTypeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private Button itemButton;
    private Button itemLocation;
    private Button itemBarCode;
    public SearchFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Select Search type");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        itemButton = (Button)root.findViewById(R.id.search_item_name);
        itemLocation = (Button)root.findViewById(R.id.search_item_location);
        itemBarCode = (Button)root.findViewById(R.id.search_item_barcode);

        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);

            }
        });
        itemLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), SearchLocation.class);
                startActivity(searchIntent);

            }
        });
        itemBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), BarcodeScannerActivity.class);
                startActivity(searchIntent);

            }
        });

        return root;
    }




}
