package com.google.firebase.samples.apps.mlkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class PreFillActivity extends AppCompatActivity {

    private String messageRecieverId;
    private String messageRecieverName;

    private Toolbar mToolbar;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private String messageSenderId;

    private EditText UserName;
    private EditText UserEmail;
    private EditText UserNumber;
    private EditText Prod_Code_Price;
    private EditText Details;
    private Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_fill);

        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        messageRecieverId = getIntent().getExtras().get("visit_user_id").toString();
        messageRecieverName = getIntent().getExtras().get("user_name").toString();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" Pre Filled Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserName = (EditText) findViewById(R.id.user_name);
        UserEmail = (EditText) findViewById(R.id.user_email);
        UserNumber = (EditText) findViewById(R.id.user_number);
        Prod_Code_Price = (EditText) findViewById(R.id.product_code_price);
        Details = (EditText) findViewById(R.id.user_detail);
        Submit = (Button) findViewById(R.id.submit_button);



        Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String name = UserName.getText().toString();
                final String email = UserEmail.getText().toString();
                final String number = UserNumber.getText().toString();
                final String CodePrice = Prod_Code_Price.getText().toString();
                final String details = Details.getText().toString();


                submitInfo(name,email,number,CodePrice,details);
            }
        });




    }

    public void submitInfo(String name, String email, String number, String CodePrice, String details){
        if(TextUtils.isEmpty(name)){
            Toast.makeText(PreFillActivity.this,"Please write your name", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(PreFillActivity.this,"Please wirte your email", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(number)){
            Toast.makeText(PreFillActivity.this,"Please write your number", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(CodePrice)){
            Toast.makeText(PreFillActivity.this,"Please wirte product code and price", Toast.LENGTH_SHORT).show();
        }
        else{
            String userInfo = "Name: " +name+ "\n" +
                               "Email: " +email+ "\n" +
                                "Number: " +number+ "\n" +
                                 "Product Code and Price: " +CodePrice+ "\n" +
                                  "Details: " +details+ "\n";


            String message_sender_ref = "Messages/" + messageSenderId + "/" + messageRecieverId;

            String message_reciever_ref = "Messages/" + messageRecieverId + "/" + messageSenderId;

            DatabaseReference user_messageKey = rootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();

            String message_push_id = user_messageKey.getKey();

            Map messageTextBody = new HashMap();

            messageTextBody.put("message",userInfo);
            messageTextBody.put("seen",false);
            messageTextBody.put("type","text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from",messageSenderId);

            Map messageBodyDetails = new HashMap();

            messageBodyDetails.put(message_sender_ref + "/" + message_push_id ,messageTextBody);

            messageBodyDetails.put(message_reciever_ref + "/" + message_push_id ,messageTextBody);


            rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        Log.d("Info Log",databaseError.getMessage().toString());
                        Toast.makeText(PreFillActivity.this,"Your Information has been sent.", Toast.LENGTH_SHORT).show();

                    }

                    UserName.setText("");
                    UserEmail.setText("");
                    UserNumber.setText("");
                    Prod_Code_Price.setText("");
                    Details.setText("");


                }
            });


        }




    }
}
