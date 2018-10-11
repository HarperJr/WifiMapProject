package h.maps.mapsproject.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import h.maps.mapsproject.traas.TraasHandler;


public class GlobalLocationService extends Service {

    public static final String GlOBAL_LOCATIONS_UPDATE_SERVICE = "h.maps.mapsproject.location.UPDATE_SERVICE";
    public static final String RECEIVE_GLOBAL = "h.maps.mapsproject.location.RECEIVE_GLOBAL";
    public static final String EXTRA_LOCATIONS_LIST = "LocationsList";

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        final Bundle extras = intent.getExtras();
        final Handler serviceHandler = new Handler();
        final TraasHandler traasHandler = new TraasHandler().withTimeStep(extras.getFloat("timeStep"));

        try {
            traasHandler.connect(extras.getString("host"), extras.getInt("port"));
            serviceHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        final ArrayList<Location> locationsUpdate = traasHandler.getAll();

                        if (locationsUpdate == null) {
                            GlobalLocationService.this.stopSelf();
                        }

                        final Bundle locationsBundle = new Bundle();
                        locationsBundle.putParcelableArrayList(EXTRA_LOCATIONS_LIST, locationsUpdate);

                        final Intent broadcastIntent = new Intent(RECEIVE_GLOBAL);
                        broadcastIntent.putExtra(EXTRA_LOCATIONS_LIST, locationsBundle);

                        sendBroadcast(broadcastIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, extras.getLong("delayMills"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
