package com.example.myapp3.Adapter;

import static com.example.myapp3.R.*;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp3.R;
import com.example.myapp3.User;
import com.example.myapp3.databinding.StoryDesignBinding;
import com.example.myapp3.model.StoryModel;
import com.example.myapp3.model.userStories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder>{
    ArrayList<StoryModel> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public StoryAdapter(ArrayList<StoryModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_design,parent,false);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        StoryModel story=list.get(position);

            userStories laststory = story.getStories().get(story.getStories().size() - 1);

            Picasso.get().load(laststory.getImage()).placeholder(drawable.ic_imagenotset).into(holder.binding.story);
            database.getReference().child("Users").child(story.getStoryBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProimg()).placeholder(drawable.ic_imagenotset).into(holder.binding.userId);

                        if (auth.getUid().equals(story.getStoryBy())) {
                            holder.binding.username.setText("Your Story");
                        } else {
                            holder.binding.username.setText(user.getName());
                        }

                        holder.binding.story.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<MyStory> myStories = new ArrayList<>();

                                for (userStories story : story.getStories()) {
                                    myStories.add(new MyStory(
                                            story.getImage()
                                    ));
                                }
                                new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                        .setStoriesList(myStories) // Required
                                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                        .setTitleText(user.getName()) // Default is Hidden
                                        .setSubtitleText("") // Default is Hidden
                                        .setTitleLogoUrl(user.getProimg()) // Default is Hidden
                                        .setStoryClickListeners(new StoryClickListeners() {
                                            @Override
                                            public void onDescriptionClickListener(int position) {
                                                //your action
                                            }

                                            @Override
                                            public void onTitleIconClickListener(int position) {
                                                //your action
                                            }
                                        }) // Optional Listeners
                                        .build() // Must be called before calling show method
                                        .show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        holder.binding.circularStatusView.setPortionsCount(story.getStories().size());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder{
        StoryDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=StoryDesignBinding.bind(itemView);


        }
    }
}
