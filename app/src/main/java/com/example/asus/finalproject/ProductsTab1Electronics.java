package com.example.asus.finalproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductsTab1Electronics extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseElectronicPosts;
    private RecyclerView recyclerView;
    private boolean mProcessLike = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.products_tab1_electronics, container, false);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseElectronicPosts = mDatabase.child("ElectronicPosts");
        mDatabaseLike = mDatabase.child("Likes");


        recyclerView = (RecyclerView) rootView.findViewById(R.id.products_electronics_RV);

        mDatabaseElectronicPosts.keepSynced(true);
        mDatabase.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()); //// TODO: 24.02.2017 Bu ve sonraki iki komut anlaşılmadı araştır!!!
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //--------------ARAŞTIR---------------
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Posts, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, BlogViewHolder>(
                Posts.class,
                R.layout.products_electronics_blog_row,
                BlogViewHolder.class,
                mDatabaseElectronicPosts
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Posts model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setLikeBtn(post_key);
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getActivity(),model.getImage());
                viewHolder.setUserName(model.getUsername());

                viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessLike = true;
                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mProcessLike){
                                    if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                                        mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;
                                    }else{
                                        mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                        mProcessLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
