package h.maps.mapsproject.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.nio.ByteBuffer;

import h.maps.mapsproject.http.HTTPRequestHandler;

/**
 * @author Maxim Berezin
 */
public class GlobalLocationReceiver extends BroadcastReceiver {

    private final HTTPRequestHandler.Callback callback;

    public GlobalLocationReceiver(HTTPRequestHandler.Callback callback) {
        this.callback = callback;
    }

    /*
     * Ресивер получает массив байт, полученных в результате запроса сервиса к серверу.
     * Нужно десериализовать пакет в отдельном потоке
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        final byte[] result = intent.getByteArrayExtra("Result");

        if (callback != null) {
            callback.onReceive(ByteBuffer.wrap(result));
        }
    }
}
