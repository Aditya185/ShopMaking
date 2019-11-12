package com.adityaprakash.shopmaking.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adityaprakash.shopmaking.DayWise.DateActivity;
import com.adityaprakash.shopmaking.DayWise.DateAdapter;
import com.adityaprakash.shopmaking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DateWiseFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference dateRef;
    private DateAdapter mAdapter;

    private ArrayList<String> dateList = new ArrayList<>();


    public DateWiseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_date_wise_adapter, container, false);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView)root. findViewById(R.id.recycler_view);
        dateRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid()).child("Dates");
        updateDateList();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        return root;
    }


    private void updateDateList() {

        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dateList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    String p = dataSnapshot1.getKey();
                    Log.d("MyDate",p);
                    dateList.add(p);
                }
                mAdapter = new DateAdapter(getActivity(),dateList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyItemInserted(dateList.size());            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

}
