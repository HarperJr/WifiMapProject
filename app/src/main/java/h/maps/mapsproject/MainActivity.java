package h.maps.mapsproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import h.maps.mapsproject.location.LocationHandler;

public class MainActivity extends AppCompatActivity {

    private static final List<String> PERMS;
    private static final int REQ_CODE = 0x1001;

    private LocationHandler locationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissionsForLocationUpdates();


        //Init map
        setContentView(R.layout.activity_main);
    }

    private void requestPermissionsForLocationUpdates() {
        final List<String> required = new ArrayList<>();

        for (String perm : PERMS) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                    required.add(perm);
                }
            }
        }

        if (!required.isEmpty()) {
            ActivityCompat.requestPermissions(this, required.toArray(new String[required.size()]), REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE: {
                boolean shouldRequest = false;
                for (int res : grantResults) {
                    if (res == PackageManager.PERMISSION_DENIED) {
                        shouldRequest = true;
                        break;
                    }
                }

                if (shouldRequest) {
                    requestPermissionsForLocationUpdates();
                } else {
                    if (locationHandler == null) {
                        locationHandler = new LocationHandler(this, locationCallback);
                        locationHandler.startUpdates();
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationHandler.startUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationHandler.stopUpdates();
    }

    private LocationHandler.Callback locationCallback = new LocationHandler.Callback() {
        @Override
        public void onLocationChanged(Location location) {
            //Update location state
        }

        @Override
        public void onStatusChanged(String s) {
            //Update status
        }
    };

    static {
        PERMS = Arrays.asList(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        );
    }

}
