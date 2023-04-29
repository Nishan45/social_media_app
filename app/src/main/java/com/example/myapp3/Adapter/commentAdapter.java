package com.example.myapp3.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapp3.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.myapp3.User;
import com.example.myapp3.databinding.CommentSampleBinding;

import com.example.myapp3.model.Post;
import com.example.myapp3.model.comment_model;
import com.example.myapp3.model.friendsnotificationModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class commentAdapter  extends RecyclerView.Adapter<commentAdapter.viewHolder>{
    ArrayList<comment_model> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;



    public commentAdapter(ArrayList<comment_model> list, Context context) {
        this.list = list;
        this.context = context;
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        comment_model comment=list.get(position);
        String time = TimeAgo.using(comment.getCommentedAt());
        holder.binding.time.setText(time);
        database.getReference().child("Users").child(comment.getCommentId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Picasso.get().load(user.getProimg()).placeholder(R.drawable.ic_imagenotset).into(holder.binding.userId);
                if(auth.getUid().equals(comment.getCommentId())){
                    holder.binding.comment.setText(Html.fromHtml("<b>"+"You"+"</b>"+"    "+comment.getComment()));
                }
                else {
                    holder.binding.comment.setText(Html.fromHtml("<b>" + user.getName() + "</b>" + "    " + comment.getComment()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CommentSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CommentSampleBinding.bind(itemView);
        }
    }

}
