package com.example.admins.snaphotel.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admins.snaphotel.Event.SendHotelModel;
import com.example.admins.snaphotel.Event.SendKeyMess;
import com.example.admins.snaphotel.Model.ChatModel;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.admins.snaphotel.Ultis.ImageUtils;
import com.example.admins.snaphotel.fragment.ChatFragment;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Admins on 6/14/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    List<ChatModel> modelArrayList;
    FragmentManager fragmentManager;

    public ChatAdapter(List<ChatModel> modelArrayList, FragmentManager fragmentManager) {
        this.modelArrayList = modelArrayList;
        this.fragmentManager = fragmentManager;
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

        public void setData(final ChatModel chatModel) {
            if (chatModel.UserAva!=null) {
                Picasso.with(itemView.getContext()).load(chatModel.UserAva)
                        .transform(new CropCircleTransformation()).into(ivPhoto);
            } else {
                Picasso.with(itemView.getContext()).load(chatModel.hotelAva)
                        .transform(new CropCircleTransformation()).into(ivPhoto);
            }
            tvName.setText(chatModel.name);
            tvDate.setText(chatModel.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().postSticky(new SendKeyMess(chatModel.keyHotel, chatModel.keyListMess));
                    ImageUtils.openFragment(fragmentManager, R.id.rl_main, new ChatFragment());
                }
            });
        }
    }
}
