package com.example.myapp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapp3.databinding.ActivityMainBinding;
import com.example.myapp3.fragement.add;
import com.example.myapp3.fragement.home;
import com.example.myapp3.fragement.notification;
import com.example.myapp3.fragement.profile;
import com.example.myapp3.fragement.search;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new home());
        transaction.commit();

        binding.readablebottombar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                switch(i){
                    case 0:
                        transaction.replace(R.id.container,new home());
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        transaction.replace(R.id.container,new search());
                        Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        transaction.replace(R.id.container,new add());
                        Toast.makeText(MainActivity.this, "add", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        transaction.replace(R.id.container,new notification());
                        Toast.makeText(MainActivity.this, "notification", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        transaction.replace(R.id.container,new profile());
                        Toast.makeText(MainActivity.this, "profile", Toast.LENGTH_SHORT).show();
                        break;
                }
                transaction.commit();
            }
        });
    }
}