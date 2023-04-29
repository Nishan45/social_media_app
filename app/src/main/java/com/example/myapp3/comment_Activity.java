package com.example.myapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapp3.Adapter.commentAdapter;
import com.example.myapp3.databinding.ActivityCommentBinding;
import com.example.myapp3.model.Post;
import com.example.myapp3.model.comment_model;
import com.example.myapp3.model.friendsnotificationModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.animation.Positioning;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Date;

public class comment_Activity extends AppCompatActivity {
    ActivityCommentBinding binding;
    Intent intent;
    String postedby;
    String postid;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<comment_model>list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        intent=getIntent();
        postedby = intent.getStringExtra("postedby");
        postid = intent.getStringExtra("postid");
        database.getReference().child("Post").child(postid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post=snapshot.getValue(Post.class);
                Picasso.get().load(post.getPostImage()).placeholder(R.drawable.ic_imagenotset).into(binding.commentimage);
                binding.dislikecount.setText(post.getDislikecount()+"");
                binding.likecount.setText(post.getLikecount()+"");
                binding.postDiscription.setText(post.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference().child("Users").child(postedby).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Picasso.get().load(user.getProimg()).placeholder(R.drawable.ic_imagenotset).into(binding.userId);
                binding.postName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.postcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_model comment_model=new comment_model();
                comment_model.setComment(binding.addcomment.getText().toString());
                comment_model.setCommentId(auth.getUid());
                comment_model.setCommentedAt(new Date().getTime());

                friendsnotificationModel notification=new friendsnotificationModel();
                notification.setUser(postid);
                notification.setTime(new Date().getTime());
                notification.setType("comment");
                notification.setIsopen(false);
                notification.setNotifiedBy(auth.getUid());
                notification.setPostedBy(postedby);
                database.getReference().child("notifications").child(postedby).push().setValue(notification);

                database.getReference().child("Post").child(postid).child("comments").push().setValue(comment_model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Post").child(postid).child("commentcount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int commentcount=0;
                                if(snapshot.exists()){
                                    commentcount=snapshot.getValue(Integer.class);
                                }
                                database.getReference().child("Post").child(postid).child("commentcount").setValue(commentcount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        binding.addcomment.setText("");
                                        Toast.makeText(comment_Activity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });

        commentAdapter adapter=new commentAdapter(list,comment_Activity.this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(comment_Activity.this);
        binding.commentRv.setLayoutManager(linearLayoutManager);
        binding.commentRv.setAdapter(adapter);
        database.getReference().child("Post").child(postid).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    comment_model comment=dataSnapshot.getValue(comment_model.class);
                    list.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}