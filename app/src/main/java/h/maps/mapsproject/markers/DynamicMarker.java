package h.maps.mapsproject.markers;

import android.graphics.Canvas;
import android.location.Location;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import h.maps.mapsproject.R;

public class DynamicMarker extends MapMarker {

    public DynamicMarker(MapView mapView) {
        super(mapView);
    }

    @Override
    public void setProperties() {

    }

    @Override
    public int getIconImage() {
        return R.drawable.nav_arrow;
    }

    @Override
    public void onLocationChanged(Location location) {
        setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        setRotation(location.getBearing());
    }
}
