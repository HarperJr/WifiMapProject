package h.maps.mapsproject.markers;

import android.location.Location;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * @author Maxim Berezin
 */
public class LocationMarker extends MapMarker {

    public LocationMarker(MapView map) {
        super(map);
    }

    @Override
    public void setProperties() {

    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        setPosition(new GeoPoint(lat, lon));
    }
}
