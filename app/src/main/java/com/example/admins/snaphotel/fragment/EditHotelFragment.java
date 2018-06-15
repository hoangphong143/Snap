package com.example.admins.snaphotel.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admins.snaphotel.Activities.AddHotelActivity;
import com.example.admins.snaphotel.Model.HotelModel;

import com.example.admins.snaphotel.Ultis.ImageUtils;
import com.example.admins.snaphotel.Ultis.onClickMyHotel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditHotelFragment extends Fragment implements View.OnClickListener {

    EditText etTenNhaNghi;
    EditText etDiaChi;

    EditText etSDT1;
    EditText etGiaDem, etGiaGio;
    ImageView iv_wifi, iv_thangmay, iv_dieuhoa, iv_nonglanh, iv_tivi, iv_tulanh, iv_addphoto;
    TextView tv_wifi, tv_thangmay, tv_dieuhoa, tv_nonglanh, tv_tivi, tv_tulanh, tv_vitribando, tv_dt1;
    LinearLayout ln_wifi, ln_thangmay, ln_dieuhoa, ln_nonglanh, ln_tivi, ln_tulanh, ln_image;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    public List<HotelModel> list = new ArrayList<>();
    public static String TAG = AddHotelActivity.class.toString();
    public ImageView img_showhotel;
    public List<String> lst_Image = new ArrayList<>();
    public boolean tiVi = false;
    public boolean tuLanh = false;
    public boolean dieuHoa = false;
    public boolean thangMay = false;
    public boolean wifi = false;
    public boolean nongLanh = false;
    public Button bt_dangBai;
    HorizontalScrollView horizontalScrollView;
    AddHotelActivity.MyAsyncTask myAsyncTask;
    List<HotelModel> lstModels = new ArrayList<>();
    EditText kinhdo, vido, rate;
    public LatLng latLng = null;
    String phone1;

    HotelModel hotelModel;


    public EditHotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_hotel, container, false);
        EventBus.getDefault().register(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        setupUI(view);
        addListeners();
        return view;


    }

    private void addListeners() {

        iv_addphoto.setOnClickListener(this);
        iv_wifi.setOnClickListener(this);
        iv_thangmay.setOnClickListener(this);
        iv_dieuhoa.setOnClickListener(this);
        iv_nonglanh.setOnClickListener(this);
        iv_tivi.setOnClickListener(this);
        iv_tulanh.setOnClickListener(this);
        tv_vitribando.setOnClickListener(this);
        bt_dangBai.setOnClickListener(this);
        ln_tivi.setOnClickListener(this);
        ln_tulanh.setOnClickListener(this);
        ln_nonglanh.setOnClickListener(this);
        ln_wifi.setOnClickListener(this);
        ln_thangmay.setOnClickListener(this);
        ln_dieuhoa.setOnClickListener(this);


    }

    @Subscribe(sticky = true)
    public void getHotelModel(final onClickMyHotel onMyHotel) {
        hotelModel = onMyHotel.hotelModel;


    }


    private void setupUI(View view) {
        etTenNhaNghi = view.findViewById(R.id.et_tenadd);
        etDiaChi = view.findViewById(R.id.et_diachiadd);
        etSDT1 = view.findViewById(R.id.et_sdt1add);
        etGiaDem = view.findViewById(R.id.et_giademadd);
        etGiaGio = view.findViewById(R.id.et_giagioadd);

        iv_wifi = view.findViewById(R.id.iv_wifiadd);
        iv_thangmay = view.findViewById(R.id.iv_thangmayadd);
        iv_dieuhoa = view.findViewById(R.id.iv_dieuhoaadd);
        iv_nonglanh = view.findViewById(R.id.iv_nonglanhadd);
        iv_tivi = view.findViewById(R.id.iv_tiviadd);
        iv_tulanh = view.findViewById(R.id.iv_tulanhadd);
        iv_addphoto = view.findViewById(R.id.iv_addphoto);
        tv_wifi = view.findViewById(R.id.tv_wifiadd);
        tv_thangmay = view.findViewById(R.id.tv_thangmayadd);
        tv_dieuhoa = view.findViewById(R.id.tv_dieuhoaadd);
        tv_nonglanh = view.findViewById(R.id.tv_nonglanhadd);
        tv_tivi = view.findViewById(R.id.tv_tiviadd);
        tv_tulanh = view.findViewById(R.id.tv_tulanhadd);
        tv_vitribando = view.findViewById(R.id.tv_vitribando);
        bt_dangBai = view.findViewById(R.id.bt_danghotel);
        ln_dieuhoa = view.findViewById(R.id.ln_dieuhoa);
        ln_wifi = view.findViewById(R.id.ln_wifi);
        ln_nonglanh = view.findViewById(R.id.ln_nonglanh);
        horizontalScrollView = view.findViewById(R.id.sc_view);
        kinhdo = view.findViewById(R.id.et_kinhdoadd);
        vido = view.findViewById(R.id.et_vidoadd);
        ln_thangmay = view.findViewById(R.id.ln_thangmay);
        ln_tulanh = view.findViewById(R.id.ln_tulanh);
        ln_tivi = view.findViewById(R.id.ln_tivi);
        ln_image = view.findViewById(R.id.ln_image);
        rate = view.findViewById(R.id.et_rateadd);
        kinhdo.setVisibility(View.GONE);
        vido.setVisibility(View.GONE);
        rate.setVisibility(View.GONE);
        etTenNhaNghi.setText((hotelModel.nameHotel));
        etDiaChi.setText(hotelModel.address);
        etSDT1.setText(hotelModel.phone);
//        etGiaDem.addTextChangedListener(onTextChangedListener(etGiaDem));
//        etGiaGio.addTextChangedListener(onTextChangedListener(etGiaGio));
        etGiaDem.setText(hotelModel.gia);
        firebaseDatabase = FirebaseDatabase.getInstance();

        AlphaAnimation alpha = new AlphaAnimation(0.1F, 0.1F);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        if (!hotelModel.dieuHoa) {
            ln_dieuhoa.startAnimation(alpha);
        }

        if (!hotelModel.wifi) {
            ln_wifi.startAnimation(alpha);
        }

        if (!hotelModel.nongLanh) {
            ln_nonglanh.startAnimation(alpha);
        }

        if (!hotelModel.thangMay) {
            ln_thangmay.startAnimation(alpha);
        }

        if (!hotelModel.tulanh) {
            ln_tulanh.startAnimation(alpha);
        }

        if (!hotelModel.tivi) {
            ln_tivi.startAnimation(alpha);
        }


    }

    private TextWatcher onTextChangedListener(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editText.removeTextChangedListener(this);
                try {
                    String tmp = editable.toString();

                    Long longVar;
                    if (tmp.contains(",")) {
                        tmp = tmp.replaceAll(",", "");
                    }
                    longVar = Long.parseLong(tmp);

                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    decimalFormat.applyPattern("#,###,###");

                    String formatTmp = decimalFormat.format(longVar);
                    editText.setText(formatTmp);
                    editText.setSelection(editText.getText().length());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                editText.addTextChangedListener(this);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ln_wifi: {
                wifi = wifi ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_thangmay: {
                thangMay = thangMay ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_dieuhoa: {
                dieuHoa = dieuHoa ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_nonglanh: {
                nongLanh = nongLanh ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_tivi: {
                tiVi = tiVi ? false : true;
                setEnableService();
                break;
            }
            case R.id.ln_tulanh: {
                tuLanh = tuLanh ? false : true;
                setEnableService();
                break;
            }
            case R.id.bt_danghotel: {
                DangBai();
                ImageUtils.openFragment(getFragmentManager(),R.id.rl_main,new EditHotelFragment());

                break;
            }
            case R.id.tv_vitribando: {
                int PLACE_PICKER_REQUEST = 3;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    private void DangBai() {

        databaseReference = firebaseDatabase.getReference("hotels");
        databaseReference.child(hotelModel.key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bookSnapShot : dataSnapshot.getChildren()) {
                    HotelModel hotelModel = bookSnapShot.getValue(HotelModel.class);
                    hotelModel.tulanh = tuLanh;
                    hotelModel.tivi = tiVi;
                    hotelModel.thangMay = thangMay;
                    hotelModel.nongLanh = nongLanh;
                    hotelModel.dieuHoa = dieuHoa;
                    hotelModel.wifi = wifi;
                    hotelModel.address = etDiaChi.getText().toString();
                    hotelModel.nameHotel = etTenNhaNghi.getText().toString();
                    hotelModel.phone = etSDT1.getText().toString();
                    int giadem = Integer.parseInt( etGiaDem.getText().toString().replace(",",""));
                    int giagio = Integer.parseInt( etGiaGio.getText().toString().replace(",",""));
                    hotelModel.gia = Integer.toString(giagio) + "-" + Integer.toString(giadem);

                    hotelModel.kinhDo = latLng.longitude;
                    hotelModel.viDo = latLng.latitude;
                    databaseReference.child(hotelModel.key).setValue(hotelModel);
                    Toast.makeText(getContext(),"Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        boolean dk = true;
        if (TextUtils.isEmpty(etTenNhaNghi.getText())) {
            etTenNhaNghi.setError("Tên nhà nghỉ không được để trống");
            dk = false;
        }
        if (TextUtils.isEmpty(etDiaChi.getText())) {
            etDiaChi.setError("Địa chỉ không được để trống");
            dk = false;
        }
        if (TextUtils.isEmpty(etSDT1.getText())) {
            etSDT1.setError("Số điện thoại chính không được để trống");
            dk = false;
        }
        if (TextUtils.isEmpty(etGiaGio.getText())) {
            etGiaGio.setError("Không được để trống");
            dk = false;
        }
        if (TextUtils.isEmpty(etGiaDem.getText())) {
            etGiaDem.setError("Không được để trống");
            dk = false;
        }
        if (!dk) {
            return;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode== Activity.RESULT_OK)
            {
                Place place = PlacePicker.getPlace(data, getContext());
                latLng = place.getLatLng();
                Log.d(TAG, "onActivityResult: "+latLng);
            }
        }
    }

    private void setEnableService() {
        if (tiVi) {
            iv_tivi.setAlpha(255);
            tv_tivi.setTextColor(Color.argb(255, 252, 119, 3));
        } else {
            iv_tivi.setAlpha(100);
            tv_tivi.setTextColor(Color.argb(100, 252, 119, 3));
        }
        if (tuLanh) {
            tv_tulanh.setTextColor(Color.argb(255, 252, 119, 3));
            iv_tulanh.setAlpha(255);
        } else {
            tv_tulanh.setTextColor(Color.argb(100, 252, 119, 3));
            iv_tulanh.setAlpha(100);
        }
        if (thangMay) {
            tv_thangmay.setTextColor(Color.argb(255, 252, 119, 3));
            iv_thangmay.setAlpha(255);
        } else {
            tv_thangmay.setTextColor(Color.argb(100, 252, 119, 3));
            iv_thangmay.setAlpha(100);
        }
        if (nongLanh) {
            iv_nonglanh.setAlpha(255);
            tv_nonglanh.setTextColor(Color.argb(255, 252, 119, 3));
        } else {
            iv_nonglanh.setAlpha(100);
            tv_nonglanh.setTextColor(Color.argb(100, 252, 119, 3));
        }
        if (dieuHoa) {
            tv_dieuhoa.setTextColor(Color.argb(255, 252, 119, 3));
            iv_dieuhoa.setAlpha(255);
        } else {
            tv_dieuhoa.setTextColor(Color.argb(100, 252, 119, 3));
            iv_dieuhoa.setAlpha(100);
        }
        if (wifi) {
            tv_wifi.setTextColor(Color.argb(255, 252, 119, 3));
            iv_wifi.setAlpha(255);
        } else {
            tv_wifi.setTextColor(Color.argb(100, 252, 119, 3));
            iv_wifi.setAlpha(100);
        }




    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                Bitmap bitmap = null;
//                Log.e(TAG, "onActivityResult: ");
//                bitmap = ImageUtils.getBitmap(getActivity());
//                myAsyncTask = new DetailFragment.AsyncTax();
//                myAsyncTask.execute(bitmap);
//                Log.d(TAG, "onActivityResult: " + bitmap);
//            }
//
//        } else if (requestCode == 2) {
//            Bitmap bitmap = null;
//            if (resultCode == RESULT_OK) {
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                    myAsyncTask = new DetailFragment().AsyncTax();
//                    myAsyncTask.execute(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
//    }


}