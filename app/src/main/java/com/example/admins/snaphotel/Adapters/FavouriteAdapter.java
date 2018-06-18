package com.example.admins.snaphotel.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.admins.snaphotel.Activities.InformationOfHotelActivity;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.admins.snaphotel.Ultis.ImageUtils;
import com.example.admins.snaphotel.Ultis.OnClickWindowinfo;
import com.example.nguyenducanhit.hotelhunter2.R;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
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
        ImageView ivMenu, ivImage;
        TextView tvName, tvPrice, tvAddress, tvEdit;
        RatingBar rbStar;
        View itemView;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvAddress = itemView.findViewById(R.id.tv_address);
            ivImage = itemView.findViewById(R.id.iv_image);
            rbStar = itemView.findViewById(R.id.rb_star);
            this.itemView = itemView;
        }

        public void setData(final HotelModel hotelModel) {
            tvName.setText(hotelModel.nameHotel);
            tvAddress.setText(hotelModel.address);
            rbStar.setRating(hotelModel.danhGiaTB);
            String giaDon = hotelModel.gia.substring(0, hotelModel.gia.indexOf("-"));
            String giaDoi = hotelModel.gia.substring(hotelModel.gia.indexOf("-") + 1);
            tvPrice.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDon)) + "VNĐ" + " -  " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDoi)) + "VNĐ");
            ivImage.setImageBitmap(ImageUtils.base64ToImage(hotelModel.images.get(0)));
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

    ;
}


