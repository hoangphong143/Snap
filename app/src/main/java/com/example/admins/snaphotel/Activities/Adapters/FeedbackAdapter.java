package com.example.admins.snaphotel.Activities.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.admins.snaphotel.Model.ReviewModel;
import com.example.nguyenducanhit.hotelhunter2.R;

import java.util.List;

/**
 * Created by Admins on 4/6/2018.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private Context context;
    private List<ReviewModel> reviewModels;

    public FeedbackAdapter(Context context, List<ReviewModel> reviewModels) {
        this.context = context;
        this.reviewModels = reviewModels;
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.item_feedback,parent,false);
        return new  FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackAdapter.FeedbackViewHolder holder, int position) {
        holder.setData(reviewModels.get(position));

    }

    @Override
    public int getItemCount() {
        return reviewModels.size();
    }


    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDate;
        RatingBar rbRatting;
        TextView tvFeedback;
        View view;

        public FeedbackViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            rbRatting = itemView.findViewById(R.id.rb_rating);
            tvFeedback = itemView.findViewById(R.id.tv_feedback);
            view = itemView;
        }

        public void setData(ReviewModel reviewModel) {

            tvName.setText(reviewModel.userModel.getName());
            tvDate.setText(reviewModel.getDate());
            rbRatting.setMax(5);
            rbRatting.setRating(reviewModel.getRatting());
            tvFeedback.setText(reviewModel.getReview());
        }
    }
}