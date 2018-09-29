package sample.sumo;

import it.polito.appeal.traci.SumoTraciConnection;

import static sample.sumo.SumoConstants.*;

public class Sumo {

    private static Sumo instance;
    private final SumoTraciConnection connection;

    public static Sumo getInstance() {
        if (instance == null) {
            instance = new Sumo();
        }

        return instance;
    }

    private Sumo() {
        connection = new SumoTraciConnection(SUMO_EX_PATH, CONFIG);
        connection.addOption(STEP_OPTION, "0.1");
    }

    public SumoTraciConnection getConnection() {
        return connection;
    }

}
