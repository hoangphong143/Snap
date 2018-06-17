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
import com.example.admins.snaphotel.Event.LoadMessageDone;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    List<ChatModel> chatModelsAsHotel = new ArrayList<>();
    List<ChatModel> chatModelsAsUser = new ArrayList<>();
    List<ChatModel> chatModels = new ArrayList<>();

    public static String hotelModelKey;

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
        EventBus.getDefault().register(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid())
                .child("Huid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatModelsAsHotel.clear();
                for (DataSnapshot uidSnapshot : dataSnapshot.getChildren()) {

                    final String huid = uidSnapshot.getValue(String.class);
                    databaseReference = firebaseDatabase.getReference("hotels");

                    //todo: xl 2nn
                    hotelModelKey = huid;

                    databaseReference.child(huid).child("listMessage").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot messListSnapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot messSnapshot : messListSnapshot.getChildren()) {
                                    Log.d(TAG, "onDataChange: messkey" + messListSnapshot.getKey());
                                    MessageModel messageModel = messSnapshot.getValue(MessageModel.class);
                                    chatModelsAsHotel.add(new ChatModel(
                                            -1,
                                            messageModel.photoUri,
                                            messageModel.userName,
                                            messageModel.time.split(" ")[0],
                                            huid,
                                            messListSnapshot.getKey()
                                    ));
                                    break;

                                }
                            }
                            EventBus.getDefault().post(new LoadMessageDone(chatModelsAsHotel));
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

        databaseReference = firebaseDatabase.getReference("hotels");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatModelsAsUser.clear();
                for (DataSnapshot hotelSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot messListSnapshot : hotelSnapshot.child("listMessage").getChildren()) {
                        if (messListSnapshot.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                            for (DataSnapshot messSnapshot : messListSnapshot.getChildren()) {
                                MessageModel messageModel = messSnapshot.getValue(MessageModel.class);
                                chatModelsAsUser.add(new ChatModel(
                                        R.drawable.default_hotel,
                                        null,
                                        hotelSnapshot.getValue(HotelModel.class).nameHotel,
                                        messageModel.time.split(" ")[0],
                                        hotelSnapshot.getKey(),
                                        messListSnapshot.getKey()
                                ));
                                break;
                            }
                        }
                    }
                }
                EventBus.getDefault().post(new LoadMessageDone(chatModelsAsUser));

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

    @Subscribe
    public void loadMessDone(LoadMessageDone loadMessageDone) {

        chatModels.addAll(loadMessageDone.chatModels);
        avLoadingIndicatorView.hide();
        rvMess.setAdapter(new ChatAdapter(chatModels, getActivity().getSupportFragmentManager()));
        rvMess.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

