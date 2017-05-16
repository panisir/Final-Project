package com.example.asus.finalproject;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileTab2Liked extends Fragment {

    private DatabaseReference mDatabaseUserLikes;
    private DatabaseReference mDatabasePosts;
    private FirebaseAuth mAuth;
    private ArrayList<String> keyList;
    private RecyclerView recyclerView;
    private Integer i=0;
    private DatabaseReference mDatabaseLikedPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_tab2_liked, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUserLikes = FirebaseDatabase.getInstance().getReference().child("Likes").child(mAuth.getCurrentUser().getUid());
        mDatabasePosts = FirebaseDatabase.getInstance().getReference().child("Posts");
        getLikedPostsKeys();



        recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_liked_RV);


        mDatabaseUserLikes.keepSynced(true);
        mDatabasePosts.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //--------------ARAŞTIR---------------
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    } // end onCreate

    private void getLikedPostsKeys() {
        mDatabaseUserLikes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                keyList = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()){
                    keyList.add(String.valueOf(dsp.getKey()));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // end getLikedPostsKeys

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Posts, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, BlogViewHolder>(
                Posts.class,
                R.layout.profile_posts_blog_row,
                BlogViewHolder.class,
                mDatabasePosts
                //mDatabasePosts

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Posts model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getActivity(),model.getImage());
                viewHolder.setUserName(model.getUsername());
                //viewHolder.setDate(model.getDate());

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    } // end onStart



}// end All
