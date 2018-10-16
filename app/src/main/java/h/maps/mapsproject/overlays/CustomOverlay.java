package h.maps.mapsproject.overlays;

import android.graphics.Canvas;
import android.location.Location;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import java.util.LinkedList;
import java.util.List;

import h.maps.mapsproject.markers.DynamicMarker;

public class CustomOverlay extends Overlay {

    private final MapView mapView;
    private final List<DynamicMarker> markers = new LinkedList<>();

    public CustomOverlay(MapView mapView) {
        this.mapView = mapView;
    }

    public int size() {
        return markers.size();
    }

    public boolean addMarker(Location location) {
        final DynamicMarker marker = new DynamicMarker(mapView);
        marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        return markers.add(marker);
    }

    public boolean removeMarker(int i) {
        if (i < size()) {
            return markers.remove(i) != null;
        }
        return false;
    }

    public DynamicMarker getMarker(int i) {
        if (i < size()) {
            return markers.get(i);
        }
        return null;
    }

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
        for (DynamicMarker marker : markers) {
            if (marker != null) {
                marker.draw(c, osmv, shadow);
            }
        }
    }

    @Override
    public boolean onDoubleTap(MotionEvent e, MapView mapView) {

        for (DynamicMarker marker : markers) {
            if (marker != null) {
                if (marker.onDoubleTap(e, mapView)) {
                    return true;
                }
            }
        }

        return false;
    }
}


