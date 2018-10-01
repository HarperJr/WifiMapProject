package h.maps.mapsproject.markers;

import android.location.Location;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import h.maps.mapsproject.location.LocationHandler;

/**
 * @author Maxim Berezin
 */
public abstract class MapMarker extends Marker implements LocationHandler.Callback {

    public MapMarker(MapView map) {
        super(map);
        setProperties();
    }

    public abstract void setProperties();

    @Override
    public void onLocationChanged(Location location) {
        //Todo update when my location changes
    }

    @Override
    public void onStatusChanged(String s) {
        //Todo update when my location status changes
    }
}
