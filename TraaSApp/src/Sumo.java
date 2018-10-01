import it.polito.appeal.traci.SumoTraciConnection;

import javax.swing.*;
import java.awt.*;

public class Sumo {

    private JButton startServerButton = new JButton("Start simulation") {

    };

    private JFrame sumoServerFrame = new JFrame("Sumo server") {
        {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(200, 300));
            setLayout(new GridLayout(1, 1));

            add(startServerButton);

            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
    };

    private final String sumoExec;
    private final String sumoConf;

    private final SumoTraciConnection connection;

    public Sumo(String sumoExec, String sumoConf) {
        this.sumoExec = sumoExec;
        this.sumoConf = sumoConf;

        this.connection = new SumoTraciConnection(sumoExec, sumoConf);
    }

    public void run() throws Exception {

    }
}
