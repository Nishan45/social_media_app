package com.example.myapp3;

import android.graphics.Color;

public class User {
    private String name,email,password;
    private String coverphoto;
    private String userId;
    private int followerscount;
    private int photocount;
    private String proimg;



    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getCoverphoto() {
        return coverphoto;
    }

    public void setCoverphoto(String coverphoto) {
        this.coverphoto = coverphoto;
    }

    public String getProimg() {
        return proimg;
    }

    public void setProimg(String proimg) {
        this.proimg = proimg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getFollowerscount() {
        return followerscount;
    }

    public void setFollowerscount(int followerscount) {
        this.followerscount = followerscount;
    }


    public int getPhotocount() {
        return photocount;
    }

    public void setPhotocount(int photocount) {
        this.photocount = photocount;
    }
}
