package com.example.asus.finalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.asus.finalproject.R.id.btnLike;
import static com.example.asus.finalproject.R.id.main_content;

/**
 * Created by ASUS on 26.04.2017.
 */
public class BlogViewHolder extends RecyclerView.ViewHolder {

    View mView;
    ImageButton btnLike;
    DatabaseReference mDatabaseLike;
    FirebaseAuth mAuth;
    public BlogViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        btnLike = (ImageButton) mView.findViewById(R.id.btnLike);
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth = FirebaseAuth.getInstance();

        mDatabaseLike.keepSynced(true);
    }

    public void setImage(Context ctx, String image){
        ImageView blog_image = (ImageView) mView.findViewById(R.id.post_image);
        Picasso.with(ctx).load(image).into(blog_image);
    }

    public void setUserName(String userName){
        TextView post_userName = (TextView) mView.findViewById(R.id.tvPost_userName);
        post_userName.setText(userName);
    }

    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.post_title);
        post_title.setText(title);
    }

    public void setDesc(String desc){
        TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
        post_desc.setText(desc);
    }

    public void setLikeBtn(final String post_key){
        mDatabaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                    btnLike.setImageResource(R.drawable.ic_favorite_black_48px);
                }else {
                    btnLike.setImageResource(R.drawable.ic_favorite_border_black_48px);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
