package com.example.myapp3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp3.MainActivity;
import com.example.myapp3.R;
import com.example.myapp3.User;
import com.example.myapp3.comment_Activity;
import com.example.myapp3.databinding.DashboardDesignBinding;
import com.example.myapp3.model.Post;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{
    ArrayList<Post>list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public PostAdapter(ArrayList<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dashboard_design,parent,false);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post model=list.get(position);

        holder.binding.likecount.setText(model.getLikecount()+"");
        holder.binding.dislikecount.setText(model.getDislikecount()+"");
        holder.binding.commentcount.setText(model.getCommentcount()+"");
        Picasso.get().load(model.getPostImage()).placeholder(R.drawable.ic_imagenotset).into(holder.binding.postImage);
        holder.binding.postingTime.setText(model.getPostAt());
        holder.binding.postdiscription.setText(model.getDescription());


        database.getReference().child("Users").child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getProimg()).placeholder(R.drawable.ic_imagenotset).into(holder.binding.userId);

                if(auth.getUid().equals(model.getPostedBy())){
                    holder.binding.userName.setText("You");
                }
                else{
                    holder.binding.userName.setText(user.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Post").child(model.getPostId()).child("likes").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(context, "You Unliked It", Toast.LENGTH_SHORT).show();



                            database.getReference().child("Post").child(model.getPostId()).child("likecount").setValue(model.getLikecount()-1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    database.getReference().child("Post").child(model.getPostId()).child("likes").child(auth.getUid()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.like.setImageResource(R.drawable.ic_like);

                                        }
                                    });
                                }
                            });

                            }
                        else{

                            database.getReference().child("Post").child(model.getPostId()).child("likecount").setValue(model.getLikecount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    holder.binding.like.setImageResource(R.drawable.ic_likedone);

                                    Toast.makeText(v.getContext(), "You Liked This Post", Toast.LENGTH_SHORT).show();
                                    friendsnotificationModel notification=new friendsnotificationModel();
                                    notification.setUser(model.getPostId());
                                    notification.setTime(new Date().getTime());
                                    notification.setType("like");
                                    notification.setIsopen(false);
                                    notification.setNotifiedBy(auth.getUid());
                                    notification.setPostedBy(model.getPostedBy());
                                    database.getReference().child("notifications").child(model.getPostedBy()).push().setValue(notification);


                                    database.getReference().child("Post").child(model.getPostId()).child("likes").child(auth.getUid()).setValue(true);
                                    database.getReference().child("Post").child(model.getPostId()).child("dislikes").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()) {
                                                database.getReference().child("Post").child(model.getPostId()).child("dislikecount").setValue(model.getDislikecount()- 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        holder.binding.dislike.setImageResource(R.drawable.ic_dislike);
                                                        database.getReference().child("Post").child(model.getPostId()).child("dislikes").child(auth.getUid()).setValue(null);

                                                    }
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

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

            }
        });

        holder.binding.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Post").child(model.getPostId()).child("dislikes").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(context, "You Undisliked It", Toast.LENGTH_SHORT).show();



                            database.getReference().child("Post").child(model.getPostId()).child("dislikecount").setValue(model.getDislikecount()-1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    database.getReference().child("Post").child(model.getPostId()).child("dislikes").child(auth.getUid()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.dislike.setImageResource(R.drawable.ic_dislike);

                                        }
                                    });
                                }
                            });
                        }
                        else{

                            database.getReference().child("Post").child(model.getPostId()).child("dislikecount").setValue(model.getDislikecount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(v.getContext(), "You Disliked This Post", Toast.LENGTH_SHORT).show();

                                    holder.binding.dislike.setImageResource(R.drawable.ic_dislikedone);

                                    friendsnotificationModel notification=new friendsnotificationModel();
                                    notification.setUser(model.getPostId());
                                    notification.setTime(new Date().getTime());
                                    notification.setType("dislike");
                                    notification.setIsopen(false);
                                    notification.setNotifiedBy(auth.getUid());
                                    notification.setPostedBy(model.getPostedBy());
                                    database.getReference().child("notifications").child(model.getPostedBy()).push().setValue(notification);

                                    database.getReference().child("Post").child(model.getPostId()).child("dislikes").child(auth.getUid()).setValue(true);
                                    database.getReference().child("Post").child(model.getPostId()).child("likes").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                database.getReference().child("Post").child(model.getPostId()).child("likecount").setValue(model.getLikecount()-1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        holder.binding.like.setImageResource(R.drawable.ic_like);
                                                        database.getReference().child("Post").child(model.getPostId()).child("likes").child(auth.getUid()).setValue(null);


                                                    }
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

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


            }
        });
        database.getReference().child("Post").child(model.getPostId()).child("likes").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.binding.like.setImageResource(R.drawable.ic_likedone);
                }
                else{
                    holder.binding.like.setImageResource(R.drawable.ic_like);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference().child("Post").child(model.getPostId()).child("dislikes").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.binding.dislike.setImageResource(R.drawable.ic_dislikedone);
                }
                else{
                    holder.binding.dislike.setImageResource(R.drawable.ic_dislike);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, comment_Activity.class);
                intent.putExtra("postedby",model.getPostedBy());
                intent.putExtra("postid",model.getPostId());
                context.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        DashboardDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=DashboardDesignBinding.bind(itemView);
        }
    };
}
