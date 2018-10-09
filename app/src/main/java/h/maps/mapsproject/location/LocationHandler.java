package h.maps.mapsproject.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

/**
 * @author Maxim Berezin
 */
public class LocationHandler {

    private static final int MIN_TIME = 1000;
    private static final int MIN_DISTANCE = 0;

    private final Context context;
    private final LocationManager locationManager;

    private Callback callback;

    public interface Callback {
        void onLocationChanged(Location location);
        void onStatusChanged(String s);
    }

    public LocationHandler(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startUpdates() throws SecurityException {
        if (callback == null && locationManager == null) {
            return;
        }
        final boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
            final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                callback.onLocationChanged(location);
            }
        } else {
            Toast.makeText(context, "GPS provider disabled", Toast.LENGTH_LONG).show();
        }
    }

    public void stopUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(context, "Update", Toast.LENGTH_LONG).show();
            if (location != null) {
                callback.onLocationChanged(location);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            callback.onStatusChanged(s);
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            final Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(locationIntent);
        }
    };

}
