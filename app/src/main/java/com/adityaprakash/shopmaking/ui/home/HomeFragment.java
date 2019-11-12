package com.adityaprakash.shopmaking.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adityaprakash.shopmaking.Item;
import com.adityaprakash.shopmaking.ItemsActivity;
import com.adityaprakash.shopmaking.ItemsAdapter;
import com.adityaprakash.shopmaking.R;
import com.adityaprakash.shopmaking.SearchTypeActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

   // private HomeViewModel homeViewModel;

    private ArrayList<Item> itemsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter mAdapter;
    private Button findButton;
    public TextView totalBill;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    LayoutInflater mInflater;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Items in Store");


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        mAdapter = new ItemsAdapter(mContext, itemsArrayList);
        findButton = (Button) root.findViewById(R.id.find_btn);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        FloatingActionButton fab = root.findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();


                Snackbar.make(view, "Add your Items", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), SearchTypeActivity.class);
                startActivity(searchIntent);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        return root;
    }


    public void onStart()
    {
        super.onStart();

        // Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = databaseReference.child("Users").child(currentUserId).child("Items");
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(firebaseSearchQuery, Item.class)
                        .build();

        FirebaseRecyclerAdapter<Item, ItemsActivity.ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Item, ItemsActivity.ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemsActivity.ItemViewHolder holder, final int position, @NonNull final Item model) {
                        holder.itemName.setText(model.getItemName());
                        holder.itemPrice.setText("MRP-"+model.getPrice());
                        holder.itemQty.setText("Qty-"+model.getQuantity());
                        holder.itemLocation.setText(model.getLocation());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Delete",
                                                "Edit"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Modify Item Details");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {

                                            databaseReference.child("Users").child(currentUserId).child("Items").child(model.getItemName()).removeValue();

                                        }
                                        if(i == 1){

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("Add Item");
                                            mInflater = LayoutInflater.from(getActivity());
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
                                                        Toast.makeText(getActivity(), "Please Enter Quantity...", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if (TextUtils.isEmpty(thirdEny))
                                                    {
                                                        Toast.makeText(getActivity(), "Please Enter Price of one Item...", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if (TextUtils.isEmpty(fourthEny))
                                                    {
                                                        Toast.makeText(getActivity(), "Please Enter New Location of Item...", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {


                                                        databaseReference.child("Users").child(currentUserId).child("Items").child(model.getItemName()).child("quantity").setValue(secondEny);
                                                        databaseReference.child("Users").child(currentUserId).child("Items").child(model.getItemName()).child("price").setValue(thirdEny);
                                                        databaseReference.child("Users").child(currentUserId).child("Items").child(model.getItemName()).child("location").setValue(fourthEny);

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

                    @NonNull
                    @Override
                    public ItemsActivity.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup, false);
                        ItemsActivity.ItemViewHolder viewHolder = new ItemsActivity.ItemViewHolder(view);
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);

        adapter.startListening();


    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQty,itemLocation;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.price);
            itemQty = itemView.findViewById(R.id.qty);
            itemLocation = itemView.findViewById(R.id.location2);

        }

    }

    private void addNewItem() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.items_dialog_box, null);

        final TextInputEditText eItemName = alertLayout.findViewById(R.id.item_name);
        final TextInputEditText price = alertLayout.findViewById(R.id.price);
        final TextInputEditText quantity = alertLayout.findViewById(R.id.quantity);
        final TextInputEditText location1 = alertLayout.findViewById(R.id.location_row);
        final TextInputEditText location2 = alertLayout.findViewById(R.id.location_col);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Item");
        builder.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        builder.setCancelable(false);

        //final EditText courseNameField = new EditText(CustomerActivity.this);


        builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String firstEntry = eItemName.getText().toString();
                String secondEntry = quantity.getText().toString();
                String thirdEntry = price.getText().toString();
                String loc_row = location1.getText().toString();
                String loc_col = location2.getText().toString();
                String fourthEntry = "row: "+loc_row+" col: "+loc_col;


                Item items = new Item(firstEntry,secondEntry,thirdEntry,fourthEntry);


                if (TextUtils.isEmpty(firstEntry))
                {
                    Toast.makeText(getActivity(), "Please Enter Item Name...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(secondEntry))
                {
                    Toast.makeText(getActivity(), "Please Enter Quantity...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(thirdEntry))
                {
                    Toast.makeText(getActivity(), "Please Enter Price of one Item...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(fourthEntry))
                {
                    Toast.makeText(getActivity(), "Please Enter location of Item...", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    HashMap<String,Object> ItemsMap = new HashMap<>();
                    ItemsMap.put("ItemName",firstEntry);
                    ItemsMap.put("quantity",secondEntry);
                    ItemsMap.put("price",thirdEntry);
                    ItemsMap.put("location",fourthEntry);
                    databaseReference.child("Users").child(currentUserId).child("Items").child(firstEntry).updateChildren(ItemsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Item added Successfully", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    mAdapter.notifyDataSetChanged();
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