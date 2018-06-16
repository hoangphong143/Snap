package com.example.admins.snaphotel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admins.snaphotel.Activities.MainActivity;
import com.example.admins.snaphotel.Adapters.ChatAdapter;
import com.example.admins.snaphotel.Adapters.MessageAdapter;
import com.example.admins.snaphotel.Model.ChatModel;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.admins.snaphotel.Model.MessageModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyMessageFragment extends Fragment {
    private static final String TAG = "MyMessageFragment";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    RecyclerView rvMess;
    AVLoadingIndicatorView avLoadingIndicatorView;

    List<ChatModel> chatModels = new ArrayList<>();

    public MyMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_message, container, false);
        rvMess = view.findViewById(R.id.rv_mess);
        avLoadingIndicatorView = view.findViewById(R.id.iv_loading);

        MainActivity.iv_filter.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("hotels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatModels.clear();
                for (DataSnapshot hotelSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot messListSnapshot : hotelSnapshot.child("listMessage").getChildren()) {
                        if (messListSnapshot.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                            for (DataSnapshot messSnapshot : messListSnapshot.getChildren()) {
                                MessageModel messageModel = messSnapshot.getValue(MessageModel.class);
                                chatModels.add(new ChatModel(
                                        messageModel.photoUri,
                                        messageModel.userName,
                                        messageModel.time.split(" ")[0]
                                ));
                                break;
                            }
                        }
                    }
                }
                avLoadingIndicatorView.hide();
                rvMess.setAdapter(new ChatAdapter(chatModels));
                rvMess.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.iv_filter.setVisibility(View.VISIBLE);
    }
}

