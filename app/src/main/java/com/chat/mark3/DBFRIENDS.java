package com.chat.mark3;

public class DBFRIENDS {


    String fid;
    String fusername;
    String fphoto;
    String specialfriend;
    String fstatusname;
    String mute;
    public DBFRIENDS() {
    }
    public DBFRIENDS(String fid, String fusername,String fphoto,String specialfriend,String fstatusname,String mute) {
        this.fid = fid;
        this.fusername = fusername;
        this.fphoto=fphoto;
        this.specialfriend=specialfriend;
        this.fstatusname=fstatusname;
        this.mute=mute;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFusername() {
        return fusername;
    }

    public void setFusername(String fusername) {
        this.fusername = fusername;
    }

    public String getFphoto() {
        return fphoto;
    }

    public void setFphoto(String fphoto) {
        this.fphoto = fphoto;
    }

    public String getSpecialfriend() {
        return specialfriend;
    }

    public void setSpecialfriend(String specialfriend) {
        this.specialfriend = specialfriend;
    }

    public String getFstatusname() {
        return fstatusname;
    }

    public void setFstatusname(String fstatusname) {
        this.fstatusname = fstatusname;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }
}
