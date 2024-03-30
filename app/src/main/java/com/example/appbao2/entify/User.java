package com.example.appbao2.entify;

public class User {

    private String email;

    private String fullname;
    private String phone;

    public User(String email, String fullname, String phone) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
