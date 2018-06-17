package com.example.admins.snaphotel.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admins.snaphotel.Activities.InformationOfHotelActivity;
import com.example.admins.snaphotel.Activities.MainActivity;
import com.example.admins.snaphotel.Event.OnClickWindowinfo;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>{
    List<HotelModel> hotelModelList;

    public FavouriteAdapter(List<HotelModel> hotelModelList) {
        this.hotelModelList = hotelModelList;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_fav, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        holder.setData(hotelModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return hotelModelList.size();
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotel;
        TextView tvHotel;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            ivHotel = itemView.findViewById(R.id.iv_item_fav_hotel);
            tvHotel = itemView.findViewById(R.id.tv_item_fav_hotel);
        }

        public void setData(final HotelModel hotelModel) {
            Picasso.with(itemView.getContext()).load(R.drawable.default_hotel)
                    .transform(new CropCircleTransformation())
                    .into(ivHotel);
            tvHotel.setText(hotelModel.nameHotel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().postSticky(new OnClickWindowinfo(hotelModel));
                    Intent intent = new Intent(itemView.getContext(), InformationOfHotelActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
