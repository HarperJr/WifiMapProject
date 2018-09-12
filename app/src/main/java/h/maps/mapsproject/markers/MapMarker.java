package h.maps.mapsproject.markers;

import android.location.Location;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import h.maps.mapsproject.location.LocationHandler;

/**
 * @author Maxim Berezin
 */
public class MapMarker extends Marker implements LocationHandler.Callback {

    public MapMarker(MapView map) {
        super(map);
    }

    @Override
    public void onLocationChanged(Location location) {
        //Todo
    }

    @Override
    public void onStatusChanged(String s) {
        //Todo
    }
}
