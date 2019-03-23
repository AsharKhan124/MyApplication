package com.google.firebase.samples.apps.mlkit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CareCenterActivity extends AppCompatActivity {

    private RecyclerView usersList; //myFriendList
    private DatabaseReference usersReference; //FriendsRefernce
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    String online_UserID;
    ProgressBar progressBar;



    //private View myMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_center);
        setupBottomNavigationView();



        mToolbar = findViewById(R.id.all_users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Care Center");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_carecenter);
       // getSupportActionBar().setIcon(R.drawable.ic_carecenter);

        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        //Sprite doublebounce = new DoubleBounce();
        Sprite foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);

        usersList = (RecyclerView) findViewById(R.id.all_users_list);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        usersList.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        online_UserID = mAuth.getCurrentUser().getUid();
        String current_emailId = mAuth.getCurrentUser().getEmail();

        if(current_emailId.equals("carecenter@gmail.com")) {
            usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
            //Offline
            usersReference.keepSynced(true);
        }
        else{
            usersReference = FirebaseDatabase.getInstance().getReference().child("Admin");
            //Offline
            usersReference.keepSynced(true);
        }


    }

    //BottomNavugationView Setup
    private void setupBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
       // bottomNavigationViewEx.setItemBackground(1,R.color.colorAccent);
        bottomNavigationViewEx.setIconSizeAt(2,30,30);
        bottomNavigationViewEx.enableShiftingMode(1,true);
        bottomNavigationViewEx.setCurrentItem(1).enableShiftingMode(1,true);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(CareCenterActivity.this,bottomNavigationViewEx);

        //updateNavigationBarState(R.id.ic_chat);

       // View view = bottomNavigationViewEx.findViewById(R.id.ic_chat);
        //view.performClick();


    }

   /* private void updateNavigationBarState(int actionId){
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

        FirebaseRecyclerAdapter<AllUsers,AllUsersViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(
                        AllUsers.class,
                        R.layout.all_users_display_layout,
                        AllUsersViewHolder.class,
                        usersReference
                ) {
                    @Override
                    protected void populateViewHolder(AllUsersViewHolder viewHolder, final AllUsers model, int position) {


                        final String list_user_id = getRef(position).getKey();



                        viewHolder.setUser_name(model.getUser_name());
                        viewHolder.setUser_status(model.getUser_status());
                        // viewHolder.setUser_image(getApplicationContext(),model.getUser_image());
                        viewHolder.setUser_thumb_image(CareCenterActivity.this,model.getUser_thumb_image());
                        progressBar.setVisibility(View.INVISIBLE);
                        viewHolder.setUser_online(model.getOnline());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { CharSequence options[] = new CharSequence[]{
                                    "Pre Fill Form",
                                    "Send Message"
                            };
                                AlertDialog.Builder builder = new AlertDialog.Builder(CareCenterActivity.this);
                                builder.setTitle("Select Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {

                                       /* if(position==0){
                                            Intent intent = new Intent(getContext(),ProfileActivity.class);
                                            intent.putExtra("visit_user_id",list_user_id);
                                            startActivity(intent);


                                        }*/
                                         if(position==0){

                                            Intent intent = new Intent(CareCenterActivity.this,PreFillActivity.class);
                                            intent.putExtra("visit_user_id",list_user_id);
                                            intent.putExtra("user_name",model.getUser_name());
                                            startActivity(intent);

                                        }
                                        else if(position==1){
                                            Intent intent = new Intent(CareCenterActivity.this,ChatActivity.class);
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
