package h.maps.mapsproject.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import h.maps.mapsproject.R;
import h.maps.mapsproject.location.GlobalLocationIntentService;
import h.maps.mapsproject.location.LocationHandler;
import h.maps.mapsproject.fragments.MapFragment;
import h.maps.mapsproject.traas.TraCIService;

public class MainActivity extends AppCompatActivity {

    private static final List<String> PERMS;
    private static final int REQ_CODE = 0x1001;

    private LocationHandler locationHandler;
    private MapFragment mapFragment;

    private ProgressBar progressBar;
    private ImageButton connectionButton;

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (mapFragment != null) {
                    mapFragment.getMapView().invalidate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private final BroadcastReceiver globalLocationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            final List<Location> locationExtras = bundle.getParcelableArrayList(GlobalLocationIntentService.EXTRA_GLOBAL_UPDATES);

            if (mapFragment != null) {
                mapFragment.onReceiveGlobal(locationExtras);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions();

        setContentView(R.layout.activity_main);

        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(globalLocationsReceiver, new IntentFilter(GlobalLocationIntentService.ACTION_GLOBAL_UPDATES));

        locationHandler = new LocationHandler(this).setLocationUpdateCallback(locationCallback);

        /**Views and fragments instances**/
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        progressBar = findViewById(R.id.progressBar);

        connectionButton = findViewById(R.id.connectionButton);
        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TraCIConnectivityTask()
                        .execute("192.168.137.1", "36332");
            }
        });

    }

    private void requestPermissions() {
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
                    requestPermissions();
                    Toast.makeText(this, getString(R.string.permissions_toast), Toast.LENGTH_LONG).show();
                } else {
                    if (locationHandler != null) {
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
            locationHandler.stopUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationHandler != null) {
            locationHandler.startUpdates();
        }
    }

    private LocationHandler.Callback locationCallback = new LocationHandler.Callback() {
        @Override
        public void onLocationChanged(Location location) {
            if (mapFragment != null) {
                mapFragment.onLocationChanged(location);
            }
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
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE

        );
    }

    class TraCIConnectivityTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... data) {
            final TraCIService traCIService = TraCIService.getInstance();
            try {
                traCIService.connect(data[0], Integer.parseInt(data[1]));
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressBar.animate();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                final TimerTask asyncTask = new TimerTask() {
                    @Override
                    public void run() {
                        final Intent serviceIntent = new Intent(getApplicationContext(), GlobalLocationIntentService.class);
                        serviceIntent.setAction(GlobalLocationIntentService.ACTION_REQUEST_GLOBAL);

                        startService(serviceIntent);
                    }
                };

                new Timer().schedule(asyncTask, 0, 250);

                Toast.makeText(MainActivity.this, "Connected to TraCI", Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }
    }

}
