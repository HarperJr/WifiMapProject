package h.maps.mapsproject.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

/**
 * @author Maxim Berezin
 */
public class LocationHandler {

    private static final long MIN_TIME_INTERVAL = 1000;

    private final Context context; //Unused for a while
    private final FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest request;

    private Callback callback;

    public interface Callback {
        void onLocationChanged(Location location);
        void onStatusChanged(String s);
    }

    public LocationHandler(Context context) {
        this.context = context;

        request = new LocationRequest().setInterval(MIN_TIME_INTERVAL).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder();
        settingsBuilder.addLocationRequest(request);

        final SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(settingsBuilder.build());

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public LocationHandler setLocationUpdateCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public void startUpdates() throws SecurityException {
        if (callback == null || fusedLocationProviderClient == null) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, null);
    }

    public void stopUpdates() {
        if (fusedLocationProviderClient == null) return;
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }

            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    callback.onLocationChanged(location);
                }
            }
        }
    };

}
