package com.google.firebase.samples.apps.mlkit;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;

    private DatabaseReference UsersDatabaseReference;


    public MessageAdapter(List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout_of_users,parent,false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int position)
    {
        final String message_sender_id = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        final String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();
       /* String current_emailId = mAuth.getCurrentUser().getEmail();
        if(current_emailId.equals("carecenter@gmail.com")){*/
            UsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Data").child(fromUserId);


        UsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String userName = dataSnapshot.child("user_name").getValue().toString();
                String userThumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();

                if(!fromUserId.equals(message_sender_id)){

                Picasso.with(messageViewHolder.userProfileImage.getContext()).load(userThumbImage).placeholder(R.drawable.profileimage)
                        .into(messageViewHolder.userProfileImage);
               // Picasso.get().load(userThumbImage).placeholder(R.drawable.profileimage).into(messageViewHolder.userProfileImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(fromMessageType.equals("text")){
            messageViewHolder.messageText.setVisibility(View.GONE);
            messageViewHolder.messageText2.setVisibility(View.GONE);
            messageViewHolder.messagePicture.setVisibility(View.GONE);
            messageViewHolder.userProfileImage.setVisibility(View.GONE);

           // messageViewHolder.params = (LinearLayout.LayoutParams) messageViewHolder.messageText.getLayoutParams();

            if(fromUserId.equals(message_sender_id)){
                messageViewHolder.messageText.setVisibility(View.VISIBLE);
                messageViewHolder.messageText.setBackgroundResource(R.drawable.chat_text_background_two);
               // messageViewHolder.params.gravity = Gravity.END;
                messageViewHolder.messageText.setTextColor(Color.BLACK);
                messageViewHolder.messageText.setGravity(Gravity.RIGHT);
                messageViewHolder.messageText.setText(messages.getMessage());

            }

            else{
               // messageViewHolder.messageText.setVisibility(View.INVISIBLE);
                messageViewHolder.userProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.messageText2.setVisibility(View.VISIBLE);
                messageViewHolder.messageText2.setBackgroundResource(R.drawable.chat_text_background);
                //messageViewHolder.params.gravity = Gravity.START;
                messageViewHolder.messageText2.setTextColor(Color.WHITE);
                messageViewHolder.messageText2.setGravity(Gravity.LEFT);
                messageViewHolder.messageText2.setText(messages.getMessage());
            }

           // messageViewHolder.messageText.setText(messages.getMessage());


        }
        else{

            //messageViewHolder.messageText.setVisibility(View.INVISIBLE);
            //messageViewHolder.messageText.setPadding(0,0,0,0);
           // messageViewHolder.messageText2.setVisibility(View.INVISIBLE);
            //messageViewHolder.messageText2.setPadding(0,0,0,0);
            messageViewHolder.messageText.setVisibility(View.GONE);
            messageViewHolder.messageText2.setVisibility(View.GONE);
            messageViewHolder.userProfileImage.setVisibility(View.GONE);
            messageViewHolder.messagePicture.setVisibility(View.VISIBLE);

            Picasso.with(messageViewHolder.messagePicture.getContext()).load(messages.getMessage())
                    .placeholder(R.drawable.profileimage).into(messageViewHolder.messagePicture);
            //Picasso.get().load(messages.getMessage()).placeholder(R.drawable.profileimage).into(messageViewHolder.messagePicture);

        }


     /*   if(fromUserId.equals(message_sender_id)){
            messageViewHolder.messageText.setBackgroundResource(R.drawable.chat_text_background_two);
            messageViewHolder.messageText.setTextColor(Color.BLACK);
            messageViewHolder.messageText.setGravity(Gravity.RIGHT);
            // messageViewHolder.messageText.setla;
        }

        else{
            messageViewHolder.messageText.setBackgroundResource(R.drawable.chat_text_background);
            messageViewHolder.messageText.setTextColor(Color.WHITE);
            messageViewHolder.messageText.setGravity(Gravity.LEFT);
        }

        messageViewHolder.messageText.setText(messages.getMessage());*/




    }

    @Override
    public int getItemCount()
    {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageText;
        public TextView messageText2;
        public CircleImageView userProfileImage;
        public ImageView messagePicture;


        public MessageViewHolder(View view){
            super(view);

            messageText = (TextView) view.findViewById(R.id.chat_text);
            messageText2 = (TextView) view.findViewById(R.id.chat_text2);
            userProfileImage = (CircleImageView) view.findViewById(R.id.chat_profile_image);
            messagePicture = (ImageView) view.findViewById(R.id.chat_image_view);
        }
    }
}
