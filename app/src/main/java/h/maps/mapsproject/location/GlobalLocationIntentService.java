package h.maps.mapsproject.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import h.maps.mapsproject.traas.TraCIService;

public class GlobalLocationIntentService extends IntentService {

    public static final String ACTION_REQUEST_GLOBAL = "h.maps.mapsproject.ACTION_REQUEST_GLOBAL";
    public static final String ACTION_GLOBAL_UPDATES = "h.maps.mapsproject.action.ACTION_GLOBAL_UPDATES";
    public static final String EXTRA_GLOBAL_UPDATES = "globalUpdates";

    public GlobalLocationIntentService() {
        super("GlobalLocationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        if (intent.getAction().equals(ACTION_REQUEST_GLOBAL)) {
            try {
                final TraCIService traCIService = TraCIService.getInstance();
                final ArrayList<Location> locations = traCIService.getAll();
                traCIService.doTimeStep();

                if (locations == null) {
                    GlobalLocationIntentService.this.stopSelf();
                    return;
                }

                final Bundle bundle = new Bundle();

                bundle.putParcelableArrayList(EXTRA_GLOBAL_UPDATES, locations);
                final Intent broadcastIntent = new Intent(ACTION_GLOBAL_UPDATES);
                broadcastIntent.putExtras(bundle);

                sendBroadcast(broadcastIntent);
            } catch (Exception ex) {

            }
        }
    }
}
