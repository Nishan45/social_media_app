package com.example.myapp3.fragement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.adapters.AdapterViewBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp3.Adapter.notification2Adapter;
import com.example.myapp3.R;
import com.example.myapp3.model.friendsnotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class notification2 extends Fragment {

    private android.view.Menu Menu;
    private Menu mainmenu=Menu;
    RecyclerView recyclerView;
    ArrayList<friendsnotificationModel> list;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notification2, container, false);
        recyclerView=view.findViewById(R.id.notificationRV);
        list=new ArrayList<>();
        notification2Adapter adapter=new notification2Adapter(list,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        database.getReference().child("notifications").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    ArrayList<friendsnotificationModel> notify=new ArrayList<>();
                    for(DataSnapshot users: snapshot.getChildren()){
                        friendsnotificationModel notification=users.getValue(friendsnotificationModel.class);
                        if(!notification.getNotifiedBy().equals(auth.getUid())) {
                            notification.setKey(users.getKey());
                            notify.add(notification);
                        }

                    }
                    Collections.reverse(notify);
                    if(!notify.isEmpty()) {
                        for (friendsnotificationModel notification : notify) {
                            list.add(notification);
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        mainmenu=menu;
        inflater.inflate(R.menu.menu_delete,mainmenu);
        super.onCreateOptionsMenu(menu, inflater);
    }




}