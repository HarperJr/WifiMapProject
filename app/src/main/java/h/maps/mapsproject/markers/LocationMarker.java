package h.maps.mapsproject.markers;

import android.location.Location;

import h.maps.mapsproject.R;
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
        setSnippet("My location");
    }

    @Override
    protected int selectIcon() {
        return R.drawable.person;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        setPosition(new GeoPoint(lat, lon));
    }
}
