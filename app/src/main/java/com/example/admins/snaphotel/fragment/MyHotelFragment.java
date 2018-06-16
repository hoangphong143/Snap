package com.example.admins.snaphotel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admins.snaphotel.Activities.Adapters.HotelAdapter;
import com.example.admins.snaphotel.Activities.MainActivity;
import com.example.admins.snaphotel.Model.HotelModel;

import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyHotelFragment extends Fragment {
    private static final String TAG = MyHotelFragment.class.toString();
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    List<HotelModel> hotelModelList = new ArrayList<>();
    HotelAdapter hotelAdapter;
    RecyclerView rvHotel;
    ImageView ivAvata;
    TextView tvName;
    TextView tvNoPost;
    AVLoadingIndicatorView avLoadingIndicatorView;

    public MyHotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_hotel, container, false);
        rvHotel = view.findViewById(R.id.rv_myHotel);
        ivAvata = view.findViewById(R.id.iv_avatar);
        tvName = view.findViewById(R.id.tv_name);
        tvNoPost = view.findViewById(R.id.tv_noPost);

        avLoadingIndicatorView = view.findViewById(R.id.iv_loading);
        avLoadingIndicatorView.show();

        MainActivity.iv_filter.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Picasso.with(getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).transform(new CropCircleTransformation()).into(ivAvata);
        tvName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Huid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> huidList = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String huid = d.getValue().toString();
                        huidList.add(huid);
                    }
                    getListHotel(huidList, 0);
                } else {
                    tvNoPost.setVisibility(View.VISIBLE);
                    avLoadingIndicatorView.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });


        return view;
    }

    public void getListHotel(final List<String> huidList, final int index) {
        databaseReference = firebaseDatabase.getReference("hotels");
        Log.d(TAG, "onDataChange: start for loop");

        databaseReference.child(huidList.get(index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HotelModel hotelModel = dataSnapshot.getValue(HotelModel.class);
                hotelModelList.add(hotelModel);

                if (index == huidList.size() - 1) {
                    hotelAdapter = new HotelAdapter(getFragmentManager(), getContext(), hotelModelList);
                    Log.d(TAG, "onDataChange: " + hotelAdapter);
                    rvHotel.setAdapter(hotelAdapter);
                    rvHotel.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvHotel.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                    avLoadingIndicatorView.hide();
                } else {
                    getListHotel(huidList, index + 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        MainActivity.iv_filter.setVisibility(View.VISIBLE);
        super.onStop();

    }

}
