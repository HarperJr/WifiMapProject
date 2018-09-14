package h.maps.mapsproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import h.maps.mapsproject.location.GlobalLocationService;
import h.maps.mapsproject.location.LocationHandler;
import h.maps.mapsproject.map.MapFragment;

public class MainActivity extends AppCompatActivity {

    private static final List<String> PERMS;
    private static final int REQ_CODE = 0x1001;

    private LocationHandler locationHandler;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissionsForLocationUpdates();
        //Init map
        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        final Intent serviceIntent = new Intent(this, GlobalLocationService.class);
        startService(serviceIntent);
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
                    Toast.makeText(this, getString(R.string.permissions_toast), Toast.LENGTH_LONG).show();
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
        if (locationHandler != null) {
            locationHandler.startUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationHandler != null) {
            locationHandler.stopUpdates();
        }
    }

    private LocationHandler.Callback locationCallback = new LocationHandler.Callback() {
        @Override
        public void onLocationChanged(Location location) {
            if (mapFragment != null) mapFragment.onLocationChanged(location);
        }

        @Override
        public void onStatusChanged(String s) {
            if (mapFragment != null) mapFragment.onStatusChanged(s);
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
