package com.chat.mark3;

public class DBREQUEST {
    String requests;
    String requestid;
    String fusername;
    String fphoto;

    public DBREQUEST() {
    }

    public DBREQUEST(String requests, String requestid, String fusername,String fphoto) {
        this.requests = requests;
        this.requestid = requestid;
        this.fusername = fusername;
        this.fphoto=fphoto;
    }

    public String getRequests() {
        return requests;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
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
}
