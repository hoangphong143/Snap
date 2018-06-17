package com.example.admins.snaphotel.Model;

/**
 * Created by Admins on 6/13/2018.
 */

public class ChatModel {
    public int hotelAva;
    public String UserAva;
    public String name;
    public String date;
    public String keyHotel;
    public String keyListMess;

    public ChatModel(int hotelAva, String userAva, String name, String date, String keyHotel, String keyListMess) {
        this.hotelAva = hotelAva;
        UserAva = userAva;
        this.name = name;
        this.date = date;
        this.keyHotel = keyHotel;
        this.keyListMess = keyListMess;
    }
}
