package com.example.asus.finalproject;

/**
 * Created by ASUS on 25.04.2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileTab1Posts extends Fragment {

    private DatabaseReference mDatabaseThisUserPosts;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_tab1_posts, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseThisUserPosts = mDatabase.child("UserPosts").child(mAuth.getCurrentUser().getUid());


        recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_posts_RV);

        mDatabaseThisUserPosts.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()); //// TODO: 24.02.2017 Bu ve sonraki iki komut anlaşılmadı araştır!!!
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //--------------ARAŞTIR---------------
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        return rootView;

    }// end onCreateView

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Posts, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, BlogViewHolder>(
                Posts.class,
                R.layout.profile_posts_blog_row,
                BlogViewHolder.class,
                mDatabaseThisUserPosts
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
    }


} // end All

/*var ref = firebase.database().ref("dinosaurs");
ref.orderByChild("height").equalTo(25).on("child_added", function(snapshot) {
  console.log(snapshot.key);
});*/