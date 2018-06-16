package com.example.admins.snaphotel.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admins.snaphotel.Activities.Adapters.CustomInfoWindowAdapter;
import com.example.admins.snaphotel.Activities.Event.ContinueWithOldFragment;
import com.example.admins.snaphotel.Activities.Event.OnClickWindowinfo;
import com.example.admins.snaphotel.Activities.Event.SendInfoFragmentToLogin;
import com.example.admins.snaphotel.Model.HotelModel;

import com.example.admins.snaphotel.Ultis.DataHandle;
import com.example.admins.snaphotel.Ultis.ImageUtils;
import com.example.admins.snaphotel.fragment.MyHotelFragment;
import com.example.admins.snaphotel.fragment.MyMessageFragment;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = MainActivity.class.toString();
    FirebaseAuth firebaseAuth;
    public static GoogleMap mMap;
    TextView tvName, tvNavText;
    ImageView ivAvata;
    RelativeLayout relativeLayout;
    HotelModel hotelModel;
    public static boolean first = true;
    public LatLng currentLocation;
    public static List<HotelModel> list = new ArrayList<>();
    RadioButton rd_cademnho100, rd_cademnho200, rd_cademlon200, rd_thegionho70, rd_theogionho100,
            rd_theogiolon100, rd_kc2km, rd_kc27km, rd_kclon7km;
    ImageView iv_wifi, iv_thangmay, iv_dieuhoa, iv_nonglanh, iv_tivi, iv_tulanh;
    TextView tv_wifi, tv_thangmay, tv_dieuhoa, tv_nonglanh, tv_tivi, tv_tulanh, tv_loc, tv_huy;
    LinearLayout ln_wifi, ln_thangmay, ln_dieuhoa, ln_nonglanh, ln_tivi, ln_tulanh;
    public boolean tiVi = false;
    public boolean tuLanh = false;
    public boolean dieuHoa = false;
    public boolean thangMay = false;
    public boolean wifi = false;
    public boolean nongLanh = false;
    AlertDialog alertDialog;
    AVLoadingIndicatorView avLoadingIndicatorView;
    public static ImageView iv_filter;
    public static Context Main;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_filter = findViewById(R.id.iv_filter);
        iv_filter.setOnClickListener(this);
        Main = MainActivity.this;

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        tvNavText = view.findViewById(R.id.tv_nav_login);
        tvName = view.findViewById(R.id.tv_name);
        firebaseAuth = FirebaseAuth.getInstance();
        ivAvata = view.findViewById(R.id.iv_avatar);
//        Log.d(TAG, "onCreate: "+firebaseAuth.getCurrentUser().getDisplayName());
        if (firebaseAuth.getCurrentUser() == null) {
            Picasso.with(this).load(R.drawable.avatar_offline).transform(new CropCircleTransformation()).into(ivAvata);
        } else {
            Log.d(TAG, "onCreate: " + firebaseAuth.getCurrentUser() + firebaseAuth.getCurrentUser().getDisplayName());
            tvNavText.setVisibility(View.GONE);
            tvName.setVisibility(View.VISIBLE);
            ivAvata.setVisibility(View.VISIBLE);
            tvName.setText(firebaseAuth.getCurrentUser().getDisplayName());
            Picasso.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).transform(new CropCircleTransformation()).into(ivAvata);
