package com.example.admins.snaphotel.Activities.Adapters;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admins.snaphotel.Model.ChatModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admins on 6/14/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    ArrayList<ChatModel> modelArrayList = new ArrayList<>();

    public ChatAdapter(ArrayList<ChatModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_mess, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.setData(modelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvName;
        TextView tvDate;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_item_list_mess);
            tvName = itemView.findViewById(R.id.tv_item_list_mess_name);
            tvDate = itemView.findViewById(R.id.tv_item_list_mess_time);
        }

        public void setData(ChatModel chatModel) {
            Picasso.with(itemView.getContext()).load(chatModel.photoUri).into(ivPhoto);
            tvName.setText(chatModel.name);
            tvDate.setText(chatModel.date);
        }
    }
}
