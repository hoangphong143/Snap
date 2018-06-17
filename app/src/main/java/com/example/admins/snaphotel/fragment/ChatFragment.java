package com.example.admins.snaphotel.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.admins.snaphotel.Adapters.MessageAdapter;
import com.example.admins.snaphotel.Event.SendHotelModel;
import com.example.admins.snaphotel.Event.SendKeyMess;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.admins.snaphotel.Model.MessageModel;
import com.example.admins.snaphotel.Model.UserModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";

    EditText et_chat;
    ImageView iv_send;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    UserModel userModel;
    String hotelModelKey;
    LinearLayoutManager linearLayoutManager;

    List<MessageModel> messageModelList = new ArrayList<>();

    boolean isHotel = true;
    FragmentActivity a;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("hotels");
        firebaseUser = firebaseAuth.getCurrentUser();
        recyclerView = view.findViewById(R.id.rv_text_chat);
        et_chat = view.findViewById(R.id.et_text_chat);
        iv_send = view.findViewById(R.id.iv_sent_chat);
        a = getActivity();
        EventBus.getDefault().register(this);

        loadListMess();
        setupUi(view);
        return view;
    }

    private void loadListMess() {

        if (!isHotel) {
            databaseReference.child(MyMessageFragment.hotelModelKey).child("listMessage")
                    .child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: " + dataSnapshot);
                    messageModelList.clear();
                    for (DataSnapshot messSnapshot : dataSnapshot.getChildren()) {
                        MessageModel messageModel = messSnapshot.getValue(MessageModel.class);
                        messageModelList.add(messageModel);
                    }

                    recyclerView.setAdapter(new MessageAdapter(firebaseUser, firebaseAuth, getContext(), firebaseDatabase,
                            getActivity().getSupportFragmentManager(), messageModelList));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setupUi(View view) {
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_chat.getText().toString().equals("")) {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm a");
                    String strDate = formatter.format(date);
                    MessageModel messageModel = new MessageModel(et_chat.getText().toString(), strDate,
                            firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString());
                    et_chat.setText("");
                    messageModelList.add(messageModel);

                    if (!isHotel) {
                        databaseReference.child(MyMessageFragment.hotelModelKey).child("listMessage")
                                .child(firebaseUser.getUid()).setValue(messageModelList);
                    }
                }
            }
        });

    }

    @Subscribe(sticky = true)
    public void getHotelModel(SendHotelModel sendHotelModel) {
        Log.d(TAG, "getHotelModel: ");
        MyMessageFragment.hotelModelKey = sendHotelModel.hotelModel.key;
        isHotel = false;
    }

    @Subscribe(sticky = true)
    public void getKey(final SendKeyMess sendHotelModel) {
        Log.d(TAG, "getKey: " + sendHotelModel.keyHotel);

        databaseReference.child(sendHotelModel.keyHotel).child("listMessage")
                .child(sendHotelModel.keyListMess).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageModelList.clear();
                for (DataSnapshot messSnapshot : dataSnapshot.getChildren()) {
                    MessageModel messageModel = messSnapshot.getValue(MessageModel.class);
                    messageModelList.add(messageModel);
                }

                recyclerView.setAdapter(new MessageAdapter(firebaseUser, firebaseAuth, getContext(), firebaseDatabase,
                        a.getSupportFragmentManager(), messageModelList));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_chat.getText().toString().equals("")) {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm a");
                    String strDate = formatter.format(date);
                    MessageModel messageModel = new MessageModel(et_chat.getText().toString(), strDate,
                            firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString());
                    et_chat.setText("");
                    messageModelList.add(messageModel);

                    databaseReference.child(sendHotelModel.keyHotel).child("listMessage")
                            .child(sendHotelModel.keyListMess).setValue(messageModelList);

                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        EventBus.getDefault().removeAllStickyEvents();
    }
}
