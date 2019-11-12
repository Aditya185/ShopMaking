package com.adityaprakash.shopmaking.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adityaprakash.shopmaking.CustomerActivity;
import com.adityaprakash.shopmaking.R;
import com.adityaprakash.shopmaking.invoiceGenerator.CartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class InvoiceFragment extends Fragment {

    private Button addItemButton;
    private EditText customerName;
    private  EditText phoneNo;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Context mContext;


    public InvoiceFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Enter Customer Details");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_invoice, container, false);
        customerName = (EditText) root.findViewById(R.id.editText);
        phoneNo = (EditText) root.findViewById(R.id.editText2);
        addItemButton = (Button)root.findViewById(R.id.signUpButton);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String currentUserId = mAuth.getUid();





        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(customerName.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter name...", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(phoneNo.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter phone...", Toast.LENGTH_SHORT).show();
                } else {


                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    String name = customerName.getText().toString();
                    String phone = phoneNo.getText().toString();
                    // Log.d("phone_no",phone);
                    HashMap<String,Object> ItemsMap = new HashMap<>();
                    ItemsMap.put("name",name);
                    ItemsMap.put("phone",phone);


                    databaseReference.child("Users").child(mAuth.getUid()).child("Dates").
                            child(formattedDate).child("Customers").child(name).child("details").updateChildren(ItemsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Customer added Successfully", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }

                        }

                    });

                }
//                databaseReference.child("Users").child(currentUserId).child("")
                Intent intent = new Intent(getActivity(), CartActivity.class);
                intent.putExtra("name",customerName.getText().toString());
                intent.putExtra("phone",phoneNo.getText().toString());
                startActivity(intent);



            }
        });


        return root;
    }


}
