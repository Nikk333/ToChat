package com.chat.mark3;

public class DBMESSAGE {
    String messageid;
    String message;
    String senderid;
    String sendertime;
    String images;
    int react=-1;
    long timing;

    public DBMESSAGE() {

    }

    public DBMESSAGE(String message,String images, String senderid, String sendertime, long timing) {
        this.message = message;
        this.images=images;
        this.senderid = senderid;
        this.sendertime = sendertime;
        this.timing = timing;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getSendertime() {
        return sendertime;
    }

    public void setSendertime(String sendertime) {
        this.sendertime = sendertime;
    }

    public int getReact() {
        return react;
    }

    public void setReact(int react) {
        this.react = react;
    }

    public long getTiming() {
        return timing;
    }

    public void setTiming(long timing) {
        this.timing = timing;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
