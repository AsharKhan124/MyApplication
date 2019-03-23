package com.google.firebase.samples.apps.mlkit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BucketActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView allbucketList;
    private DatabaseReference bucketReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);
        setupBottomNavigationView();

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();

        mToolbar = findViewById(R.id.all_product_list_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Bucket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allbucketList = (RecyclerView) findViewById(R.id.all_bucket_list);
        allbucketList.setHasFixedSize(true);
        allbucketList.setLayoutManager(new LinearLayoutManager(this));

        bucketReference = FirebaseDatabase.getInstance().getReference().child("Bucket").child(currentUserId);

        //Offline
        bucketReference.keepSynced(true);
    }

    //BottomNavugationView Setup
    private void setupBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
       // bottomNavigationViewEx.setItemBackground(4,R.color.colorAccent);
        bottomNavigationViewEx.setIconSizeAt(2,30,30);
        bottomNavigationViewEx.setCurrentItem(4).enableShiftingMode(4,true);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(BucketActivity.this,bottomNavigationViewEx);

        //updateNavigationBarState(R.id.ic_buckett);

    }

    /*private void updateNavigationBarState(int actionId){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationViewEx.getMenu();

        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<bucket_item_data,BucketViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<bucket_item_data, BucketViewHolder>(
                        bucket_item_data.class,
                        R.layout.bucket_item,
                        BucketViewHolder.class,
                        bucketReference
                ) {
                    @Override
                    protected void populateViewHolder(final BucketViewHolder viewHolder, bucket_item_data model, int position) {

                        String itemcode = getRef(position).getKey();

                        bucketReference.child(itemcode).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String productcode=dataSnapshot.child("Product_Code").getValue().toString();
                                String productprice=dataSnapshot.child("Product_Price").getValue().toString();
                                String productname = dataSnapshot.child("Product_Name").getValue().toString();
                                String productquantity = dataSnapshot.child("Product_Quantity").getValue().toString();

                                viewHolder.setProduct_name(productname);
                                viewHolder.setProduct_price(productprice);
                                viewHolder.setProduct_code(productcode);
                                viewHolder.setProduct_quantity(productquantity);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                };

        allbucketList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BucketViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public BucketViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setProduct_name(String product_name){
            TextView productname = mView.findViewById(R.id.pro_name);
            productname.setText(product_name);

        }



        public void setProduct_price(String product_price){
            TextView productprice = mView.findViewById(R.id.price);
            productprice.setText(product_price);

        }

        public void setProduct_code(String product_code){
            TextView productcode = mView.findViewById(R.id.code);
            productcode.setText(product_code);

        }

        public void setProduct_quantity(String product_quantity){
            TextView productquantity = mView.findViewById(R.id.pro_quantity);
            productquantity.setText(product_quantity);

        }
    }
}
