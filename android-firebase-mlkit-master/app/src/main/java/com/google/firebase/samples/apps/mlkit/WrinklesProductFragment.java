package com.google.firebase.samples.apps.mlkit;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class WrinklesProductFragment extends Fragment {

    private RecyclerView allProductsList;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference bucketReference;
    private FirebaseAuth mAuth;

    private View myMainView;


    public WrinklesProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myMainView = inflater.inflate(R.layout.fragment_wrinkles_product, container, false);

        mAuth = FirebaseAuth.getInstance();

        allProductsList = (RecyclerView) myMainView.findViewById(R.id.all_product_list);
        allProductsList.setHasFixedSize(true);
        allProductsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child("Wrinkles and Anti-Aging");

        //Offline
        mDatabaseReference.keepSynced(true);

        return myMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product_item_data,AllProductsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Product_item_data, AllProductsViewHolder>(
                        Product_item_data.class,
                        R.layout.product_item,
                        AllProductsViewHolder.class,
                        mDatabaseReference
                ) {
                    @Override
                    protected void populateViewHolder(final AllProductsViewHolder viewHolder, Product_item_data model, int position) {

                        final String itemcode = getRef(position).getKey();

                        mDatabaseReference.child(itemcode).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String productname = dataSnapshot.child("Product_Name").getValue().toString();
                                final String productprice = dataSnapshot.child("Product_Price").getValue().toString();
                                String productdescription = dataSnapshot.child("Product_Description").getValue().toString();
                                String productimage = dataSnapshot.child("Product_Image").getValue().toString();
                                viewHolder.setProduct_name(productname);
                                viewHolder.setProduct_price(productprice);
                                viewHolder.setProduct_description(productdescription);
                                viewHolder.setProduct_image(getContext(),productimage);
                                viewHolder.setQuantity(getContext());
                                viewHolder.quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        final String quantity = parent.getItemAtPosition(position).toString();

                                        viewHolder.button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String currentUserId = mAuth.getCurrentUser().getUid();
                                                bucketReference = FirebaseDatabase.getInstance().getReference().child("Bucket").child(currentUserId).child(itemcode);
                                                bucketReference.child("Product_Name").setValue(productname);
                                                bucketReference.child("Product_Price").setValue(productprice);
                                                bucketReference.child("Product_Quantity").setValue(quantity);
                                                bucketReference.child("Product_Code").setValue(itemcode).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getActivity(),"Product has been added to cart", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }
                                        });

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                };
        allProductsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AllProductsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        Button button;
        Spinner quantity;
        TextView quantity_text;

        public AllProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            button = (Button) mView.findViewById(R.id.addtocart);
            quantity = (Spinner) mView.findViewById(R.id.quantity_spinner);
            quantity_text = (TextView) mView.findViewById(R.id.quantity_text);
        }


        public void setProduct_name(String product_name){
            TextView productname = (TextView) mView.findViewById(R.id.productname);
            productname.setText(product_name);
        }

        public void setProduct_price(String product_price){
            TextView productprice = (TextView) mView.findViewById(R.id.productprice);
            productprice.setText(product_price);

        }

        public void setProduct_description(String product_description){
            TextView productdescription = (TextView) mView.findViewById(R.id.productdescription);
            productdescription.setText(product_description);

        }

        public void  setProduct_image(final Context context, final String productimage) {
            final ImageView image = (ImageView) mView.findViewById(R.id.productimage);

            Picasso.with(context).load(productimage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.product).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(productimage).placeholder(R.drawable.product).into(image);

                }
            });
            // Picasso.get().load(productimage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.product).into(image);
        }

        public void setQuantity(Context context){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.quantity,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
            quantity.setAdapter(adapter);
        }


    }

}
