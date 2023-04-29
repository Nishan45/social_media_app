package com.example.myapp3.model;

public class comment_model {
    private String commentId;
    private String comment;
    private long commentedAt;

    public comment_model(String commentId, String comment, long commentedAt) {
        this.commentId = commentId;
        this.comment = comment;
        this.commentedAt = commentedAt;
    }

    public comment_model() {
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(long commentedAt) {
        this.commentedAt = commentedAt;
    }
}
