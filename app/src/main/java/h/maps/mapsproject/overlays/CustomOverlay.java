package h.maps.mapsproject.overlays;

import android.graphics.Point;

import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import h.maps.mapsproject.R;

/**
 * @author Maxim Berezin
 */
public class CustomOverlay extends ItemizedOverlay<OverlayItem> {
    private List<OverlayItem> items = new ArrayList<>();

    public CustomOverlay(MapView map) {
        super(map.getContext().getDrawable(R.drawable.direction_arrow));
    }

    public void addOverlay(OverlayItem item) {
        if (items.contains(item)) return;
        items.add(item);
        populate();
    }

    public void removeOverlay(OverlayItem item) {
        if (!items.contains(item)) return;
        items.remove(item);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return items.get(i);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean onSnapToItem(int x, int y, Point snapPoint, IMapView mapView) {
        return false;
    }
}
