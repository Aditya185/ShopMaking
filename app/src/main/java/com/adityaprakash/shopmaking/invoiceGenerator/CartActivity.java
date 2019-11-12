package com.adityaprakash.shopmaking.invoiceGenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adityaprakash.shopmaking.Item;
import com.adityaprakash.shopmaking.ItemsActivity;
import com.adityaprakash.shopmaking.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    private RecyclerView CartRecyclerView;
    private DatabaseReference itemRef;
    private String currentUserid;
    private CartAdapter mAdapter;
    private FirebaseAuth mAuth;
    LayoutInflater mInflater;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private DatabaseReference databaseReference;
    private Button sendBillButton;
    private TextView totalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);

        initializer();
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);



        FloatingActionButton fab = findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();


                Snackbar.make(view, "Add your Items", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        CartRecyclerView.setLayoutManager(mLayoutManager);
        CartRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CartRecyclerView.setAdapter(mAdapter);

        checkForSmsPermission();
        sendBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getBillMessage();

            }
        });

      // only for setting total price of bill
        String customerName = extras.getString("name");


        databaseReference.child("Users").child(currentUserid).child("Dates").
                child(formattedDate).child("Customers").child(customerName).child("purchased_items")
                .addValueEventListener(new ValueEventListener() {

                    int totalPri1 = 0;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            Cart p = dataSnapshot1.getValue(Cart.class);
                            totalPri1+=Double.parseDouble(p.getItemQty())*Double.parseDouble(p.getItemPrice());


                        }
                       totalView.setText("Total: "+Double.toString(totalPri1));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }




    protected void onStart()
    {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);



        Query firebaseSearchQuery =FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid)
                .child("Dates")
                .child(formattedDate).child("Customers").child(name).child("purchased_items");

        // Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();

