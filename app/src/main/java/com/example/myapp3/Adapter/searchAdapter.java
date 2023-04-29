package com.example.myapp3.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp3.User;
import com.example.myapp3.databinding.SearchUserSampleBinding;
import com.example.myapp3.R;
import com.example.myapp3.model.follow_model;
import com.example.myapp3.model.friendsnotificationModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.viewHolder>{
    ArrayList<User> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public searchAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_user_sample,parent,false);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        User user=list.get(position);
        Picasso.get().load(user.getProimg()).placeholder(R.drawable.ic_imagenotset).into(holder.binding.userId);
        holder.binding.name.setText(user.getName());


        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId()).child("followers").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    holder.binding.follow.setText("FOLLOWING...");
                    holder.binding.follow.setBackgroundColor(Color.YELLOW);
                    holder.binding.follow.setEnabled(false);

                }
                else{
                    holder.binding.follow.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            follow_model follow=new follow_model();


                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId()).child("followers").child(FirebaseAuth.getInstance().getUid()).setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId()).child("followerscount").setValue(user.getFollowerscount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Yow Followed"+" "+user.getName(), Toast.LENGTH_SHORT).show();
                                            holder.binding.follow.setText("FOLLOWING...");
                                            holder.binding.follow.setBackgroundColor(Color.YELLOW);
                                            holder.binding.follow.setEnabled(false);
                                            friendsnotificationModel notification=new friendsnotificationModel();
                                            notification.setUser(follow.getFollowedBy());
                                            notification.setTime(new Date().getTime());
                                            notification.setType("follow");
                                            notification.setIsopen(false);
                                            notification.setNotifiedBy(auth.getUid());
                                            notification.setPostedBy(user.getUserId());
                                            database.getReference().child("notifications").child(user.getUserId()).push().setValue(notification);

                                        }
                                    });
                                }
                            });


                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.binding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow_model follow = new follow_model();
                follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                follow.setFollowedAt(new Date().getTime());

            }
         });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        SearchUserSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
           super(itemView);
           binding=SearchUserSampleBinding.bind(itemView);
       }
   }
}
