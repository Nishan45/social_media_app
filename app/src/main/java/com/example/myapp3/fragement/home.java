package com.example.myapp3.fragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myapp3.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapp3.Adapter.StoryAdapter;
import com.example.myapp3.Adapter.PostAdapter;
import com.example.myapp3.User;
import com.example.myapp3.databinding.FragmentHomeBinding;
import com.example.myapp3.model.StoryModel;
import com.example.myapp3.model.Post;
import com.example.myapp3.model.userStories;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class home extends Fragment {


    FragmentHomeBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    RecyclerView storyRv,dashboardRV;
    ImageView image;
    ArrayList<StoryModel> list;
    ArrayList<Post> list2;
    ImageView addstory;

    ProgressDialog dialog;

    public home() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        dialog=new ProgressDialog(getContext());
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false);

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        storyRv=view.findViewById(R.id.storyRV);
        image=view.findViewById(R.id.userId);

        ReadableBottomBar readableBottomBar=view.findViewById(R.id.readablebottombar);
        binding.userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readableBottomBar.selectItem(4);
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    Picasso.get().load(user.getProimg()).placeholder(R.drawable.ic_imagenotset).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list=new ArrayList<>();
        StoryAdapter adapter=new StoryAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);


        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StoryModel story = new StoryModel();
                        story.setStoryBy(dataSnapshot.getKey());
                        story.setStoryAt(dataSnapshot.child("postedBy").getValue(Long.class));
                        ArrayList<userStories> stories = new ArrayList<>();
                        for (DataSnapshot Story : dataSnapshot.child("userstories").getChildren()) {
                            userStories userstories = Story.getValue(userStories.class);
                            if(userstories.getStoryAt()+86400000>new Date().getTime()){
                            stories.add(userstories);
                            }


                        }
                        if(!stories.isEmpty()) {
                            story.setStories(stories);
                            list.add(story);

                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        dashboardRV=view.findViewById(R.id.dashboardRV);
        list2=new ArrayList<>();
        PostAdapter dadapter=new PostAdapter(list2, getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        dashboardRV.setLayoutManager(layoutManager);
        dashboardRV.setNestedScrollingEnabled(false);
        dashboardRV.setAdapter(dadapter);
        database.getReference().child("Post").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2.clear();
                ArrayList<Post>array=new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Post post=dataSnapshot.getValue(Post.class);
                    post.setPostId(dataSnapshot.getKey());

                    array.add(post);
                    if(array.size()>100){
                        database.getReference().child("Post").child(array.get(0).getPostId()).setValue(null);
                        array.remove(0);
                    }
                }
                Collections.reverse(array);
                for(Post post:array){
                    list2.add(post);
                    dadapter.notifyItemChanged(array.indexOf(post));
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addstory=view.findViewById(R.id.addStory);
        addstory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,17);
            }
        });



        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Story Uploading");
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Uri uri=data.getData();

            final StorageReference reference=storage.getReference().child("stories").child(auth.getUid()).child(new Date().getTime()+"");
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            StoryModel story=new StoryModel();
                            story.setStoryAt(new Date().getTime());
                            userStories stories=new userStories(uri.toString(),story.getStoryAt());

                            database.getReference().child("stories").child(auth.getUid()).child("postedBy").setValue(story.getStoryAt());
                            database.getReference().child("stories").child(auth.getUid()).child("userstories").push().setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Story Uploaded", Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    });
                }
            });
        }
    }
}