package com.example.admins.snaphotel.Model;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



public class MessageModel  {
    public String userName;
    public String content;
    public String time;
    public String uId;
    public String Uri;

    public MessageModel( ) {

    }

    public MessageModel(String userName, String content, String time, String uId, String uri) {
        this.userName = userName;
        this.content = content;
        this.time = time;
        this.uId = uId;
        Uri = uri;
    }
}
