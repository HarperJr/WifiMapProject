package h.maps.mapsproject.location;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class GlobalLocationService extends IntentService {

    public static final String NOTIFICATION = "h.maps.mapsproject.location.RECEIVE_GLOBAL";
    public static final String EXTRA_MESsaGE_CONTENT = "TIMESTEP_UPDATED_LOCATIONS";

    public GlobalLocationService() {
        super("GlobalLocationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
