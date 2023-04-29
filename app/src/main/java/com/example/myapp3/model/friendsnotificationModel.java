package com.example.myapp3.model;

public class friendsnotificationModel {
    private String user;
    private String type;
    private String notifiedBy;

    private String postedBy;

    private String key;

    private long time;
    private boolean isopen;

    public friendsnotificationModel() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isIsopen() {
        return isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

    public String getNotifiedBy() {
        return notifiedBy;
    }

    public void setNotifiedBy(String notifiedBy) {
        this.notifiedBy = notifiedBy;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
