package com.example.chahat.anotode;

/**
 * Created by chahat on 5/10/16.
 */
public class User {

    String username, email,password,picture_url;

    public User(String username,String email,String password,String picture)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture_url = picture;
    }

    public User(String email,String password)
    {
        this.email=email;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
