package h.maps.mapsproject.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @author Maxim Berezin
 */
public class LocationHandler {

    private static final int MIN_TIME = 10000;
    private static final int MIN_DISTANCE = 10;

    private final Context context; //unused for a while
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
        if (callback == null) {
            return;
        }

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
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

        }
    };

}
