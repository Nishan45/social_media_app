package com.example.myapp3.model;

public class Post {
    private String postId;
    private String postImage;
    private  String postedBy;
    private String description;
    private String postAt;
    private int likecount;
    private int dislikecount;
    private int commentcount;


    public Post(String postId, String postImage, String postedBy, String description, String postAt) {
        this.postId = postId;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.description = description;
        this.postAt = postAt;
    }

    public Post() {
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostAt() {
        return postAt;
    }

    public void setPostAt(String postAt) {
        this.postAt = postAt;
    }


    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getDislikecount() {
        return dislikecount;
    }

    public void setDislikecount(int dislikecount) {
        this.dislikecount = dislikecount;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }
}
