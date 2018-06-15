package com.example.admins.snaphotel.Model;

import java.util.List;

/**
 * Created by Admins on 3/1/2018.
 */

public class UserModel {
    public String name;
    public String uid;
    public String uri;
    public List<String> Huid;
    public List<String> Muid;
    public boolean register;

    public UserModel(String name, String uid, String uri, List<String> huid, boolean register) {
        this.name = name;
        this.uid = uid;
        this.uri = uri;
        Huid = huid;
        this.register = register;
    }

    public UserModel(String name, String uri) {
        this.name = name;


        this.uri = uri;

    }

    public UserModel(String name, String uid, String uri) {
        this.name = name;
        this.uid = uid;
        this.uri = uri;
    }

    public UserModel() {
    }

    public UserModel(String name) {
        this.name = name;


    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

