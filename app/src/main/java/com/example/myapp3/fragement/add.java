package com.example.myapp3.fragement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapp3.R;
import com.example.myapp3.User;
import com.example.myapp3.databinding.FragmentAddBinding;
import com.example.myapp3.model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;


public class add extends Fragment {

   FragmentAddBinding binding;
   FirebaseAuth auth;
   FirebaseDatabase database;
   FirebaseStorage storage;
   ProgressDialog dialog;
    Uri uri;
    public add() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddBinding.inflate(inflater, container, false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        binding.posttext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String discription=binding.posttext.getText().toString();
                if(!discription.isEmpty()){
                    binding.post.setEnabled(true);
                    binding.post.setBackgroundColor(Color.GREEN);

                }
                else{
                    binding.post.setBackgroundColor(Color.parseColor("#BFC7E0"));
                    binding.post.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    Picasso.get().load(user.getProimg()).placeholder(R.drawable.ic_imagenotset).into(binding.userId);
                    binding.username.setText(user.getName());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,13);
            }
        });
        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                final StorageReference reference= storage.getReference().child("Post").child(auth.getUid()).child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               Date date= Calendar.getInstance().getTime();
                               @SuppressLint("SimpleDateFormat") SimpleDateFormat df=new SimpleDateFormat("dd-MM-yy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat time=new SimpleDateFormat("HH:mm");

                               String ap=(date.getHours()>=12?"PM":"AM");

                               String formattedDate=df.format(date);
                               String formattedTime=time.format(date);
                               Post post=new Post();

                               post.setPostImage(uri.toString());
                               post.setDescription(binding.posttext.getText().toString());
                               post.setPostAt(formattedDate+" "+"At"+" "+formattedTime+" "+ap);
                               post.setPostedBy(auth.getUid());

                               database.getReference().child("Post").push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       dialog.dismiss();
                                       Toast.makeText(getContext(), "Post Uploaded", Toast.LENGTH_SHORT).show();

                                   }
                               });
                               database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       User user=snapshot.getValue(User.class);
                                       database.getReference().child("Users").child(auth.getUid()).child("photocount").setValue(user.getPhotocount()+1);
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });

                           }
                       });
                    }
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            uri=data.getData();
            binding.postimage.setImageURI(uri);
            binding.post.setEnabled(true);
            binding.post.setBackgroundColor(Color.GREEN);
        }
        else{
            binding.post.setBackgroundColor(Color.parseColor("#BFC7E0"));
            binding.post.setEnabled(false);
        }

    }
}