package com.google.firebase.samples.apps.mlkit;

/**
 * Created by Hp on 1/28/2019.
 */

public class AllUsers {

    public String user_name;
    public String user_image;
    public String user_status;
    public String user_thumb_image;
    public boolean  online ;

    public AllUsers() {
    }

    public AllUsers(String user_name, String user_image, String user_status, String user_thumb_image, boolean online) {
        this.user_name = user_name;
        this.user_image = user_image;
        this.user_status = user_status;
        this.user_thumb_image = user_thumb_image;
        this.online = online;
    }


    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUser_thumb_image() {
        return user_thumb_image;
    }

    public void setUser_thumb_image(String user_thumb_image) {
        this.user_thumb_image = user_thumb_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
}
