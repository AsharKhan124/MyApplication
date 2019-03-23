package com.google.firebase.samples.apps.mlkit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//import android.widget.Toolbar;

public class ChatActivity extends AppCompatActivity {

    private String messageRecieverId;
    private String messageRecieverName;

    private Toolbar ChatToolbar;

    private TextView userNameTitle;
    private TextView userLastSeen;
    private CircleImageView userChatProfileImage;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private String messageSenderId;

    private ImageButton SendMessageButton;
    private ImageButton SendImageButton;
    private EditText InputMessageText;

    private RecyclerView userChatList;

    private final List<Messages> messagesList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    private MessageAdapter messageAdapter;

    private static int Gallery_Pick = 1;

    private StorageReference MessageImageStorageRef;

    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        messageRecieverId = getIntent().getExtras().get("visit_user_id").toString();
        messageRecieverName = getIntent().getExtras().get("user_name").toString();

        MessageImageStorageRef = FirebaseStorage.getInstance().getReference().child("Messages_Pictures");


       // Toast.makeText(ChatActivity.this,messageRecieverId,Toast.LENGTH_LONG).show();
       // Toast.makeText(ChatActivity.this,messageReieverName,Toast.LENGTH_LONG).show();

        ChatToolbar = (Toolbar) findViewById(R.id.chat_bar_layout);
        setSupportActionBar(ChatToolbar);

