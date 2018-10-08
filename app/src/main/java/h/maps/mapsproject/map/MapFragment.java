package h.maps.mapsproject.map;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import h.maps.mapsproject.MapConstant;
import h.maps.mapsproject.R;
import h.maps.mapsproject.location.LocationHandler;
import h.maps.mapsproject.markers.LocationMarker;


public class MapFragment extends Fragment implements LocationHandler.Callback {
    //Map here
    private static final String REQ_ARGS = "&radius=4000&ie=UTF";

    private MapView mapView;
    private Handler mapHandler;

    private LocationMarker locationMarker;
    private SharedPreferences sharedPreferences;

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

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN : {
                        //Todo handle touches on map
                        break;
                    }
                }
                return mapView.performClick();
            }
        };

        mapView.setTileSource(TileSourceFactory.ROADS_OVERLAY_NL);

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

        sharedPreferences = context.getSharedPreferences(MapConstant.PREFS_NAME, Context.MODE_PRIVATE);

        locationMarker = new LocationMarker(mapView);
        mapView.getOverlays().add(locationMarker);
        mapView.getOverlays().add(new ScaleBarOverlay(mapView));


        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(MapConstant.MIN_ZOOM);
        mapView.setMaxZoomLevel(MapConstant.MAX_ZOOM);

        mapView.getController().setZoom(7.0d);

        loadPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();

        final String tileSourceName = sharedPreferences.getString(MapConstant.PREFS_TILE_SOURCE,
                TileSourceFactory.DEFAULT_TILE_SOURCE.name());
        try {
            final ITileSource tileSource = TileSourceFactory.getTileSource(tileSourceName);
            mapView.setTileSource(tileSource);
        } catch (final IllegalArgumentException e) {
            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(MapConstant.PREFS_TILE_SOURCE, mapView.getTileProvider().getTileSource().name());
        edit.putString(MapConstant.PREFS_ORIENTATION_STRING, String.valueOf(mapView.getMapOrientation()));
        edit.putString(MapConstant.PREFS_LATITUDE_STRING, String.valueOf(mapView.getMapCenter().getLatitude()));
        edit.putString(MapConstant.PREFS_LONGITUDE_STRING, String.valueOf(mapView.getMapCenter().getLongitude()));
        edit.putString(MapConstant.PREFS_ZOOM_LEVEL_STRING, String.valueOf(mapView.getZoomLevelDouble()));
        edit.apply();
        mapView.onPause();
    }

    private void loadPreferences() {
        final String zoomLevel = sharedPreferences.getString(MapConstant.PREFS_ZOOM_LEVEL_STRING, null);

        if (zoomLevel != null) {
            mapView.getController().setZoom(Double.valueOf(zoomLevel));
        }

        final String orientation = sharedPreferences.getString(MapConstant.PREFS_ORIENTATION_STRING, null);
        if (orientation != null) {
            mapView.setMapOrientation(Float.valueOf(orientation), false);
        }

        final String latitudeString = sharedPreferences.getString(MapConstant.PREFS_LATITUDE_STRING, null);
        final String longitudeString = sharedPreferences.getString(MapConstant.PREFS_LONGITUDE_STRING, null);
        if (latitudeString == null || longitudeString == null) {
            final int scrollX = sharedPreferences.getInt(MapConstant.PREFS_SCROLL_X, 0);
            final int scrollY = sharedPreferences.getInt(MapConstant.PREFS_SCROLL_Y, 0);

            mapView.scrollTo(scrollX, scrollY);
        } else {
            final double latitude = Double.valueOf(latitudeString);
            final double longitude = Double.valueOf(longitudeString);

            mapView.setExpectedCenter(new GeoPoint(latitude, longitude));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationMarker != null) locationMarker.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s) {

    }
}
