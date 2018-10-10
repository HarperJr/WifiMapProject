package h.maps.mapsproject.location;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class GlobalLocationService extends IntentService {

    public static final String RECEIVE_GLOBAL = "h.maps.mapsproject.location.RECEIVE_GLOBAL";
    public static final String EXTRA_LOCATIONS_LIST = "LocationsList";

    public GlobalLocationService() {
        super("GlobalLocationService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
