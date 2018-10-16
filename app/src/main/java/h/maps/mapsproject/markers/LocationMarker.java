package h.maps.mapsproject.markers;

import android.graphics.drawable.Drawable;
import android.location.Location;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import h.maps.mapsproject.R;

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
    public int getIconImage() {
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