//        Query firebaseSearchQuery = databaseReference.child("Users").child(currentUserId).child("Items");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(firebaseSearchQuery, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartActivity.CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartActivity.CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartActivity.CartViewHolder holder,
                                                    final int position, @NonNull final Cart model) {
                        holder.itemName.setText(model.getItemName());
                        holder.itemPrice.setText("Sell Price: "+model.getItemPrice());
                        holder.itemQty.setText("Qty-"+model.getItemQty());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Delete"

                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Modify Item Details");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {

                                            Bundle extras = getIntent().getExtras();
                                            String name = extras.getString("name");
                                            Date c = Calendar.getInstance().getTime();
                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                            String formattedDate = df.format(c);
                                            databaseReference.child("Users").child(mAuth.getUid()).child("Items").child(model.ItemName).child("quantity")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String bn = dataSnapshot.getValue().toString();
                                                            double a =Double.parseDouble(bn);
                                                            double b =Double.parseDouble(model.getItemQty());
                                                            double c = a+b;

                                                            databaseReference.child("Users")
                                                                    .child(mAuth.getUid()).child("Items").child(model.ItemName)
                                                                    .child("quantity").setValue(Double.toString(c));


                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid).child("Dates")
                                                    .child(formattedDate).child("Customers").child(name).child("purchased_items").child(model.ItemName).removeValue();


                                            databaseReference.child("Users").child(currentUserid).child("Dates").
                                                    child(formattedDate).child("Customers").child(name).child("purchased_items")
                                                    .addValueEventListener(new ValueEventListener() {

                                                        int totalPri1 = 0;

                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                                                                Cart p = dataSnapshot1.getValue(Cart.class);
                                                                totalPri1+=Double.parseDouble(p.getItemQty())*Double.parseDouble(p.getItemPrice());


                                                            }
                                                            totalView.setText("Total: "+Double.toString(totalPri1));

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });







                                        }

                                    }
                                });
                                builder.show();
                            }
                        });



                    }

                    @NonNull
                    @Override
                    public CartActivity.CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_list_view, viewGroup, false);
                        CartActivity.CartViewHolder viewHolder = new CartActivity.CartViewHolder(view);
                        return viewHolder;
                    }
                };

        CartRecyclerView.setAdapter(adapter);

        adapter.startListening();


    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQty;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.price);
            itemQty = itemView.findViewById(R.id.qty);


        }

    }


    private void addNewItem() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.cart_dialog_box, null);

        final TextInputEditText eItemName = alertLayout.findViewById(R.id.item_name);
        final TextInputEditText price = alertLayout.findViewById(R.id.price);
        final TextInputEditText quantity = alertLayout.findViewById(R.id.quantity);

        final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("Add Item");
        builder.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        builder.setCancelable(false);

        //final EditText courseNameField = new EditText(CustomerActivity.this);


        builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i)
            {
                final String firstEntry = eItemName.getText().toString();
                final String secondEntry = quantity.getText().toString();
                final String thirdEntry = price.getText().toString();


                if (TextUtils.isEmpty(firstEntry))
                {
                    Toast.makeText(CartActivity.this, "Please Enter Item Name...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(secondEntry))
                {
                    Toast.makeText(CartActivity.this, "Please Enter Quantity...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(thirdEntry))
                {
                    Toast.makeText(CartActivity.this, "Please Enter Price of one Item...", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    databaseReference.child("Users").child(mAuth.getUid()).child("Items").child(firstEntry).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                databaseReference.child("Users")
                                        .child(mAuth.getUid()).child("Items").child(firstEntry).child("quantity")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String bn = dataSnapshot.getValue().toString();
                                                Log.d("qty",bn);

                                                if(Double.parseDouble(bn)>= Double.parseDouble(secondEntry)){
                                                    //fetching the PHONE NO
                                                    Bundle extras = getIntent().getExtras();
                                                    String phone = extras.getString("phone");
                                                    String name = extras.getString("name");
                                                    Date c = Calendar.getInstance().getTime();
                                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                                    String formattedDate = df.format(c);




                                                    HashMap<String,Object> ItemsMap = new HashMap<>();
                                                    ItemsMap.put("ItemName",firstEntry);
                                                    ItemsMap.put("quantity",secondEntry);
                                                    ItemsMap.put("price",thirdEntry);


                                                    databaseReference.child("Users").child(currentUserid).child("Dates").
                                                            child(formattedDate).child("Customers").child(name).child("purchased_items").child(firstEntry).updateChildren(ItemsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                Toast.makeText(CartActivity.this, "Item added Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else{
                                                                String message = task.getException().toString();
                                                                Toast.makeText(CartActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                            }

                                                        }


                                                    });
                                                    String rem = Double.toString(Double.parseDouble(bn) - Double.parseDouble(secondEntry));

                                                    databaseReference.child("Users")
                                                            .child(mAuth.getUid()).child("Items").child(firstEntry).child("quantity").setValue(rem);



                                                }
                                                else{
                                                    Toast.makeText(CartActivity.this,"Quantity Not Sufficient",Toast.LENGTH_SHORT).show();
                                                    dialogInterface.cancel();
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });



                            }


                            else{
                                Toast.makeText(CartActivity.this,"Item Not found",Toast.LENGTH_SHORT).show();
                                dialogInterface.cancel();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(CartActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
               // mAdapter.notifyDataSetChanged();
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

    private void initializer() {
        CartRecyclerView = (RecyclerView) findViewById(R.id.rec_view);
        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sendBillButton = (Button) findViewById(R.id.sen_bill_btn);
        totalView = (TextView) findViewById(R.id.title_view);


    }


    //sms sending area
    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the SMS button.
            //enableSmsButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                    //enableSmsButton();
                } else {
                    // Permission denied.
                    //Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, "Permission not Granted",
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                    //disableSmsButton();
                }
            }
        }
    }


    public void smsSendMessage(String message) {
        // EditText editText = (EditText) findViewById(R.id.editText_main);
        // Set the destination phone number to the string in editText.
        Bundle extras = getIntent().getExtras();
        String destinationAddress = extras.getString("phone");
        String customerName = extras.getString("name");
        Log.d("phone no",destinationAddress);




        // Find the sms_message view.
        // EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        // Get the text of the sms message.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String shop = sharedPref.getString("shop_name", "Not Available");
        String smsMessage = shop+"\n"+message;
        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Check for permission first.
        checkForSmsPermission();
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage,
                sentIntent, deliveryIntent);
        Toast.makeText(CartActivity.this,"Bill Sent Successfully",Toast.LENGTH_SHORT).show();
    }
    private String getBillMessage() {
        Bundle extras = getIntent().getExtras();
        String destinationAddress = extras.getString("phone");
        String customerName = extras.getString("name");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);



        databaseReference.child("Users").child(currentUserid).child("Dates").
                child(formattedDate).child("Customers").child(customerName).child("purchased_items")
                .addValueEventListener(new ValueEventListener() {
                    String message = "";
                    double totalPri1 = 0;
                    String fin_message ="";
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            Cart p = dataSnapshot1.getValue(Cart.class);
                            totalPri1+=Double.parseDouble(p.getItemQty())*Double.parseDouble(p.getItemPrice());
                            message += p.getItemName() + " qty-" + p.getItemQty() + " price=" + p.getItemPrice() + "\n";

                        }
                        fin_message = message +"Total: "+ Double.toString(totalPri1);
                        Log.d("message",fin_message);
                        smsSendMessage(fin_message);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//          topa = "Total Amount: "+new Integer(totalPri1).toString();
//        fin_message = message+topa;





      return "";
    }




}
