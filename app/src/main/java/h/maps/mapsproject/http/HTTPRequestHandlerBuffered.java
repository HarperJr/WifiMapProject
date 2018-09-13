package h.maps.mapsproject.http;

import java.nio.ByteBuffer;

/**
 * @author Maxim Berezin
 */
public class HTTPRequestHandlerBuffered extends HTTPRequestHandler {

    public interface Callback {
        void onReceive(Status status, String s);
    }

    public void doRequest(String request, final HTTPRequestHandlerBuffered.Callback callback) throws Exception {
        final HTTPRequestHandler.Callback httpCallback = new HTTPRequestHandler.Callback() {
            @Override
            public void onReceive(Status status, ByteBuffer buffer) {
                callback.onReceive(status, buffer.toString());
            }
        };
        super.doRequest(request, httpCallback);
    }
}
