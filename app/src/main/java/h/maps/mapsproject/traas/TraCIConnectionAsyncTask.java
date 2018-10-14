package h.maps.mapsproject.traas;

import android.os.AsyncTask;

public class TraCIConnectionAsyncTask extends AsyncTask<String, Void, Boolean> {

    private OnUpdateListener onUpdateListener;

    public interface OnUpdateListener {
        void onPreUpdate();
        void onUpdate();
        void onPostUpdate();
    }

    public TraCIConnectionAsyncTask setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
        return this;
    }

    @Override
    protected void onPreExecute() {
        onUpdateListener.onPreUpdate();
    }

    @Override
    protected Boolean doInBackground(String... data) {
        final TraCIService traCIService = TraCIService.getInstance();
        try {
            traCIService.connect(data[0], Integer.parseInt(data[1]));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        onUpdateListener.onUpdate();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            onUpdateListener.onPostUpdate();
        }
    }
}

