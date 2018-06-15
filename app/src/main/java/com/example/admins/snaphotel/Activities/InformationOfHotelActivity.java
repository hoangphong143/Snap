package com.example.admins.snaphotel.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.admins.snaphotel.Activities.Adapters.ViewPagerAdapter;
import com.example.admins.snaphotel.Activities.Event.OnClickWindowinfo;
import com.example.admins.snaphotel.Model.HotelModel;
import com.example.admins.snaphotel.Ultis.ImageUtils;
import com.example.admins.snaphotel.fragment.ChatFragment;
import com.example.admins.snaphotel.fragment.MyMessageFragment;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InformationOfHotelActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{
    private static final String TAG = "InformationOfHotel";
    TabLayout tab;
    ViewPager vpFragment;
    ImageView ivHotel;
    HotelModel hotelModel;
    ViewPager viewPager;
    TextView tvName;
    ImageView iv_chat;
    SliderLayout sliderLayout;
    PagerIndicator pagerIndicator;
    ImageView iv_back;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_information_of_hotel);
        Log.d(TAG, "onCreate: ");
        EventBus.getDefault().register(this);
        Log.d(TAG, "onCreate: register hotel"+hotelModel);
        setupUI();
        ShowImage();
    }

    private void setupUI() {
        firebaseAuth=FirebaseAuth.getInstance();
        tab = findViewById(R.id.tab);
        viewPager = findViewById(R.id.vp_fragment);
        tvName = findViewById(R.id.tv_name);
        iv_chat=findViewById(R.id.iv_chat);
//        ivHotel = findViewById(R.id.iv_hotel);
        tab.addTab(tab.newTab().setText("Details"));
        tab.addTab(tab.newTab().setText("Comment"));
        sliderLayout = findViewById(R.id.slide_hotel);
        pagerIndicator= findViewById(R.id.custom_indicator);
        iv_back = findViewById(R.id.iv_backinfor);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getIcon().setAlpha(100);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() == null) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
                    LayoutInflater layoutInflater = getLayoutInflater();
                    View dialogView = layoutInflater.inflate(R.layout.require, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    Button btYes = dialogView.findViewById(R.id.btn_yes);
                    btYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i2 = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i2);

                        }
                    });
                    Button btNo = dialogView.findViewById(R.id.btn_no);
                    btNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    ImageUtils.openFragment(getSupportFragmentManager(), R.id.rl_main, new MyMessageFragment());
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tvName.setText("Nhà nghỉ " + hotelModel.nameHotel);
    }

    @Subscribe(sticky = true)
    public void onRecievedHotelModel(final OnClickWindowinfo onClickWindowinfo) {
        hotelModel = onClickWindowinfo.hotelModel;
        Log.d(TAG, "onRecievedHotelModel: " + hotelModel);
    }
    public void ShowImage()
    {

        for (int i=0; i<hotelModel.images.size(); i++)
        {
            String encodedImage = hotelModel.images.get(i);
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            try {
                TextSliderView sliderView = new TextSliderView(this);
                File f = File.createTempFile("tmp","jpg", getCacheDir());
                FileOutputStream fos = new FileOutputStream(f);
                decodedByte.compress(Bitmap.CompressFormat.JPEG,100,fos);
                fos.close();
                sliderView.image(f)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                sliderLayout.addSlider(sliderView);
            }catch (IOException io) {
                io.printStackTrace();
            }
        }
        pagerIndicator.setVisibility(View.VISIBLE);
        sliderLayout.setCustomIndicator(pagerIndicator);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
