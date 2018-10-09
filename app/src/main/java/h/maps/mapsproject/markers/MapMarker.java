package h.maps.mapsproject.markers;

import android.graphics.drawable.Drawable;
import android.location.Location;

import android.view.MotionEvent;
import android.view.View;
import h.maps.mapsproject.R;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import h.maps.mapsproject.location.LocationHandler;

/**
 * @author Maxim Berezin
 */
public abstract class MapMarker extends Marker implements LocationHandler.Callback, View.OnTouchListener {

    protected MapView map;

    public MapMarker(MapView map) {
        super(map);
        this.map = map;

        setProperties();
        setIcon(map.getContext().getDrawable(selectIcon()));
    }

    public abstract void setProperties();

    protected int selectIcon() {
        return R.drawable.moreinfo_arrow;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Todo update when my location changes
    }

    @Override
    public void onStatusChanged(String s) {
        //Todo update when my location status changes
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //show tab
            }
        }
        return v.performClick();
    }
}
