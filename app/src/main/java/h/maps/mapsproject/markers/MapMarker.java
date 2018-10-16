package h.maps.mapsproject.markers;

import android.location.Location;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import h.maps.mapsproject.R;
import h.maps.mapsproject.location.LocationHandler;

/**
 * @author Maxim Berezin
 */
public abstract class MapMarker extends Marker implements LocationHandler.Callback {

    protected MapView map;

    public MapMarker(MapView map) {
        super(map);
        this.map = map;

        setProperties();
        setIcon(map.getContext().getDrawable(getIconImage()));
    }

    public abstract void setProperties();

    public abstract int getIconImage();

    @Override
    public void onLocationChanged(Location location) {
        //Todo update when my location changes
    }

    @Override
    public void onStatusChanged(String s) {
        //Todo update when my location status changes
    }
}
