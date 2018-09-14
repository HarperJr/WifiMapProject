package h.maps.mapsproject.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import h.maps.mapsproject.location.GlobalLocationListener;

/**
 * @author Maxim Berezin
 */
public class GlobalLocationReceiver extends BroadcastReceiver {

    private GlobalLocationListener locationListener;
    private Handler globalHandler;

    public GlobalLocationReceiver() {
        globalHandler = new Handler();
    }

    public void setGlobalLocationListener(GlobalLocationListener locationListener) {
        this.locationListener = locationListener;
    }

    /*
     * Ресивер получает массив байт, полученных в результате запроса сервиса к серверу.
     * Нужно десериализовать пакет в отдельном потоке
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        final String result = intent.getStringExtra("Result");

        final Runnable packageHandlingThread = new Runnable() {
            @Override
            public void run() {
                if (result != null) {

                    try {
                        final JSONObject json = new JSONObject(result);
                        //Process json
                        if (locationListener != null) {
                            locationListener.onReceiveGlobal(/*Model is required*/);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        globalHandler.post(packageHandlingThread);
    }
}
