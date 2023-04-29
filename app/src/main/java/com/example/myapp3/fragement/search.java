package com.example.myapp3.fragement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp3.Adapter.searchAdapter;
import com.example.myapp3.R;
import com.example.myapp3.User;
import com.example.myapp3.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class search extends Fragment {
    FragmentSearchBinding binding;
    ArrayList<User> list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public search() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSearchBinding.inflate(inflater, container, false);
        list=new ArrayList<>();
        searchAdapter adapter=new searchAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.userRV.setLayoutManager(linearLayoutManager);
        binding.userRV.setAdapter(adapter);
        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());
                    if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}