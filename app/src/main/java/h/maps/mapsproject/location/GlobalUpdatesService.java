package h.maps.mapsproject.location;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

import h.maps.mapsproject.traas.TraCIService;

public class GlobalUpdatesService extends JobService {
    public static final String LOCATION_UPDATES = "h.maps.mapsproject.broadcast.GLOBAL_UPDATE";
    public static final String EXTRA_LOCATION_UPDATE = "locationuUdate";

    private boolean needsUpdates;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!needsUpdates) {
                        return;
                    }
                    final TraCIService traCIService = TraCIService.getInstance();
                    final ArrayList<Location> updatedLocations = traCIService.getAll();

                    final Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList(EXTRA_LOCATION_UPDATE, updatedLocations);
                    final Intent broadcastIntent = new Intent(LOCATION_UPDATES);
                    broadcastIntent.putExtras(bundle);

                    sendBroadcast(broadcastIntent);

                } catch (Exception ex) {
                    jobFinished(jobParameters, false);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        needsUpdates = false;
        return false;
    }
}
