package com.example.admins.snaphotel.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admins.snaphotel.Activities.MainActivity;
import com.example.admins.snaphotel.Adapters.FavouriteAdapter;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    RecyclerView rvFav;
    AVLoadingIndicatorView avLoading;
    TextView tvNoFavHotel;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    Context context;

    List<HotelModel> hotelModelList = new ArrayList<>();

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        rvFav = view.findViewById(R.id.rv_fav);
        avLoading = view.findViewById(R.id.iv_loading);
        tvNoFavHotel = view.findViewById(R.id.tv_no_fav_hotel);
        MainActivity.iv_filter.setVisibility(View.INVISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        context = getContext();

        firebaseDatabase.getReference("users")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("favList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            tvNoFavHotel.setVisibility(View.VISIBLE);
                            avLoading.hide();
                        }
                        for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {
                            String favHuid = favSnapshot.getValue(String.class);

                            firebaseDatabase.getReference("hotels").child(favHuid)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            HotelModel hotelModel = dataSnapshot.getValue(HotelModel.class);
                                            hotelModelList.add(hotelModel);

                                            rvFav.setAdapter(new FavouriteAdapter(hotelModelList));
                                            rvFav.setLayoutManager(new LinearLayoutManager(context));
                                            avLoading.hide();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }

    @Override
    public void onStop() {
        MainActivity.iv_filter.setVisibility(View.VISIBLE);
        super.onStop();

    }
}
