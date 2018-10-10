package h.maps.mapsproject.location;

import android.location.Location;

import java.util.List;

/**
 * @author Maxim Berezin
 */
public interface GlobalLocationListener {
    void onReceiveGlobal(List<Location> locations);
}
