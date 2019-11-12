package com.adityaprakash.shopmaking.invoice;

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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.adityaprakash.shopmaking.CustomerActivity;
import com.adityaprakash.shopmaking.Item;
import com.adityaprakash.shopmaking.ItemsActivity;
import com.adityaprakash.shopmaking.ItemsAdapter;
import com.adityaprakash.shopmaking.R;
import com.adityaprakash.shopmaking.SearchDetailsActivity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CartItemsActivity extends AppCompatActivity {



    private ArrayList<Items> itemsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button sendBillButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final String TAG = CartItemsActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    public SQLiteDatabase mInvoicedb;
    public TextView totalBill;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_items);
        initializer();



//        mInvoicedb = this.openOrCreateDatabase("Invoice.db",MODE_PRIVATE,null);
//        mInvoicedb.execSQL("CREATE TABLE IF NOT EXISTS ITEMS(ID INTEGER PRIMARY KEY AUTOINCREMENT,ITEM_NAME VARCHAR,QUANTITY VARCHAR,PRICE VARCHAR,PHONE VARCHAR,C_NAME VARCHAR)");


        //updateItemList();

        FloatingActionButton fab = findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();


                Snackbar.make(view, "Add Item to Cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        checkForSmsPermission();
        sendBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = getBillMessage();

                smsSendMessage(v,message);
            }
        });

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        String phone = extras.getString("phone");
        String name = extras.getString("name");
        Log.d("name",name);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Query firebaseSearchQuery = databaseReference.child("Users").child(currentUserId).child("Dates").child(formattedDate)
                .child(name).child("purchased_items");


        FirebaseRecyclerOptions<Items> options =
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(firebaseSearchQuery, Items.class)
                        .build();

        FirebaseRecyclerAdapter<Items, CartItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Items, CartItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartItemViewHolder holder, final int position, @NonNull Items model)
                    {
                        holder.itemName.setText(model.getItemName());
                        holder.itemPrice.setText("Sell Price"+model.getPrice());
                        holder.itemQty.setText("Qty-"+model.getQuantity());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String visit_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(CartItemsActivity.this, SearchDetailsActivity.class);
                                profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_list_view, viewGroup, false);
                        CartItemViewHolder viewHolder = new CartItemViewHolder(view);
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }




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


    public void smsSendMessage(View view,String message) {
        // EditText editText = (EditText) findViewById(R.id.editText_main);
        // Set the destination phone number to the string in editText.
        Bundle extras = getIntent().getExtras();
        String destinationAddress = extras.getString("phone");
        String customerName = extras.getString("name");
        Log.d("phone no",destinationAddress);




        // Find the sms_message view.
        // EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        // Get the text of the sms message.
        String smsMessage = "Store Name\n"+message;
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
        Toast.makeText(CartItemsActivity.this,"Bill Sent Successfully",Toast.LENGTH_SHORT).show();
    }

    private void addNewItem() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.cart_dialog_box, null);

        final TextInputEditText eItemName = alertLayout.findViewById(R.id.item_name);
        final TextInputEditText price = alertLayout.findViewById(R.id.price);
        final TextInputEditText quantity = alertLayout.findViewById(R.id.quantity);

        //databaseReference.child("Users")

        final AlertDialog.Builder builder = new AlertDialog.Builder(CartItemsActivity.this);
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
                    Toast.makeText(CartItemsActivity.this, "Please Enter Item Name...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(secondEntry))
                {
                    Toast.makeText(CartItemsActivity.this, "Please Enter Quantity...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(thirdEntry))
                {
                    Toast.makeText(CartItemsActivity.this, "Please Enter Price of one Item...", Toast.LENGTH_SHORT).show();
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

                                               if(Integer.parseInt(bn)>= Integer.parseInt(secondEntry)){
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


                                                   databaseReference.child("Users").child(currentUserId).child("Dates").
                                                           child(formattedDate).child("Customers").child(name).child("purchased_items").child(firstEntry).updateChildren(ItemsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {

                                                           if(task.isSuccessful()){
                                                               Toast.makeText(CartItemsActivity.this, "Item added Successfully", Toast.LENGTH_SHORT).show();
                                                              // updateItemList();
                                                           }
                                                           else{
                                                               String message = task.getException().toString();
                                                               Toast.makeText(CartItemsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                           }

                                                       }

                                                   });
                                                   String rem = Integer.toString(Integer.parseInt(bn) - Integer.parseInt(secondEntry));

                                                  databaseReference.child("Users")
                                                        .child(mAuth.getUid()).child("Items").child(firstEntry).child("quantity").setValue(rem);



                                               }
                                               else{
                                                   Toast.makeText(CartItemsActivity.this,"Quantity Not Sufficient",Toast.LENGTH_SHORT).show();
                                                   dialogInterface.cancel();
                                               }


                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                           }
                                       });



                           }


                           else{
                               Toast.makeText(CartItemsActivity.this,"Item Not found",Toast.LENGTH_SHORT).show();
                               dialogInterface.cancel();
                           }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(CartItemsActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();

                        }
                    });


//                    Bundle extras = getIntent().getExtras();
//                    String phone = extras.getString("phone");
//                    String name = extras.getString("name");
//                    Date c = Calendar.getInstance().getTime();
//                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                    String formattedDate = df.format(c);





