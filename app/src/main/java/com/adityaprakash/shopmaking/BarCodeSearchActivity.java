package com.adityaprakash.shopmaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class BarCodeSearchActivity extends AppCompatActivity {


    private String currentUserId;
    private FirebaseAuth mAuth;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_search);

        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();




        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        String search_item = extras.getString("key");
        firebaseUserSearch(search_item);


        

    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(BarCodeSearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.child("Users").child(currentUserId).child("Items").orderByChild("ItemName").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(firebaseSearchQuery, Item.class)
                        .build();

        FirebaseRecyclerAdapter<Item, SearchActivity.ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Item, SearchActivity.ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SearchActivity.ItemViewHolder holder, final int position, @NonNull Item model) {
                        holder.itemName.setText(model.getItemName());
                        holder.itemPrice.setText("MRP-"+model.getPrice());
                        holder.itemQty.setText("Qty-"+model.getQuantity());
                        holder.itemLocation.setText(model.getLocation());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String visit_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(BarCodeSearchActivity.this, SearchDetailsActivity.class);
                                profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public SearchActivity.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup, false);
                        SearchActivity.ItemViewHolder viewHolder = new SearchActivity.ItemViewHolder(view);
                        return viewHolder;
                    }
                };

        mResultList.setAdapter(adapter);

        adapter.startListening();


    }


    //onStart
//
//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//
//        FirebaseRecyclerOptions<Items> options =
//                new FirebaseRecyclerOptions.Builder<Items>()
//                        .setQuery(firebaseSearchQuery, Items.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Items, ItemViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Items, ItemViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull Items model)
//                    {
//                        holder.itemName.setText(model.getItemName());
//                        holder.itemPrice.setText(model.getPrice());
//                        holder.itemQty.setText(model.getQuantity());
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                String visit_user_id = getRef(position).getKey();
//
//                                Intent profileIntent = new Intent(SearchActivity.this, SearchDetailsActivity.class);
//                                profileIntent.putExtra("visit_user_id", visit_user_id);
//                                startActivity(profileIntent);
//                            }
//                        });
//
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
//                    {
//                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup, false);
//                        ItemViewHolder viewHolder = new ItemViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//
//        mResultList.setAdapter(adapter);
//
//        adapter.startListening();
//    }
//
//
//


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
}
