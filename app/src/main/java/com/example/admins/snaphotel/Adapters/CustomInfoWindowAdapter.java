package com.example.admins.snaphotel.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.admins.snaphotel.Model.HotelModel;

import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Admins on 4/6/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "CustomInfoWindowAdapter";
    private Context context;
    public CustomInfoWindowAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.information_marker, null);
        RatingBar ratingBar = view.findViewById(R.id.rb_rating);
//        DataSnapshot dataSnapshot = (DataSnapshot) marker.getTag();
//        HotelModel hotelModel = dataSnapshot.getValue(HotelModel.class);
        HotelModel hotelModel = (HotelModel) marker.getTag();
        TextView tvPrice = view.findViewById(R.id.tv_price);
        ImageView ivWifi = view.findViewById(R.id.iv_wifi);
        ImageView ivThangMay = view.findViewById(R.id.iv_elevator);
        ImageView ivNongLanh = view.findViewById(R.id.iv_heater);
        ImageView ivDieuHoa = view.findViewById(R.id.iv_air_condition);
        ImageView ivTivi = view.findViewById(R.id.iv_tivi);
        ImageView ivTuLanh = view.findViewById(R.id.iv_tu_lanh);
        String giaDon = hotelModel.gia.substring(0, hotelModel.gia.indexOf("-"));
        String giaDoi = hotelModel.gia.substring(hotelModel.gia.indexOf("-")+1);
        Log.d(TAG, "loadData: " + giaDon + "   " + giaDoi);
        tvPrice.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDon))+" - " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDoi))+ " VNĐ");

        ratingBar.setRating(hotelModel.danhGiaTB);
        if (hotelModel.dieuHoa){
            ivDieuHoa.setVisibility(View.VISIBLE);
        }

        if (hotelModel.nongLanh){
            ivNongLanh.setVisibility(View.VISIBLE);
        }

        if (hotelModel.thangMay){
            ivThangMay.setVisibility(View.VISIBLE);
        }

        if (hotelModel.wifi){
            ivWifi.setVisibility(View.VISIBLE);
        }

        if (hotelModel.tivi){
            ivTivi.setVisibility(View.VISIBLE);
        }

        if (hotelModel.tulanh){
            ivTuLanh.setVisibility(View.VISIBLE);
        }
        return view;
    }
}