package com.google.firebase.samples.apps.mlkit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment
{

    private RecyclerView usersList; //myFriendList
    private DatabaseReference usersReference; //FriendsRefernce
    private FirebaseAuth mAuth;

    String online_UserID;


    private View myMainView;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      myMainView = inflater.inflate(R.layout.fragment_users, container, false);

      usersList = (RecyclerView) myMainView.findViewById(R.id.all_users_list);

      mAuth = FirebaseAuth.getInstance();
      online_UserID = mAuth.getCurrentUser().getUid();
      String current_emailId = mAuth.getCurrentUser().getEmail();

        if(current_emailId.equals("carecenter@gmail.com")) {
            usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        }
        else{
            usersReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        }
      usersList.setLayoutManager(new LinearLayoutManager(getContext()));



        return myMainView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>
                        (
                                AllUsers.class,
                                R.layout.all_users_display_layout,
                                AllUsersViewHolder.class,
                                usersReference
                        )
                {
                    @Override
                    protected void populateViewHolder(final AllUsersViewHolder viewHolder, final AllUsers model, int position)
                    {


                        final String list_user_id = getRef(position).getKey();



                        viewHolder.setUser_name(model.getUser_name());
                        viewHolder.setUser_status(model.getUser_status());
                        // viewHolder.setUser_image(getApplicationContext(),model.getUser_image());
                        viewHolder.setUser_thumb_image(getContext(),model.getUser_thumb_image());
                        viewHolder.setUser_online(model.getOnline());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { CharSequence options[] = new CharSequence[]{
                                    model.getUser_name() + "'s Profile","Pre Fill Form",
                                    "Send Message"
                            };
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Select Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {

                                        if(position==0){
                                            Intent intent = new Intent(getContext(),ProfileActivity.class);
                                            intent.putExtra("visit_user_id",list_user_id);
                                            startActivity(intent);


                                        }
                                        else if(position==1){

                                            Intent intent = new Intent(getContext(),PreFillActivity.class);
                                            intent.putExtra("visit_user_id",list_user_id);
                                            intent.putExtra("user_name",model.getUser_name());
                                            startActivity(intent);

                                        }
                                        else if(position==2){
                                            Intent intent = new Intent(getContext(),ChatActivity.class);
                                            intent.putExtra("visit_user_id",list_user_id);
                                            intent.putExtra("user_name",model.getUser_name());
                                            startActivity(intent);
                                        }

                                    }
                                });
                                builder.show();

                            }
                        });


                    }
                };

        usersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AllUsersViewHolder extends RecyclerView.ViewHolder
    {


        View mView;

        public AllUsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUser_name(String user_name)
        {
            TextView name = (TextView) mView.findViewById(R.id.allusers_username);
            name.setText(user_name);
        }

        public void setUser_status(String user_status){
            TextView status = (TextView) mView.findViewById(R.id.allusers_status);
            status.setText(user_status);
        }

      /*  public void setUser_image(Context context,String user_image){
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.all_users_profileimage);

            Picasso.with(context).load(user_image).into(image);
        }*/

        public void setUser_thumb_image(final Context context, final String user_thumb_image){
            final CircleImageView thumb_image = (CircleImageView) mView.findViewById(R.id.all_users_profileimage);

            //         Picasso.with(context).load(user_thumb_image).placeholder(R.drawable.profileimage).into(thumb_image);

            Picasso.with(context).load(user_thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.profileimage).into(thumb_image, new Callback() {
                @Override
                public void onSuccess()
                {

                }

                @Override
                public void onError()
                {
                    Picasso.with(context).load(user_thumb_image).placeholder(R.drawable.profileimage).into(thumb_image);

                }
            });

           // Picasso.get().load(user_thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profileimage).into(thumb_image);
        }

        public void setUser_online(boolean online_status) {
            //this.user_online = user_online;

            ImageView onlineStatusView = (ImageView) mView.findViewById(R.id.online_status);

            if(online_status == true)
            {
                onlineStatusView.setVisibility(View.VISIBLE);

            }

            else
                {
                    onlineStatusView.setVisibility(View.INVISIBLE);

                }


        }
    }
}
