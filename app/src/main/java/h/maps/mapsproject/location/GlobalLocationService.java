package h.maps.mapsproject.location;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class GlobalLocationService extends IntentService {

    public static final String NOTIFICATION = "h.maps.mapsproject.location.receiver";

    public GlobalLocationService() {
        super("GlobalLocationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
