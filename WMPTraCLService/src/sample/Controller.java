package sample;

import it.polito.appeal.traci.SumoTraciConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.sumo.Sumo;

import java.io.IOException;

public class Controller {

    private Sumo sumo;

    @FXML
    Button startButton;

    @FXML
    private void initialize() {
        sumo = Sumo.getInstance();
    }

    @FXML
    private void onStart() {
        try {
            final SumoTraciConnection connection = sumo.getConnection();
            connection.runServer();
            connection.do_timestep();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
