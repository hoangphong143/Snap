package com.example.admins.snaphotel.Activities.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admins.snaphotel.Model.ChatModel;
import com.example.admins.snaphotel.Model.MessageModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Admins on 6/13/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    FirebaseAuth firebaseAuth;
    private Context context;
    String userName;
    int viewType;
    FirebaseDatabase firebaseDatabase;
    FragmentManager fragmentManager;
    List<MessageModel> messageModelList;


    public MessageAdapter(FirebaseAuth firebaseAuth, Context context, FirebaseDatabase firebaseDatabase, FragmentManager fragmentManager, List<MessageModel> messageModelList) {
        this.firebaseAuth = firebaseAuth;
        this.context = context;
        this.firebaseDatabase = firebaseDatabase;
        this.fragmentManager = fragmentManager;
        this.messageModelList = messageModelList;

    }


    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == 1) {
            View view = layoutInflater.inflate(R.layout.item_chat, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == 2) {
            View view = layoutInflater.inflate(R.layout.item_replychat, parent, false);
            return new MessageViewHolder(view);
        } else {
            return null;
        }


    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {
        holder.setData(messageModelList.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        if (userName.equals(messageModelList.get(position).userName)){
             viewType = 1;
            return 1;
        }else{
            viewType =2;
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView repUserName;
        TextView reply;
        TextView mycontent;


        public MessageViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_reply);
            repUserName = itemView.findViewById(R.id.tv_name_reply);
            reply = itemView.findViewById(R.id.tv_reply);
            mycontent = itemView.findViewById(R.id.tv_text_chat);
        }


        public void setData(MessageModel messageModel) {
            if (viewType == 2){
                Picasso.with(context).load(messageModel.Uri)
                        .transform(new CropCircleTransformation())
                        .into(ivAvatar);
                repUserName.setText(messageModel.userName);
                reply.setText(messageModel.content);
            }else if (viewType == 1){
                Log.d("TAG", "setData: " + messageModel.content);
                mycontent.setText(messageModel.content);
            }
        }
    }
}
