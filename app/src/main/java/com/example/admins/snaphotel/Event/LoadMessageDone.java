package com.example.admins.snaphotel.Event;

import com.example.admins.snaphotel.Model.ChatModel;

import java.util.List;

public class LoadMessageDone {
    public List<ChatModel> chatModels;

    public LoadMessageDone(List<ChatModel> chatModels) {
        this.chatModels = chatModels;
    }
}
