package h.maps.mapsproject.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * @author Maxim Berezin
 */
public class HTTPRequestHandler {

    public interface Callback {
        void onReceive(ByteBuffer buffer);
    }

    public void doRequest(String request, Callback callback) throws Exception {
        if (callback == null) return;
        final HttpURLConnection connection = (HttpURLConnection) new URL(request).openConnection();

        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        if (connection != null) {
            connection.connect();

            try (BufferedInputStream stream = new BufferedInputStream(connection.getInputStream())) {
                int result = stream.read();

                while (result != -1) {
                    bytes.write(result);
                    result = stream.read();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                connection.disconnect();
            }
        }

        callback.onReceive(ByteBuffer.wrap(bytes.toByteArray()));
    }



}
