package com.example.admins.snaphotel.fragment;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admins.snaphotel.Event.OnClickWindowinfo;
import com.example.admins.snaphotel.Event.SendHotelModel;
import com.example.admins.snaphotel.Activities.LoginActivity;
import com.example.admins.snaphotel.Model.HotelModel;

import com.example.admins.snaphotel.Ultis.ImageUtils;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String TAG = DetailFragment.class.toString();
    TextView tvGia;
    RelativeLayout rlWifi, rlNongLanh, rlDieuHoa, rlThangMay, rlTuLanh, rlTivi;
    TextView tvAddress;
    TextView tvPhone, tvPhone2, tvChat;
    TextView tvRate;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ImageView ivStar;
    public HotelModel hotelModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        EventBus.getDefault().register(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        setupUI(view);

        addListtenners();
        loadData();
        return view;
    }

    private void loadData() {
        AlphaAnimation alpha = new AlphaAnimation(0.1F, 0.1F);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        if (!hotelModel.dieuHoa) {
            rlDieuHoa.startAnimation(alpha);
        }

        if (!hotelModel.wifi) {
            rlWifi.startAnimation(alpha);
        }

        if (!hotelModel.nongLanh) {
            rlNongLanh.startAnimation(alpha);
        }

        if (!hotelModel.thangMay) {
            rlThangMay.startAnimation(alpha);
        }

        if (!hotelModel.tulanh) {
            rlTuLanh.startAnimation(alpha);
        }

        if (!hotelModel.tivi) {
            rlTivi.startAnimation(alpha);
        }

        tvAddress.setText(hotelModel.address);

        tvPhone.setText(hotelModel.phone);
        tvPhone2.setText(hotelModel.phone1);
        String giaDon = hotelModel.gia.substring(0, hotelModel.gia.indexOf("-"));
        String giaDoi = hotelModel.gia.substring(hotelModel.gia.indexOf("-") + 1);
        Log.d(TAG, "loadData: " + giaDon + "   " + giaDoi);

        tvGia.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDon)) + " VNĐ" + " -  " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDoi)) + " VNĐ");

        // tvGia.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDon))+" - " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDoi))+ " VNĐ");


        tvRate.setText(hotelModel.danhGiaTB * 2 + "/10");
    }

    private void setupUI(View view) {
        rlDieuHoa = view.findViewById(R.id.rl_dieu_hoa);
        rlNongLanh = view.findViewById(R.id.rl_nong_lanh);
        rlThangMay = view.findViewById(R.id.rl_thang_may);
        rlWifi = view.findViewById(R.id.rl_wifi);
        tvAddress = view.findViewById(R.id.tv_address);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvGia = view.findViewById(R.id.tv_gia);
        tvRate = view.findViewById(R.id.tv_rating);
        tvRate = view.findViewById(R.id.tv_rating);
        ivStar = view.findViewById(R.id.iv_star);
        rlTivi = view.findViewById(R.id.rl_tivi);
        rlTuLanh = view.findViewById(R.id.rl_tu_lanh);
        tvPhone2 = view.findViewById(R.id.tv_phone2);
        tvChat = view.findViewById(R.id.tv_chat);

        checkHotel();
    }

    private void checkHotel() {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null) {
            databaseReference.child(firebaseUser.getUid()).child("Huid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String huid = snapshot.getValue(String.class);
                        if (huid.equals(hotelModel.key)) {
                            tvChat.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Subscribe(sticky = true)
    public void getHotelModel(final OnClickWindowinfo onClickWindowinfo) {
        hotelModel = onClickWindowinfo.hotelModel;
        if (hotelModel.reviewModels == null) hotelModel.reviewModels = new ArrayList<>();
    }

    public void addListtenners() {
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder altdial = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                altdial.setMessage("Bạn muốn thực hiện cuộc gọi").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 0);
                                    Log.e(TAG, "onClick: " + "permission");
                                } else {
                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + tvPhone.getText().toString().trim())));
                                }

                            } else {

                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + tvPhone.getText().toString().trim())));
                            }
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(getContext(), "Không thể thực hiện cuộc gọi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

            }
        });

        tvPhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder altdial = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                altdial.setMessage("Bạn muốn thực hiện cuộc gọi").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 0);
                                    Log.e(TAG, "onClick: " + "permission");
                                } else {
                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + tvPhone2.getText().toString().trim())));
                                }

                            } else {

                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + tvPhone2.getText().toString().trim())));
                            }
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(getContext(), "Không thể thực hiện cuộc gọi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

            }
        });

        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() == null) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    LayoutInflater layoutInflater = getLayoutInflater();
                    View dialogView = layoutInflater.inflate(R.layout.require, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    Button btYes = dialogView.findViewById(R.id.btn_yes);
                    btYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i2 = new Intent(getContext(), LoginActivity.class);
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
                    ImageUtils.openFragment(getActivity().getSupportFragmentManager(), R.id.rl_detail, new ChatFragment());
                    EventBus.getDefault().postSticky(new SendHotelModel(hotelModel));
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            Log.e(TAG, "onRequestPermissionsResult: ");
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + tvPhone.getText().toString().trim())));
            }
        }
    }


}
