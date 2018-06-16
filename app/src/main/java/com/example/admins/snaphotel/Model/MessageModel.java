package com.example.admins.snaphotel.Model;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



public class MessageModel  {
    public String content;
    public String time;
    public String userName;
    public String photoUri;

    public MessageModel( ) {

    }

    public MessageModel(String content, String time, String userName, String photoUri) {
        this.content = content;
        this.time = time;
        this.userName = userName;
        this.photoUri = photoUri;
    }
}
