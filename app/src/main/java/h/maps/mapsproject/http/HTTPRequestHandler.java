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

    public enum Status {
        ACCEPTED, INVALID
    }

    public interface Callback {
        void onReceive(Status status, ByteBuffer buffer);
    }

    public void doRequest(String request, Callback callback) throws Exception {
        if (callback == null) return;
        final HttpURLConnection connection = (HttpURLConnection) new URL(request).openConnection();

        Status status = Status.INVALID;
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        if (connection != null) {
            connection.connect();

            try (BufferedInputStream stream = new BufferedInputStream(connection.getInputStream())) {
                int result = stream.read();

                while (result != -1) {
                    bytes.write(result);
                    result = stream.read();
                }

                status = Status.ACCEPTED;
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                connection.disconnect();
            }
        }

        callback.onReceive(status, ByteBuffer.wrap(bytes.toByteArray()));
    }



}
