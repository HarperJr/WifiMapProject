package h.maps.mapsproject.traas;

import android.location.Location;
import android.location.LocationManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoPosition2D;
import de.tudresden.ws.container.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * @author Maxim Berezin
 */
public class TraCIService {
    private static TraCIService serviceInstance;

    private SumoTraciConnection connection;
    private String timeStep;

    public static TraCIService getInstance() {
        if (serviceInstance == null) {
            serviceInstance = new TraCIService();
        }
        return serviceInstance;
    }

    public void connect(String host, int port) throws Exception {
        connection = new SumoTraciConnection(new InetSocketAddress(InetAddress.getByName(host), port));

        setTimeStep("0.03");

        if (connection != null) {
            //Waiting for response
            connection.addOption("step-length", timeStep);
            connection.do_timestep();
        }
    }

    public TraCIService setTimeStep(String timeStep) {
        this.timeStep = timeStep;
        return this;
    }

    public ArrayList<Location> getAll() throws Exception {
        final ArrayList<Location> locations = new ArrayList<>();

        if (connection != null && !connection.isClosed()) {
            for(final String idListElement : (SumoStringList)connection.do_job_get(Vehicle.getIDList())) {

                final double speed = (double) connection.do_job_get(Vehicle.getSpeed(idListElement));
                final double angle = (double) connection.do_job_get(Vehicle.getAngle(idListElement));
                final SumoPosition2D coordinates = (SumoPosition2D) connection.do_job_get(Vehicle.getPosition(idListElement));
                final SumoPosition2D position = (SumoPosition2D) connection.do_job_get(Simulation.convertGeo(coordinates.x, coordinates.y, false));

                final Location location = new Location(LocationManager.NETWORK_PROVIDER);

                location.setLongitude(position.x);
                location.setLatitude(position.y);
                location.setSpeed((float) speed);
                location.setBearing((float) angle);

                locations.add(location);
            }
        }

        return locations;
    }

    public void doTimeStep() throws Exception {
        if (connection != null) {
            connection.do_timestep();
        }
    }
}
