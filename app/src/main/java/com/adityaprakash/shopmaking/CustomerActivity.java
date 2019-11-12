package com.adityaprakash.shopmaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adityaprakash.shopmaking.invoiceGenerator.CartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CustomerActivity extends AppCompatActivity {

    private Button addItemButton;
    private EditText customerName;
    private  EditText phoneNo;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        initialier();
        String currentUserId = mAuth.getUid();





        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(customerName.getText().toString())) {
                    Toast.makeText(CustomerActivity.this, "Please enter name...", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(phoneNo.getText().toString())) {
                    Toast.makeText(CustomerActivity.this, "Please enter phone...", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(CustomerActivity.this, "Customer added Successfully", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(CustomerActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }

                        }

                    });

                }
//                databaseReference.child("Users").child(currentUserId).child("")
                Intent intent = new Intent(CustomerActivity.this, CartActivity.class);
                intent.putExtra("name",customerName.getText().toString());
                intent.putExtra("phone",phoneNo.getText().toString());
                startActivity(intent);



            }
        });
    }

    private void initialier() {
        customerName = (EditText) findViewById(R.id.editText);
        phoneNo = (EditText) findViewById(R.id.editText2);
        addItemButton = (Button) findViewById(R.id.signUpButton);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


    }
}
