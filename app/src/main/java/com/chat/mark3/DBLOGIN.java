package com.chat.mark3;

public class DBLOGIN {
    String UID,EMAIL,PASSWORD,USERNAME,PROFILEIMG,STATUSNAME,STATUSONOFF,BIO,NOTIFICATIONTOKEN;
    public DBLOGIN() {
    }
    public DBLOGIN(String UID, String EMAIL, String PASSWORD, String USERNAME, String PROFILEIMG, String STATUSNAME, String STATUSONOFF, String BIO,String NOTIFICATIONTOKEN) {

        this.UID = UID;
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
        this.USERNAME = USERNAME;
        this.PROFILEIMG = PROFILEIMG;
        this.STATUSNAME = STATUSNAME;
        this.STATUSONOFF = STATUSONOFF;
        this.BIO = BIO;
        this.NOTIFICATIONTOKEN=NOTIFICATIONTOKEN;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPROFILEIMG() {
        return PROFILEIMG;
    }

    public void setPROFILEIMG(String PROFILEIMG) {
        this.PROFILEIMG = PROFILEIMG;
    }

    public String getSTATUSNAME() {
        return STATUSNAME;
    }

    public void setSTATUSNAME(String STATUSNAME) {
        this.STATUSNAME = STATUSNAME;
    }

    public String getSTATUSONOFF() {
        return STATUSONOFF;
    }

    public void setSTATUSONOFF(String STATUSONOFF) {
        this.STATUSONOFF = STATUSONOFF;
    }

    public String getBIO() {
        return BIO;
    }

    public void setBIO(String BIO) {
        this.BIO = BIO;
    }

    public String getNOTIFICATIONTOKEN() {
        return NOTIFICATIONTOKEN;
    }

    public void setNOTIFICATIONTOKEN(String NOTIFICATIONTOKEN) {
        this.NOTIFICATIONTOKEN = NOTIFICATIONTOKEN;
    }
}
