package h.maps.mapsproject.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

import h.maps.mapsproject.traas.TraasHandler;


public class GlobalLocationService extends Service {

    public static final String RECEIVE_GLOBAL_UPDATES = "h.maps.mapsproject.location.RECEIVE_GLOBAL_UPDATES";
    public static final String EXTRA_LOCATIONS_LIST = "LocationsList";

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, final int startId) {
        final Bundle extras = intent.getExtras();
        final Handler serviceHandler = new Handler();
        final TraasHandler traasHandler = new TraasHandler().withTimeStep(extras.getFloat("timeStep"));
        final long updateMills = extras.getLong("updateMills");

        try {
            traasHandler.connect(extras.getString("host"), extras.getInt("port"));
            serviceHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        final ArrayList<Location> locationsUpdate = traasHandler.getAll();

                        final Bundle locationsBundle = new Bundle();
                        locationsBundle.putParcelableArrayList(EXTRA_LOCATIONS_LIST, locationsUpdate);

                        final Intent broadcastIntentUpdates = new Intent(RECEIVE_GLOBAL_UPDATES);
                        broadcastIntentUpdates.putExtra(EXTRA_LOCATIONS_LIST, locationsBundle);

                        sendBroadcast(broadcastIntentUpdates);

                        traasHandler.doTimeStep();
                        serviceHandler.postDelayed(this, updateMills);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, updateMills);

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