//            ivAvata.setImageResource(R.drawable.ic_close_black_24dp);
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() == null) {
                Picasso.with(this).load(R.drawable.avatar_mac_dinh).transform(new CropCircleTransformation()).into(ivAvata);
            }
        }
        tvNavText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        iv_filter.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            if (getSupportFragmentManager().getBackStackEntryCount()!=0) {
                super.onBackPressed();
            }

        } else if (id == R.id.nav_Logout) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View dialogView = layoutInflater.inflate(R.layout.sign_out, null);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            Button btYes = dialogView.findViewById(R.id.btn_yes);
            btYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    tvNavText.setVisibility(View.VISIBLE);
                    Picasso.with(MainActivity.this).load(R.drawable.avatar_offline).transform(new CropCircleTransformation()).into(ivAvata);
                    tvName.setVisibility(View.GONE);
                    alertDialog.dismiss();
                }
            });

            Button btNo = dialogView.findViewById(R.id.btn_no);
            btNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


        } else if (id == R.id.nav_myhotel) {
            if (firebaseAuth.getCurrentUser() == null) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.require, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                Button btYes = dialogView.findViewById(R.id.btn_yes);
                btYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i2 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i2);
                        alertDialog.dismiss();
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
                Intent i3 = new Intent(this, CodeActivity.class);
                startActivity(i3);
            }

        } else if (id == R.id.nav_myPost) {
            if (firebaseAuth.getCurrentUser() == null) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
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
                        alertDialog.dismiss();
                        EventBus.getDefault().postSticky(new SendInfoFragmentToLogin(new MyHotelFragment()));
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
            ImageUtils.openFragment(getSupportFragmentManager(), R.id.rl_main, new MyHotelFragment());
            }
        } else if (id == R.id.nav_chat) {
            if (firebaseAuth.getCurrentUser() == null) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
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
                        alertDialog.dismiss();
                    }
                });
                Button btNo = dialogView.findViewById(R.id.btn_no);
                btNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                EventBus.getDefault().postSticky(new SendInfoFragmentToLogin(new MyMessageFragment()));
            } else {
                ImageUtils.openFragment(getSupportFragmentManager(), R.id.rl_main, new MyMessageFragment());
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        DataHandle.hotelModels(mMap, this);
        list = DataHandle.hotelModels(mMap, this);
        if (first == true) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(TurnOnGPSActivity.currentLocation, 13);
            mMap.animateCamera(cameraUpdate);
            first = false;
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

//                for (int i = 0; i < list.size(); i++) {
//                    if (marker.getPosition().latitude == list.get(i).viDo && marker.getPosition().longitude == list.get(i).kinhDo) {
//                        EventBus.getDefault().postSticky(new OnClickWindowinfo(list.get(i)));
//                        Log.d(TAG, "onInfoWindowClick: " + list.get(i));
//                    }
//                }

                EventBus.getDefault().postSticky(new OnClickWindowinfo((HotelModel) marker.getTag()));

                Log.d(TAG, "onInfoWindowClick: " + list.size());
                Log.d(TAG, "onInfoWindowClick: "+marker.getTag());
                Intent intent = new Intent(MainActivity.this, InformationOfHotelActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_filter: {
                tiVi = tuLanh = dieuHoa = nongLanh = thangMay = wifi = false;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                View dialogView = layoutInflater.inflate(R.layout.filter, null);
                setupUI(dialogView);
                setEnableService();
                addListtenners();
                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                break;
            }
            case R.id.ln_wififillter: {
                wifi = wifi ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_thangmayfillter: {
                thangMay = thangMay ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_dieuhoafillter: {
                dieuHoa = dieuHoa ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_nonglanhfillter: {
                nongLanh = nongLanh ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_tivifillter: {
                tiVi = tiVi ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_tulanhfillter: {
                tuLanh = tuLanh ? false : true;
                setEnableService();
                break;
            }
            case R.id.tv_loc: {
                Fillter();
                alertDialog.dismiss();
                break;
            }
            case R.id.tv_huyfillter: {
                alertDialog.dismiss();
                break;
            }

        }

    }

    public void addListtenners() {
        ln_tivi.setOnClickListener(this);
        ln_tulanh.setOnClickListener(this);
        ln_nonglanh.setOnClickListener(this);
        ln_wifi.setOnClickListener(this);
        ln_thangmay.setOnClickListener(this);
        ln_dieuhoa.setOnClickListener(this);
        tv_loc.setOnClickListener(this);
        tv_huy.setOnClickListener(this);
    }

    private void setEnableService() {
        if (tiVi) {
            iv_tivi.setAlpha(255);
            tv_tivi.setTextColor(Color.argb(255, 0, 0, 0));
        } else {
            iv_tivi.setAlpha(100);
            tv_tivi.setTextColor(Color.argb(100, 0, 0, 0));
        }
        if (tuLanh) {
            tv_tulanh.setTextColor(Color.argb(255, 0, 0, 0));
            iv_tulanh.setAlpha(255);
        } else {
            tv_tulanh.setTextColor(Color.argb(100, 0, 0, 0));
            iv_tulanh.setAlpha(100);
        }
        if (thangMay) {
            tv_thangmay.setTextColor(Color.argb(255, 0, 0, 0));
            iv_thangmay.setAlpha(255);
        } else {
            tv_thangmay.setTextColor(Color.argb(100, 0, 0, 0));
            iv_thangmay.setAlpha(100);
        }
        if (nongLanh) {
            iv_nonglanh.setAlpha(255);
            tv_nonglanh.setTextColor(Color.argb(255, 0, 0, 0));
        } else {
            iv_nonglanh.setAlpha(100);
            tv_nonglanh.setTextColor(Color.argb(100, 0, 0, 0));
        }
        if (dieuHoa) {
            tv_dieuhoa.setTextColor(Color.argb(255, 0, 0, 0));
            iv_dieuhoa.setAlpha(255);
        } else {
            tv_dieuhoa.setTextColor(Color.argb(100, 0, 0, 0));
            iv_dieuhoa.setAlpha(100);
        }
        if (wifi) {
            tv_wifi.setTextColor(Color.argb(255, 0, 0, 0));
            iv_wifi.setAlpha(255);
        } else {
            tv_wifi.setTextColor(Color.argb(100, 0, 0, 0));
            iv_wifi.setAlpha(100);
        }

    }

    public void setupUI(View dialogView) {
        rd_cademnho100 = dialogView.findViewById(R.id.rd_cademnho100);
        rd_cademnho200 = dialogView.findViewById(R.id.rd_cademnho200);
        rd_cademlon200 = dialogView.findViewById(R.id.rd_cademlon200);

        rd_thegionho70 = dialogView.findViewById(R.id.rd_theogionho70);
        rd_theogionho100 = dialogView.findViewById(R.id.rd_theogionho100);
        rd_theogiolon100 = dialogView.findViewById(R.id.rd_theogiolon100);

        rd_kc2km = dialogView.findViewById(R.id.rd_kc2km);
        rd_kc27km = dialogView.findViewById(R.id.rd_kc27km);
        rd_kclon7km = dialogView.findViewById(R.id.rd_kclon7km);

        ln_dieuhoa = dialogView.findViewById(R.id.ln_dieuhoafillter);
        ln_tulanh = dialogView.findViewById(R.id.ln_tulanhfillter);
        ln_tivi = dialogView.findViewById(R.id.ln_tivifillter);
        ln_nonglanh = dialogView.findViewById(R.id.ln_nonglanhfillter);
        ln_wifi = dialogView.findViewById(R.id.ln_wififillter);
        ln_thangmay = dialogView.findViewById(R.id.ln_thangmayfillter);

        iv_wifi = dialogView.findViewById(R.id.iv_wififillter);
        iv_thangmay = dialogView.findViewById(R.id.iv_thangmayfillter);
        iv_dieuhoa = dialogView.findViewById(R.id.iv_dieuhoafillter);
        iv_nonglanh = dialogView.findViewById(R.id.iv_nonglanhfillter);
        iv_tivi = dialogView.findViewById(R.id.iv_tivifillter);
        iv_tulanh = dialogView.findViewById(R.id.iv_tulanhfillter);
        tv_wifi = dialogView.findViewById(R.id.tv_wififillter);
        tv_thangmay = dialogView.findViewById(R.id.tv_thangmayfillter);
        tv_dieuhoa = dialogView.findViewById(R.id.tv_dieuhoafillter);
        tv_nonglanh = dialogView.findViewById(R.id.tv_nonglanhfillter);
        tv_tivi = dialogView.findViewById(R.id.tv_tivifillter);
        tv_tulanh = dialogView.findViewById(R.id.tv_tulanhfillter);
        tv_loc = dialogView.findViewById(R.id.tv_loc);
        tv_huy = dialogView.findViewById(R.id.tv_huyfillter);
    }

    public void Fillter() {
        mMap.clear();
        for (int i = 0; i < list.size(); i++) {
            if (tiVi) {
                if (!list.get(i).tivi) {
                    Log.d(TAG, "Fillter: tivi false");
                    continue;
                }
            }
            if (tuLanh) {
                if (!list.get(i).tulanh) {
                    Log.d(TAG, "Fillter: tulanh false");
                    continue;
                }
            }
            if (wifi) {
                if (!list.get(i).wifi) {
                    Log.d(TAG, "Fillter: wifi false");
                    continue;
                }
            }
            if (thangMay) {
                if (!list.get(i).thangMay) {
                    Log.d(TAG, "Fillter: thangmay false");
                    continue;
                }
            }
            if (nongLanh) {
                if (!list.get(i).nongLanh) {
                    Log.d(TAG, "Fillter: nonglanh false");
                    continue;
                }
            }
            if (dieuHoa) {
                if (!list.get(i).dieuHoa) {
                    Log.d(TAG, "Fillter: dieuhoa false");
                    continue;
                }
            }
            if (!xetDKGiaDem(list.get(i))) {
                Log.d(TAG, "Fillter: giaDem false");
                continue;
            }
            if (!xetDKGiaGio(list.get(i))) {
                Log.d(TAG, "Fillter: giaGio false");
                continue;
            }
            if (!xetKhoangCach(i)) {
                Log.d(TAG, "Fillter: khoangCach false");
                continue;
            }

            HotelModel hotelModel = list.get(i);
            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(this);
            mMap.setInfoWindowAdapter(adapter);
            LatLng sydney = new LatLng(hotelModel.viDo, hotelModel.kinhDo);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(sydney).title(hotelModel.nameHotel).snippet(String.valueOf(hotelModel.danhGiaTB) + "/" + hotelModel.gia);
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(hotelModel);
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_hotel));
        }

    }

    public boolean xetDKGiaDem(HotelModel hotelModel) {
        String giaDem = hotelModel.gia.substring(hotelModel.gia.indexOf("-") + 1);
        if (rd_cademlon200.isChecked()) {
            try {
                if (Integer.parseInt(giaDem) > 200000) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        } else if (rd_cademnho200.isChecked()) {
            try {
                if (Integer.parseInt(giaDem) <= 200000 && Integer.parseInt(giaDem) >= 100000) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        } else if (rd_cademnho100.isChecked()) {
            try {
                if (Integer.parseInt(giaDem) < 100000) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        } else {
            return true;
        }

    }

    public boolean xetDKGiaGio(HotelModel hotelModel) {
        String giaGio = hotelModel.gia.substring(0, hotelModel.gia.indexOf("-"));
        if (rd_theogiolon100.isChecked()) {
            try {
                if (Integer.parseInt(giaGio) > 100000) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        } else if (rd_theogionho100.isChecked()) {
            try {
                if (Integer.parseInt(giaGio) <= 100000 && Integer.parseInt(giaGio) >= 70000) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        } else if (rd_thegionho70.isChecked()) {

            try {
                if (Integer.parseInt(giaGio) < 70000) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        } else {
            return true;
        }

    }
    public boolean xetKhoangCach(int position) {
        if (DataHandle.rows != null&&DataHandle.rows.size()>0) {
            if(DataHandle.rows.get(0).elements.size()>position)
            {
                if (DataHandle.rows.get(0).elements.get(position).status.equals("OK")) {
                    if (rd_kc2km.isChecked()) {
                        if (DataHandle.rows.get(0).elements.get(position).distance.value < 2000) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (rd_kc27km.isChecked()) {
                        if (DataHandle.rows.get(0).elements.get(position).distance.value <= 7000 && DataHandle.rows.get(0).elements.get(position).distance.value >= 2000) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if ((rd_kclon7km.isChecked())) {
                        if (DataHandle.rows.get(0).elements.get(position).distance.value > 7000) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                }
            }
            else {
                return false;
            }


        }
        return false;
    }

    @Subscribe(sticky = true)
    public void openFragmentAfterLogin(ContinueWithOldFragment continueWithOldFragment) {
        ImageUtils.openFragment(getSupportFragmentManager(), R.id.rl_main, continueWithOldFragment.fragment);
    }

}