        loadingbar = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar,null);

        actionBar.setCustomView(action_bar_view);


        userNameTitle = (TextView) findViewById(R.id.custom_profilename);
        //userLastSeen = (TextView) findViewById(R.id.custom_lastseen);
        userChatProfileImage = (CircleImageView) findViewById(R.id.custom_profileimage);
        SendMessageButton = (ImageButton) findViewById(R.id.send_message);
        SendImageButton = (ImageButton) findViewById(R.id.select_image);
        InputMessageText = (EditText) findViewById(R.id.input_message);

        messageAdapter = new MessageAdapter(messagesList);

        userChatList = (RecyclerView) findViewById(R.id.chat_list_of_users);

        linearLayoutManager = new LinearLayoutManager(this);

        userChatList.setHasFixedSize(true);

        userChatList.setLayoutManager(linearLayoutManager);

        userChatList.setAdapter(messageAdapter);
        
        FetchMessages();





        userNameTitle.setText(messageRecieverName);

        String current_emailId = mAuth.getCurrentUser().getEmail();

        if(current_emailId.equals("carecenter@gmail.com")){
            rootRef.child("Users").child(messageRecieverId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                   // final String online = dataSnapshot.child("online").getValue().toString();
                    final String userthumb = dataSnapshot.child("user_thumb_image").getValue().toString();

                    Picasso.with(ChatActivity.this).load(userthumb).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.profileimage).into(userChatProfileImage, new Callback() {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError()
                        {
                            Picasso.with(ChatActivity.this).load(userthumb).placeholder(R.drawable.profileimage).into(userChatProfileImage);

                        }
                    });
                  // Picasso.get().load(userthumb).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profileimage).into(userChatProfileImage);

                   /* if(online.equals("true")){
                        userLastSeen.setText("Online");
                    }
                    else{
                        userLastSeen.setText(online);
                    }*/

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }


        ///

        else{

            rootRef.child("Admin").child(messageRecieverId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    final String online = dataSnapshot.child("online").getValue().toString();
                    final String userthumb = dataSnapshot.child("user_thumb_image").getValue().toString();

                   Picasso.with(ChatActivity.this).load(userthumb).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.profileimage).into(userChatProfileImage, new Callback() {

                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError()
                        {
                            Picasso.with(ChatActivity.this).load(userthumb).placeholder(R.drawable.profileimage).into(userChatProfileImage);
                            //Picasso.get().load(userthumb).placeholder(R.drawable.profileimage).into(userChatProfileImage);

                        }
                    });
                // Picasso.get().load(userthumb).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profileimage).into(userChatProfileImage);



                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });


        }

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        SendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode == RESULT_OK && data!=null){

            loadingbar.setTitle("Sending Chat Image");
            loadingbar.setMessage("Please wait, while your chat message is sending...");
            loadingbar.show();

            Uri ImageUri = data.getData();

            final String message_sender_ref = "Messages/" + messageSenderId + "/" + messageRecieverId;

            final String message_reciever_ref = "Messages/" + messageRecieverId + "/" + messageSenderId;

            DatabaseReference user_messageKey = rootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();

            final String message_push_id = user_messageKey.getKey();

            final StorageReference filePath = MessageImageStorageRef.child(message_push_id + ".jpg");

           /* filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {

                    if(task.isSuccessful())
                    {

                        final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();

                        Map messageTextBody = new HashMap();

                        messageTextBody.put("message",downloadUrl);
                        messageTextBody.put("seen",false);
                        messageTextBody.put("type","image");
                        messageTextBody.put("time", ServerValue.TIMESTAMP);
                        messageTextBody.put("from",messageSenderId);

                        Map messageBodyDetails = new HashMap();

                        messageBodyDetails.put(message_sender_ref + "/" + message_push_id ,messageTextBody);

                        messageBodyDetails.put(message_reciever_ref + "/" + message_push_id ,messageTextBody);

                        rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                            {
                                if(databaseError!=null)
                                {
                                    Log.d("Chat_Log",databaseError.getMessage().toString());
                                }
                                InputMessageText.setText("");
                                loadingbar.dismiss();

                            }
                        });


                        Toast.makeText(ChatActivity.this,"Picture Sent Successfully.", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                    else
                        {
                            Toast.makeText(ChatActivity.this,"Picture not Sent.Try Again.", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }

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

                        //Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                       // final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();

                        Map messageTextBody = new HashMap();

                        messageTextBody.put("message",downloadUrl);
                        messageTextBody.put("seen",false);
                        messageTextBody.put("type","image");
                        messageTextBody.put("time", ServerValue.TIMESTAMP);
                        messageTextBody.put("from",messageSenderId);

                        Map messageBodyDetails = new HashMap();

                        messageBodyDetails.put(message_sender_ref + "/" + message_push_id ,messageTextBody);

                        messageBodyDetails.put(message_reciever_ref + "/" + message_push_id ,messageTextBody);

                        rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                            {
                                if(databaseError!=null)
                                {
                                    Log.d("Chat_Log",databaseError.getMessage().toString());
                                }
                                InputMessageText.setText("");
                                loadingbar.dismiss();

                            }
                        });


                        Toast.makeText(ChatActivity.this,"Picture Sent Successfully.", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();


                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this,"Picture not Sent.Try Again.", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                }
            });//////////////////



        }
    }

    private void FetchMessages() {

        rootRef.child("Messages").child(messageSenderId).child(messageRecieverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Messages messages = dataSnapshot.getValue(Messages.class);

                messagesList.add(messages);

                messageAdapter.notifyDataSetChanged();

                userChatList.smoothScrollToPosition(userChatList.getAdapter().getItemCount());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void SendMessage() {


        String messageText = InputMessageText.getText().toString();

        if(TextUtils.isEmpty(messageText)){
            Toast.makeText(ChatActivity.this,"Please Write Your Message..", Toast.LENGTH_SHORT).show();
        }
        else {
            String message_sender_ref = "Messages/" + messageSenderId + "/" + messageRecieverId;

            String message_reciever_ref = "Messages/" + messageRecieverId + "/" + messageSenderId;

            DatabaseReference user_messageKey = rootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();

            String message_push_id = user_messageKey.getKey();

            Map messageTextBody = new HashMap();

            messageTextBody.put("message",messageText);
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

                        Log.d("Chat Log",databaseError.getMessage().toString());

                    }
                    InputMessageText.setText("");

                }
            });



        }
    }
}
