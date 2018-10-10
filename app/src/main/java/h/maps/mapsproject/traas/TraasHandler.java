package h.maps.mapsproject.traas;

import android.location.Location;
import android.location.LocationManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoPosition2D;
import de.tudresden.ws.container.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * @author Maxim Berezin
 */
public class TraasHandler {

    private List<Location> locationsGlobal = new ArrayList<>();

    private SumoTraciConnection connection;
    private float timeStep;
    private float currentTimeStep;

    public TraasHandler() {
        this.timeStep = 0f;
        this.currentTimeStep = 0f;
    }

    public void connect(String host, int port) throws Exception {
        connection = new SumoTraciConnection(new InetSocketAddress(InetAddress.getByName(String.format("http://%s/SUMO?wsdl", host)), port));
        try {
            connection.do_timestep();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public TraasHandler withTimeStep(float timeStep) {
        this.timeStep = timeStep;
        connection.addOption("step-length", new StringBuilder().append(timeStep).toString());
        return this;
    }

    public List<Location> getAllVehicles() throws Exception {
        for(String idListElement : (SumoStringList)connection.do_job_get(Vehicle.getIDList())) {

            double speed = (double)connection.do_job_get(Vehicle.getSpeed(idListElement));
            SumoPosition2D position = (SumoPosition2D)connection.do_job_get(Vehicle.getPosition(idListElement));

            final int id = Integer.parseInt(idListElement);
            final Location location;
            if (locationsGlobal.size() <= id) {
                location = new Location(LocationManager.NETWORK_PROVIDER);
            } else {
                location = locationsGlobal.get(id);
            }

            location.setLongitude(position.x);
            location.setLatitude(position.y);
            location.setSpeed((float) speed);
        }

        return locationsGlobal;
    }

    public void doTimeStep() throws Exception {
        currentTimeStep += timeStep;
        try {
            connection.do_timestep();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public float getCurrentTimeStep() {
        return currentTimeStep;
    }
}
