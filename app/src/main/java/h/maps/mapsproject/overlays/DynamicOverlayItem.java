package h.maps.mapsproject.overlays;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * @author Maxim Berezin
 */
public class DynamicOverlayItem extends OverlayItem {

    public DynamicOverlayItem(String name, String snippet, IGeoPoint geoPoint) {
        super(name, snippet, geoPoint);
    }


}
