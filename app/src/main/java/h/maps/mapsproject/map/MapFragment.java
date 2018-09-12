package h.maps.mapsproject.map;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

import h.maps.mapsproject.R;
import h.maps.mapsproject.location.LocationHandler;
import h.maps.mapsproject.markers.LocationMarker;


public class MapFragment extends Fragment implements LocationHandler.Callback {
    //Map here

    private MapView mapView;
    private Handler mapHandler;

    private LocationMarker locationMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mapHandler = new Handler();
        mapView = new MapView(inflater.getContext()) {
            @Override
            public boolean onGenericMotionEvent(MotionEvent event) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
                        && 0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_SCROLL:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
                                    && event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f)
                                mapView.getController().zoomOut();
                            else {
                                IGeoPoint iGeoPoint = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                                mapView.getController().animateTo(iGeoPoint);
                                mapView.getController().zoomIn();
                            }
                            return true;
                    }
                }
                return false;
            }
        };
        final View mapFragmentView = inflater.inflate(R.layout.fragment_osmmap, container, false);
        final FrameLayout mapLayout = mapFragmentView.findViewById(R.id.map_container);

        if (mapLayout != null) {
            mapLayout.addView(mapView);
        }

        return mapFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Context context = getActivity();

        locationMarker = new LocationMarker(mapView);
        mapView.getOverlays().add(locationMarker);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationMarker != null) locationMarker.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s) {

    }
}
