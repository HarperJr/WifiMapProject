package h.maps.mapsproject.location;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

import h.maps.mapsproject.traas.TraCIDataReceiverAsyncTask;
import h.maps.mapsproject.traas.TraCIService;

public class GlobalUpdatesService extends JobService {
    public static final int JOB_ID = 0x63662;

    public static final String LOCATION_UPDATES = "h.maps.mapsproject.broadcast.GLOBAL_UPDATE";
    public static final String EXTRA_LOCATION_UPDATE = "locationuUdate";

    //Will be changed
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        new TraCIDataReceiverAsyncTask()
                .setOnDataReceivedListener(new TraCIDataReceiverAsyncTask.OnDataReceivedListener() {
                    @Override
                    public void onReceive(ArrayList<Location> locations) {
                        if (locations == null) {
                            stopSelf();
                        }

                        final Bundle bundle = new Bundle();

                        bundle.putParcelableArrayList(GlobalUpdatesService.EXTRA_LOCATION_UPDATE, locations);
                        final Intent broadcastIntent = new Intent(GlobalUpdatesService.LOCATION_UPDATES);
                        broadcastIntent.putExtras(bundle);

                        getApplicationContext().sendBroadcast(broadcastIntent);
                    }
                })
                .execute();

        jobFinished(jobParameters, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
