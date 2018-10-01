package h.maps.mapsproject.http;

import java.nio.ByteBuffer;

/**
 * @author Maxim Berezin
 */
public class HTTPRequestHandlerBuffered extends HTTPRequestHandler {

    public interface Callback {
        void onReceive(String s);
    }

    public void doRequest(String request, final HTTPRequestHandlerBuffered.Callback callback) throws Exception {
        final HTTPRequestHandler.Callback httpCallback = new HTTPRequestHandler.Callback() {
            @Override
            public void onReceive(ByteBuffer buffer) {
                callback.onReceive(buffer.toString());
            }
        };
        super.doRequest(request, httpCallback);
    }
}