//                    int pri = Integer.parseInt(thirdEntry);
//                    int qty = Integer.parseInt(secondEntry);
//                    int total = pri*qty;
//                    String totalPrice = new Integer(total).toString();
//
//
//                    String sql = "INSERT INTO ITEMS (ITEM_NAME,QUANTITY,PRICE,PHONE,C_NAME) VALUES (?,?,?,?,?)";
//                    SQLiteStatement statement = mInvoicedb.compileStatement(sql);
//                    statement.bindString(1,firstEntry);
//                    statement.bindString(2,secondEntry);
//                    statement.bindString(3,totalPrice);
//                    statement.bindString(4,phone);
//                    statement.bindString(5,name);
//
//                    try {
//                        statement.execute();
//                    }catch(Exception e){
//                        Toast.makeText(CartItemsActivity.this,"Some error occurs",Toast.LENGTH_SHORT).show();
//                    }
//
//                    updateItemList();


                    //itemsArrayList.add(new Items(firstEntry,"Qty: "+secondEntry,"Total Price: "+totalPrice));
                   // mAdapter.notifyDataSetChanged();
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








//    private void updateItemList() {
//
//        Bundle extras = getIntent().getExtras();
//        String phone = extras.getString("phone");
//        String name = extras.getString("name");
//        Date cw = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//        String formattedDate = df.format(cw);
//
//        databaseReference.child("Users").child(mAuth.getUid()).child("formattedDate")
//                .child("Customers").child(name).child("purchased_items").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                itemsArrayList.clear();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                {
//                    Items p = dataSnapshot1.getValue(Items.class);
//                    itemsArrayList.add(p);
//                }
//                mAdapter = new CartItemsAdapter(CartItemsActivity.this,itemsArrayList);
//                recyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(CartItemsActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
//
//            }
//        });


//        String phoneNo = extras.getString("phone");
//        //update array list
//        String query = "SELECT * FROM ITEMS WHERE PHONE =? AND C_NAME = ?;";
//        Cursor c = mInvoicedb.rawQuery(query, new String[]{phoneNo,name});
//        int nameIndex = c.getColumnIndex("ITEM_NAME");
//        int quantityIndex = c.getColumnIndex("QUANTITY");
//        int priceIndex = c.getColumnIndex("PRICE");
//        int phoneIndex = c.getColumnIndex("PHONE");
//        int totalPri = 0;
//        String topa = "";
//        String message = "";
//
//        if (c.moveToFirst()) {
//            itemsArrayList.clear();
//            do {
//
//                itemsArrayList.add(new Items(c.getString(nameIndex), c.getString(quantityIndex), c.getString(priceIndex)));
//                totalPri += Integer.parseInt(c.getString(priceIndex));
//                message+= c.getString(nameIndex)+" "+ c.getString(quantityIndex)+" "+ c.getString(priceIndex)+"\n";
//
//
//            } while (c.moveToNext());
//        }
//        topa = "Total: "+new Integer(totalPri).toString();
//        message += topa;
//        totalBill.setText(topa);
//        mAdapter.notifyItemInserted(itemsArrayList.size());
//        totalPri = 0;

   // }
    private String getBillMessage(){

        //fetching the date
        Bundle extras = getIntent().getExtras();
        String phoneNo = extras.getString("phone");
        String name = extras.getString("name");
        Log.d("update", phoneNo);

        //update array list
        String query = "SELECT * FROM ITEMS WHERE PHONE =? AND C_NAME = ?;";
        Cursor c = mInvoicedb.rawQuery(query, new String[]{phoneNo,name});
        int nameIndex = c.getColumnIndex("ITEM_NAME");
        int quantityIndex = c.getColumnIndex("QUANTITY");
        int priceIndex = c.getColumnIndex("PRICE");
        int phoneIndex = c.getColumnIndex("PHONE");
        int totalPri1 = 0;
        String topa = "";
        String message = "";

        if (c.moveToFirst()) {
            //itemsArrayList.clear();
            do {

                //itemsArrayList.add(new Items(c.getString(nameIndex), c.getString(quantityIndex), c.getString(priceIndex)));
                totalPri1 += Integer.parseInt(c.getString(priceIndex));
                message+= c.getString(nameIndex)+" qty-"+ c.getString(quantityIndex)+" tot_price="+ c.getString(priceIndex)+"\n";


            } while (c.moveToNext());
        }
        topa = "Total Amount: "+new Integer(totalPri1).toString();
        String fin_message = message+topa;
        //totalBill.setText(topa);
        //mAdapter.notifyItemInserted(itemsArrayList.size());
        totalPri1 = 0;
        return fin_message;

    }

    private void initializer() {



        recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        sendBillButton = (Button) findViewById(R.id.sen_bill_btn);
        totalBill = (TextView)findViewById(R.id.title_view);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQty;


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.price);
            itemQty = itemView.findViewById(R.id.qty);


        }

    }

//    private void disableSmsButton() {
//        Toast.makeText(this, R.string.sms_disabled, Toast.LENGTH_LONG).show();
//
//        sendBillButton.setVisibility(View.INVISIBLE);
//        Button retryButton = (Button) findViewById(R.id.button_retry);
//        retryButton.setVisibility(View.VISIBLE);
//    }
//
//    private void enableSmsButton() {
//        ImageButton smsButton = (ImageButton) findViewById(R.id.message_icon);
//        smsButton.setVisibility(View.VISIBLE);
//    }


    public void retryApp(View view) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(intent);
    }

}
