package com.example.admins.snaphotel.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.admins.snaphotel.Activities.Adapters.FeedbackAdapter;
import com.example.admins.snaphotel.Activities.LoginActivity;
import com.example.admins.snaphotel.Activities.Event.OnClickWindowinfo;
import com.example.admins.snaphotel.Model.HotelModel;

import com.example.admins.snaphotel.Model.ReviewModel;
import com.example.admins.snaphotel.Model.UserModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RatingFragment.class.toString();
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FeedbackAdapter feedbackAdapter;
    RecyclerView rvFeedback;
    public HotelModel hotelModel;

    public RatingFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        EventBus.getDefault().register(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        setupUI(view);
        loadData();

        return view;
//
    }

    private void loadData() {
    }

    private void setupUI(View view) {
        TextView tvrate = view.findViewById(R.id.tv_rate);
        rvFeedback = view.findViewById(R.id.rv_feedback);
        tvrate.setOnClickListener(this);
        feedbackAdapter = new FeedbackAdapter(getContext(), hotelModel.reviewModels);

        rvFeedback.setAdapter(feedbackAdapter);
        rvFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    @Subscribe(sticky = true)
    public void getHotelModel(final OnClickWindowinfo onClickWindowinfo) {
        hotelModel = onClickWindowinfo.hotelModel;
        if (hotelModel.reviewModels == null) hotelModel.reviewModels = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_rate: {
                if (firebaseAuth.getCurrentUser() == null) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    LayoutInflater layoutInflater = this.getLayoutInflater();
                    View dialogView = layoutInflater.inflate(R.layout.require, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    Button btnYes = dialogView.findViewById(R.id.btn_yes);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            alertDialog.dismiss();

                        }
                    });
                    Button btHuy = dialogView.findViewById(R.id.btn_no);
                    btHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    LayoutInflater layoutInflater = this.getLayoutInflater();
                    View dialogView = layoutInflater.inflate(R.layout.fragment_user_feed_back, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    final RatingBar rbRate = dialogView.findViewById(R.id.rb_rating);
                    final EditText etComment = dialogView.findViewById(R.id.et_comment);
                    Log.d(TAG, "onClick: " + etComment.getText().toString());
                    TextView tvGui = dialogView.findViewById(R.id.bt_post);
                    TextView tvHuy = dialogView.findViewById(R.id.tv_huy);
//                    rbRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                        @Override
//                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                            rbRate.setRating(v);
//                            Log.d(TAG, "onRatingChanged: ");
//                        }
//                    });
                    databaseReference = firebaseDatabase.getReference("users");
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    tvHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    tvGui.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            databaseReference = firebaseDatabase.getReference("hotels");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = Calendar.getInstance().getTime();
                            ReviewModel review = new ReviewModel(
                                    new UserModel(firebaseAuth.getCurrentUser().getDisplayName(),firebaseAuth.getCurrentUser().getUid(),""),
                                    dateFormat.format(date),
                                    etComment.getText().toString(),  rbRate.getRating());
                            if (hotelModel.reviewModels == null) {
                                List<ReviewModel> reviewModels = new ArrayList<>();
                                reviewModels.add(review);
                                hotelModel.reviewModels = reviewModels;
                            } else {
                                hotelModel.reviewModels.add(review);
                            }
                            rvFeedback.setAdapter(new FeedbackAdapter(getContext(), hotelModel.reviewModels));
                            databaseReference.child(hotelModel.key).setValue(hotelModel);
                            alertDialog.dismiss();

                        }


                    });


                }
            }
        }
    }
}