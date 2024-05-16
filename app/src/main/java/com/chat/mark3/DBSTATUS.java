package com.chat.mark3;

public class DBSTATUS {
    String uid;
    String status;
    String userphoto;
    String username;
    String statusname;
    public DBSTATUS() {
    }
    public DBSTATUS(String uid, String status, String userphoto, String username,String statusname) {
        this.uid = uid;
        this.status = status;
        this.userphoto = userphoto;
        this.username = username;
        this.statusname=statusname;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }
}
