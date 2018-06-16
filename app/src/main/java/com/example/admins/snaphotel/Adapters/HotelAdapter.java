package com.example.admins.snaphotel.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admins.snaphotel.Model.HotelModel;
import com.example.admins.snaphotel.Ultis.ImageUtils;
import com.example.admins.snaphotel.Ultis.onClickMyHotel;
import com.example.admins.snaphotel.fragment.EditHotelFragment;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admins on 6/12/2018.
 */

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewholder> {
    private static final String TAG = HotelAdapter.class.toString();
    private Context context;
    private List<HotelModel> hotelModels;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RelativeLayout rlMain;
    FragmentManager fragmentManager;


    public HotelAdapter(FragmentManager fragmentManager, Context context, List<HotelModel> hotelModels) {
        this.context = context;
        this.hotelModels = hotelModels;
        this.fragmentManager = fragmentManager;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Log.d(TAG, "HotelAdapter: ");
    }

    @Override
    public HotelAdapter.HotelViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_hotel_fix, parent, false);
        return new HotelViewholder(view);
    }

    @Override
    public void onBindViewHolder(HotelViewholder holder, int position) {
        holder.setData(hotelModels.get(position));
    }


    @Override
    public int getItemCount() {
        return hotelModels.size();
    }

    public class HotelViewholder extends RecyclerView.ViewHolder {
        ImageView ivMenu, ivImage;
        TextView tvName, tvPrice, tvAddress, tvEdit;
        RatingBar rbStar;
        View itemView;

        public HotelViewholder(final View itemView) {
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
            String giaDoi = hotelModel.gia.substring(hotelModel.gia.indexOf("-")+1);
            tvPrice.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDon))+"VNĐ" +" -  " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(giaDoi))+ "VNĐ");
            ivImage.setImageBitmap(ImageUtils.base64ToImage(hotelModel.images.get(0)));

//            PopupMenu popupMenu = new PopupMenu(context, ivMenu);
//            popupMenu.inflate(R.menu.main);
//            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem menuItem) {
//                    switch (menuItem.getItemId()) {
//                        case R.id.action_settings:
//                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//                            dialogBuilder.setMessage("Bạn chắc chắn muốn xóa?")
//                                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//
//                                        }
//                                    })
//                                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//
//                                        }
//                                    })
//                                    .show();
//                            break;
//
//
//                        case R.id.action_edit:
//                            ImageUtils.openFragment(fragmentManager ,R.id.rl_main,new EditHotelFragment() );
//                            EventBus.getDefault().postSticky(new onClickMyHotel(hotelModel));
//
//                            break;
//                    }
//                    return true;
//                }
//            });
//            popupMenu.show();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    ImageUtils.openFragment(fragmentManager, R.id.rl_main, new EditHotelFragment());
                    EventBus.getDefault().postSticky(new onClickMyHotel(hotelModel));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    Log.d(TAG, "onLongClick: ");
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setMessage("Bạn chắc chắn muốn xóa?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseAuth = FirebaseAuth.getInstance();
                                    firebaseDatabase = FirebaseDatabase.getInstance();


                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            })
                            .show();
                    return false;
                }
            });

//            rlMain=itemView.findViewById(R.id.rl_main);
//            ivMenu.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//                    @Override
//                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
////                    menu.setHeaderTitle("Select The Action");

//
//                    }
//            });
//            itemView.setOnCreateContextMenuListener(this);
//            tvEdit = itemView.findViewById(R.id.tv_edit);
//            tvEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {


//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }
//
//
//            });
        }
    }


}