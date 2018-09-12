package h.maps.mapsproject.markers;

import android.location.Location;

import org.osmdroid.views.MapView;

/**
 * @author Maxim Berezin
 */
public class DynamicMarker extends MapMarker {

    public DynamicMarker(MapView map) {
        super(map);
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
    }
}
