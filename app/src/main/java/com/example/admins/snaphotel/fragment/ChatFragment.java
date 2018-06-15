package com.example.admins.snaphotel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.admins.snaphotel.Activities.Adapters.MessageAdapter;
import com.example.admins.snaphotel.Model.MessageModel;
import com.example.admins.snaphotel.Model.UserModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    EditText et_chat;
    ImageView iv_send;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    UserModel userModel;
    LinearLayoutManager linearLayoutManager;
    MessageAdapter messageAdapter;
    List<MessageModel> messageModelList= new ArrayList<>();


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_chat, container, false);
         setupUi(view);
         return view;
    }

    private void setupUi(View view) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        et_chat=view.findViewById(R.id.et_text_chat);
        iv_send=view.findViewById(R.id.iv_chat);
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_chat.getText().toString().equals("")){
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm a");
                    String strDate = formatter.format(date);
                    MessageModel messageModel = new MessageModel(userModel.name,et_chat.getText().toString(),
                            strDate,userModel.uid,userModel.uri);
                    messageModelList.add(messageModel);

                    et_chat.setText("");
                }


            }
        });

    }

}
