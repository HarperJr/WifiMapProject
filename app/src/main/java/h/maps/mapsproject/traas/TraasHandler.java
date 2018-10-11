package h.maps.mapsproject.traas;

import android.location.Location;
import android.location.LocationManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoPosition2D;
import de.tudresden.ws.container.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * @author Maxim Berezin
 */
public class TraasHandler {
    private SumoTraciConnection connection;
    private float timeStep;

    public void connect(String host, int port) throws Exception {
        connection = new SumoTraciConnection(new InetSocketAddress(InetAddress.getByName(host), port));
        connection.addOption("step-length", new StringBuilder().append(timeStep).toString());
        try {
            connection.do_timestep();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public TraasHandler withTimeStep(float timeStep) {
        this.timeStep = timeStep;
        return this;
    }

    public ArrayList<Location> getAll() throws Exception {
        final ArrayList<Location> locations = new ArrayList<>();

        if (connection != null && !connection.isClosed()) {
            for(final String idListElement : (SumoStringList)connection.do_job_get(Vehicle.getIDList())) {

                float speed = (float) connection.do_job_get(Vehicle.getSpeed(idListElement));
                SumoPosition2D position = (SumoPosition2D)connection.do_job_get(Vehicle.getPosition(idListElement));

                final Location location = new Location(LocationManager.NETWORK_PROVIDER);

                location.setLatitude(position.x);
                location.setLongitude(position.y);
                location.setSpeed(speed);

                locations.add(location);
            }
        }

        return locations;
    }

    public void doTimeStep() throws Exception {
        try {
            connection.do_timestep();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
