package com.example.myapp3.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapp3.fragement.notification2;
import com.example.myapp3.fragement.requests;

public class notificationAdapter extends FragmentPagerAdapter {
    public notificationAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public notificationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new requests();
        }
        return new notification2();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        if(position==0){
            title="NOTIFICATION";
        }
        else if(position==1){
            title="REQUESTS";
        }
        return title;
    }
}
