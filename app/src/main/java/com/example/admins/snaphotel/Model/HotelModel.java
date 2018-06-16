package com.example.admins.snaphotel.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 3/1/2018.
 */

public class HotelModel {
    public String nameHotel;
    public String address;
    public String phone;
    public String phone1;
    public double kinhDo;
    public double viDo;
    public float danhGiaTB;
    public String gia;
    public List<String> images = new ArrayList<>();
    public List<ReviewModel> reviewModels;
    public boolean wifi;
    public boolean dieuHoa;
    public boolean nongLanh;
    public boolean thangMay;
    public boolean tivi;
    public boolean tulanh;
    public String key;
//    public List<MessageModel> listMessage;

    public HotelModel() {
    }

    public HotelModel( String nameHotel, String address, String phone, double kinhDo, double viDo, float danhGiaTB, String gia, List<String> images, List<ReviewModel> reviewModels,
                       boolean wifi, boolean dieuHoa, boolean nongLanh, boolean thangMay, boolean tivi, boolean tulanh) {

        this.nameHotel = nameHotel;
        this.address = address;
        this.phone = phone;
        this.kinhDo = kinhDo;
        this.viDo = viDo;
        this.danhGiaTB = danhGiaTB;
        this.gia = gia;
        this.images = images;
        this.reviewModels = reviewModels;
        this.wifi = wifi;
        this.dieuHoa = dieuHoa;
        this.nongLanh = nongLanh;
        this.thangMay = thangMay;
        this.tulanh = tulanh;
        this.tivi = tivi;
    }

    public HotelModel(String s, String s1, String s2, double v, double v1, int i, String s3, ArrayList<String> strings, ArrayList<ReviewModel> reviewModels, boolean checked, boolean checked1, boolean checked2, boolean checked3) {
    }

    @Override
    public String toString() {
        return "HotelModel{" +
                "nameHotel='" + nameHotel + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", kinhDo=" + kinhDo +
                ", viDo=" + viDo +
                ", danhGiaTB=" + danhGiaTB +
                ", gia='" + gia + '\'' +
                ", danhGiaModels=" + reviewModels +
                ", wifi=" + wifi +
                ", dieuHoa=" + dieuHoa +
                ", nongLanh=" + nongLanh +
                ", thangMay=" + thangMay +
                '}';
    }

    public void addReview(ReviewModel reviewModel) {
        if (this.reviewModels == null) {
            this.reviewModels = new ArrayList<>();
        }
        this.reviewModels.add(reviewModel);
    }
}