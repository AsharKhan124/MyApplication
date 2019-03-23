package com.google.firebase.samples.apps.mlkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // private EditText ProductCategory;
    private Spinner ProductCategory;
    private EditText ProductCode;
    private EditText ProductName;
    private EditText ProductPrice;
    private EditText ProductDescription;

    private ImageView ProductImage;

    private Button AddProduct;

    private DatabaseReference mDatabaseReference;

    private StorageReference mStorageReference;

    private final static int PRODUCT_PIC = 1;

    Uri ImageUri;

    private View myMainView;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         myMainView=inflater.inflate(R.layout.fragment_add_product, container, false);

        ProductCategory = (Spinner) myMainView.findViewById(R.id.product_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.category,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        ProductCategory.setAdapter(adapter);


        ProductName = (EditText) myMainView.findViewById(R.id.product_name);
        ProductPrice = (EditText) myMainView.findViewById(R.id.product_price);
        ProductDescription = (EditText) myMainView.findViewById(R.id.product_description);

        ProductImage = (ImageView) myMainView.findViewById(R.id.product_image);

        AddProduct = (Button) myMainView.findViewById(R.id.add_product);

        ProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String category = parent.getItemAtPosition(position).toString();
                if(category.equals("Pimple")){
                    FirebaseDatabase.getInstance().getReference().child("Products").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int code = 1001;
                            int size = (int) dataSnapshot.getChildrenCount();
                            int finalcode = code+size;
                            final String Code = String.valueOf(finalcode);
                            Log.d("finalcode",Code);
                            String a = String.valueOf(size);
                            Log.d("child ",a);

                            AddProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //String category = ProductCategory.getText().toString();
                                    //String code = ProductCode.getText().toString();
                                    String name = ProductName.getText().toString();
                                    String price = ProductPrice.getText().toString();
                                    String description = ProductDescription.getText().toString();

                                    AddProducts(category,Code,name,price,description);
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else if(category.equals("Pigmentation")){
                    FirebaseDatabase.getInstance().getReference().child("Products").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int code = 2001;
                            int size = (int) dataSnapshot.getChildrenCount();
                            int finalcode = code+size;
                            final String Code = String.valueOf(finalcode);
                            Log.d("finalcode",Code);
                            String a = String.valueOf(size);
                            Log.d("child ",a);

                            AddProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String name = ProductName.getText().toString();
                                    String price = ProductPrice.getText().toString();
                                    String description = ProductDescription.getText().toString();

                                    AddProducts(category,Code,name,price,description);
                                }
                            });




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
                else if(category.equals("Pours")){
                    FirebaseDatabase.getInstance().getReference().child("Products").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int code = 3001;
                            int size = (int) dataSnapshot.getChildrenCount();
                            int finalcode = code+size;
                            final String Code = String.valueOf(finalcode);
                            Log.d("finalcode",Code);
                            String a = String.valueOf(size);
                            Log.d("child ",a);


                            AddProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //String category = ProductCategory.getText().toString();
                                    //String code = ProductCode.getText().toString();
                                    String name = ProductName.getText().toString();
                                    String price = ProductPrice.getText().toString();
                                    String description = ProductDescription.getText().toString();

                                    AddProducts(category,Code,name,price,description);
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else if(category.equals("Wrinkles and Anti-Aging")){
                    FirebaseDatabase.getInstance().getReference().child("Products").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int code = 4001;
                            int size = (int) dataSnapshot.getChildrenCount();
                            int finalcode = code+size;
                            final String Code = String.valueOf(finalcode);
                            Log.d("finalcode",Code);
                            String a = String.valueOf(size);
                            Log.d("child ",a);



                            AddProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //String category = ProductCategory.getText().toString();
                                    //String code = ProductCode.getText().toString();
                                    String name = ProductName.getText().toString();
                                    String price = ProductPrice.getText().toString();
                                    String description = ProductDescription.getText().toString();

                                    AddProducts(category,Code,name,price,description);
                                }
                            });





                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                /*AddProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String category = ProductCategory.getText().toString();
                        //String code = ProductCode.getText().toString();
                        String name = ProductName.getText().toString();
                        String price = ProductPrice.getText().toString();
                        String description = ProductDescription.getText().toString();

                        AddProducts(category,Code,name,price,description);
                    }
                });*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,PRODUCT_PIC);
            }
        });

         return myMainView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PRODUCT_PIC && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            ProductImage.setImageURI(ImageUri);
        }


    }






    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private void AddProducts(String category, final String code, final String name, String price, String description) {
        if(TextUtils.isEmpty(category)){
            Toast.makeText(getContext(),"Please write your Product's category", Toast.LENGTH_SHORT).show();

        }
       /* if(TextUtils.isEmpty(code)){
            Toast.makeText(AddProductActivity.this,"Please write your Product's code",Toast.LENGTH_SHORT).show();

        }*/
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getContext(),"Please write your Product's name", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(price)){
            Toast.makeText(getContext(),"Please write your Product's price", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(description)){
            Toast.makeText(getContext(),"Please write your Product's description", Toast.LENGTH_SHORT).show();

        }


        else{

            mStorageReference = FirebaseStorage.getInstance().getReference().child("Product_Images");

            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(category).child(code);
            mDatabaseReference.child("Product_Name").setValue(name);
            mDatabaseReference.child("Product_Price").setValue(price);
            mDatabaseReference.child("Product_Description").setValue(description);
            mDatabaseReference.child("Product_Image").setValue("Product Image").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    StorageReference filePath = mStorageReference.child(code + ".jpg");


                    filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();

                            mDatabaseReference.child("Product_Image").setValue(downloadUrl);

                            Toast.makeText(getContext(),"Your Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(getContext(),"Your Product Add Successfully", Toast.LENGTH_SHORT).show();
                    //ProductCategory.setText("");
                    ProductCategory.setActivated(true);
                    // ProductCode.setText("");
                    ProductName.setText("");
                    ProductPrice.setText("");
                    ProductDescription.setText("");
                    ProductImage.setImageResource(R.drawable.product);

                }
            });


        }

    }
}
