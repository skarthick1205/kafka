package com.example.springbatch.DTO;



public class UserCsv {

    private String userName;
    private String emailId;

    public UserCsv() {
    }

    public UserCsv(String userName, String emailId) {
        this.userName = userName;
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
