package com.example.admins.snaphotel.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class TurnOnGPSActivity extends AppCompatActivity {
    private static final String TAG = "ABCXYZ";
    public static LatLng currentLocation;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_on_gps);
        if (Build.VERSION.SDK_INT >= 23) {
            setupPermission();
        } else

        {
            getCurrentLocation();
        }
        this.setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) TurnOnGPSActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(TurnOnGPSActivity.this)) {
            Toast.makeText(TurnOnGPSActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(TurnOnGPSActivity.this)) {
            Toast.makeText(TurnOnGPSActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(TurnOnGPSActivity.this)) {
            Log.e("keshav", "Gps already enabled");
            Toast.makeText(TurnOnGPSActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.e("keshav", "Gps already enabled");
            Toast.makeText(TurnOnGPSActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(TurnOnGPSActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // hiển thị dialog bằng cách StarResolution
                                // kiểm tra kết quả ở onActivityResult()

                                status.startResolutionForResult(TurnOnGPSActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // bỏ qua lỗi
                            }
                            break;
                    }
                }
            });

        }


    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Intent intent = new Intent(TurnOnGPSActivity.this, MainActivity.class);
                startActivity(intent);
                Log.d(TAG, "onLocationChanged: " + currentLocation);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, locationListener);
    }


    private void setupPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }
}
