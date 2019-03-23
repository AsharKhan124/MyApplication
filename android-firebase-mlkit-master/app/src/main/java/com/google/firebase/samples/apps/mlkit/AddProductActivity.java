package com.google.firebase.samples.apps.mlkit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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

public class AddProductActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

       // ProductCategory = (EditText) findViewById(R.id.product_category);
        ProductCategory = (Spinner) findViewById(R.id.product_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.category,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        ProductCategory.setAdapter(adapter);
        //ProductCategory.setActivated(true);
        //ProductCategory.setGravity(Gravity.CENTER);
        //ProductCode = (EditText) findViewById(R.id.product_code);
        ProductName = (EditText) findViewById(R.id.product_name);
        ProductPrice = (EditText) findViewById(R.id.product_price);
        ProductDescription = (EditText) findViewById(R.id.product_description);

        ProductImage = (ImageView) findViewById(R.id.product_image);
        
        AddProduct = (Button) findViewById(R.id.add_product);
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

                            /*ProductImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent galleryIntent = new Intent();
                                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    galleryIntent.setType("image/*");
                                    startActivityForResult(galleryIntent,PRODUCT_PIC);
                                }
                            });*/


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
        }); //
        
        /*.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String category = ProductCategory.getText().toString();
                String code = ProductCode.getText().toString();
                String name = ProductName.getText().toString();
                String price = ProductPrice.getText().toString();
                String description = ProductDescription.getText().toString();
                
                AddProducts(category,code,name,price,description);
            }
        });*/


        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,PRODUCT_PIC);
            }
        });
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PRODUCT_PIC && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            ProductImage.setImageURI(ImageUri);
        }


    }

    private void AddProducts(String category, final String code, final String name, String price, String description) {
        if(TextUtils.isEmpty(category)){
            Toast.makeText(AddProductActivity.this,"Please write your Product's category", Toast.LENGTH_SHORT).show();

        }
       /* if(TextUtils.isEmpty(code)){
            Toast.makeText(AddProductActivity.this,"Please write your Product's code",Toast.LENGTH_SHORT).show();

        }*/
        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddProductActivity.this,"Please write your Product's name", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(price)){
            Toast.makeText(AddProductActivity.this,"Please write your Product's price", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(description)){
            Toast.makeText(AddProductActivity.this,"Please write your Product's description", Toast.LENGTH_SHORT).show();

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

                    final StorageReference filePath = mStorageReference.child(code + ".jpg");


                   /* filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            final String downloadUrl = task.getResult().toString();

                            mDatabaseReference.child("Product_Image").setValue(downloadUrl);

                            Toast.makeText(AddProductActivity.this,"Your Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });*/

                    //add file on Firebase and got Download Link
                    filePath.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downUri = task.getResult();
                                String downloadUrl = downUri.toString();
                                mDatabaseReference.child("Product_Image").setValue(downloadUrl);
                                //Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                            }
                        }
                    });//////////////////

                    Toast.makeText(AddProductActivity.this,"Your Product Add Successfully", Toast.LENGTH_SHORT).show();
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
