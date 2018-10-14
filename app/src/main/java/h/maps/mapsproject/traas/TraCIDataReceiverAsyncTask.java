package h.maps.mapsproject.traas;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;

import h.maps.mapsproject.location.GlobalUpdatesService;

public class TraCIDataReceiverAsyncTask extends AsyncTask<Void, Void, ArrayList<Location>> {

    private OnDataReceivedListener onDataReceivedListener;

    public interface OnDataReceivedListener {
        void onReceive(ArrayList<Location> locations);
    }

    public TraCIDataReceiverAsyncTask setOnDataReceivedListener(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
        return this;
    }

    @Override
    protected ArrayList<Location> doInBackground(Void... voids) {
        ArrayList<Location> updatedLocations = null;
        try {
            final TraCIService traCIService = TraCIService.getInstance();
            updatedLocations = traCIService.getAll();

            traCIService.doTimeStep();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return updatedLocations;
    }

    @Override
    protected void onPostExecute(ArrayList<Location> locations) {
        if (onDataReceivedListener != null) {
            onDataReceivedListener.onReceive(locations);
        }
    }
}
