package com.example.myapp3.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp3.R;
import com.example.myapp3.User;
import com.example.myapp3.comment_Activity;
import com.example.myapp3.databinding.Notification2sampleBinding;
import com.example.myapp3.model.Post;
import com.example.myapp3.model.friendsnotificationModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class notification2Adapter extends RecyclerView.Adapter<notification2Adapter.viewHolder> {
    ArrayList<friendsnotificationModel> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;



    public notification2Adapter(ArrayList<friendsnotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification2sample,parent,false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        friendsnotificationModel notification=list.get(position);
        String time=TimeAgo.using(notification.getTime());
        holder.binding.time.setText(time);

        database.getReference().child("Users").child(notification.getNotifiedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                Picasso.get().load(user.getProimg()).placeholder(R.drawable.ic_imagenotset).into(holder.binding.userId);
                if (notification.getType().equals("like")) {
                    holder.binding.message.setText(Html.fromHtml("<b>" + user.getName() + "</b>" + " " + "liked your post"));
                } else if (notification.getType().equals("dislike")) {
                    holder.binding.message.setText(Html.fromHtml("<b>" + user.getName() + "</b>" + " " + "disliked your post"));
                }
                else if (notification.getType().equals("comment")) {
                    holder.binding.message.setText(Html.fromHtml("<b>" + user.getName() + "</b>" + " " + "commented on your post"));
                }
                else if (notification.getType().equals("follow")) {
                    holder.binding.message.setText(Html.fromHtml("<b>" + user.getName() + "</b>" + " " + "start following you"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!notification.getType().equals("follow")){

                    Intent intent=new Intent(context, comment_Activity.class);

                    intent.putExtra("postedby",notification.getPostedBy());
                    intent.putExtra("postid",notification.getUser());
                    context.startActivity(intent);

                }

            }
        });
        holder.binding.tap.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext()).setTitle("Delete").setMessage("Are you sure want to delete?").setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                database.getReference().child("notifications").child(auth.getUid()).child(notification.getKey()).setValue(null);


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

                return true;
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        Notification2sampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=Notification2sampleBinding.bind(itemView);
        }
    }
}
