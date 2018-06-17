package com.example.admins.snaphotel.Ultis;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.admins.snaphotel.Adapters.CustomInfoWindowAdapter;
import com.example.admins.snaphotel.Activities.TurnOnGPSActivity;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.admins.snaphotel.Maps.distance_matrix.DistanceInterface;
import com.example.admins.snaphotel.Maps.distance_matrix.DistanceResponse;

import com.example.admins.snaphotel.Maps.map_direction.DirectionHandler;
import com.example.admins.snaphotel.Maps.map_direction.DirectionResponse;
import com.example.admins.snaphotel.Maps.map_direction.RetrofitInstance;
import com.example.admins.snaphotel.Maps.map_direction.RetrofitService;
import com.example.admins.snaphotel.Maps.map_direction.RouteModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataHandle {
    public static List<LatLng> latLngs = new ArrayList<>();
    private static final String TAG = "DataHandle";
    public static final List<Polyline> polylines = new ArrayList<>();
    public static List<DistanceResponse.Rows> rows;

    public static List<HotelModel> hotelModels(final GoogleMap mMap, final Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("hotels");
        final List<HotelModel> list = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot);
                for (DataSnapshot hotel : dataSnapshot.getChildren()) {
                    HotelModel hotelModel = hotel.getValue(HotelModel.class);
                    Log.d(TAG, "onDataChange: " + hotelModel.kinhDo);
                    hotelModel.key = hotel.getKey();
                    list.add(hotelModel);
                    CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(context);
                    mMap.setInfoWindowAdapter(adapter);
                    LatLng sydney = new LatLng(hotelModel.viDo, hotelModel.kinhDo);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(sydney).title(hotelModel.nameHotel).snippet(String.valueOf(hotelModel.danhGiaTB) + "/" + hotelModel.gia);
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(hotelModel);
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_hotel));
                }
                Distance(list);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        final PolylineOptions polylineOptions = new PolylineOptions().color(Color.RED).width(16);
                        for (int i = 0; i < polylines.size(); i++) {
                            polylines.get(i).remove();
                        }
//                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 20);
//                        mMap.animateCamera(cameraUpdate);
                        RetrofitService retrofitService = RetrofitInstance.getInstance().create(RetrofitService.class);
                        Log.d(TAG, "onMarkerClick: " + TurnOnGPSActivity.currentLocation);
                        retrofitService.getDirection(String.valueOf(TurnOnGPSActivity.currentLocation.latitude)
                                        + "," + String.valueOf(TurnOnGPSActivity.currentLocation.longitude),
                                String.valueOf(marker.getPosition().latitude)
                                        + "," + String.valueOf(marker.getPosition().longitude),
                                "AIzaSyCPHUVwzFXx1bfLxZx9b8QYlZD_HMJza_0").enqueue(new Callback<DirectionResponse>() {
                            @Override
                            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                                RouteModel routeModel = DirectionHandler.getListRoute(response.body()).get(0);
                                Log.d(TAG, "onResponse: " + routeModel.duration);
                                Log.d(TAG, "onResponse: " + routeModel.distance);
//                                PolylineOptions polylineOptions = new PolylineOptions().color(Color.RED).width(16);

                                for (int i = 0; i < routeModel.points.size(); i++) {
                                    polylineOptions.add(routeModel.points.get(i));
                                    latLngs.add(routeModel.points.get(i));
                                }
                                Polyline polyline = mMap.addPolyline(polylineOptions);
                                polylines.add(polyline);
                                DirectionHandler.zoomRoute(mMap, latLngs);
                            }

                            @Override
                            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure: ");
                            }
                        });

                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return list;
    }

    public static void Distance(List<HotelModel> list) {
        LatLng currentLocation = TurnOnGPSActivity.currentLocation;
        String current = Double.toString(currentLocation.latitude) + "," + Double.toString(currentLocation.longitude);
        String key = "AIzaSyCPHUVwzFXx1bfLxZx9b8QYlZD_HMJza_0";
        String listLocation = "";

        for (int i = 0; i < list.size(); i++) {
            listLocation = listLocation + Double.toString(list.get(i).viDo) + "," + Double.toString(list.get(i).kinhDo);
            if (i + 1 < list.size()) {
                listLocation = listLocation + "|";
            }
        }
        DistanceInterface distanceInterface = RetrofitInstance.getInstance().create(DistanceInterface.class);
        distanceInterface.getDistance(current, listLocation, key).enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                Log.d(TAG, "onResponse: " + "0");
                rows = response.body().rows;
                if (rows.size() != 0) {
                    for (int i = 0; i < DataHandle.rows.get(0).elements.size(); i++) {
                        if (DataHandle.rows.get(0).elements.get(i).status.equals("OK")) {
                            Log.d(TAG, "onResponse: " + DataHandle.rows.get(0).elements.get(i).distance.value);
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + "response faile");
            }
        });
    }
}
