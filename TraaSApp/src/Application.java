import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.regex.Pattern;

public class Application {

    private static final Pattern SUMO_BIN_REG = Pattern.compile(".+\\.sumo-gui.exe");
    private static final Pattern CONFIG_REG = Pattern.compile(".+\\.(sumo.cfg | sumocfg)");

    private final JFileChooser binFileChooser = new JFileChooser() {
        {
            setFileSelectionMode(FILES_ONLY);
            setDialogTitle("Select the sumo-gui exec");
            setFileFilter(new FileNameExtensionFilter("*.exe", "exe"));
        }
    };

    private final JFileChooser configFileChooser = new JFileChooser() {
        {
            setFileSelectionMode(FILES_ONLY);
            setDialogTitle("Select the test config file");
            setFileFilter(new FileNameExtensionFilter("sumo configs", "sumocfg", "sumo.cfg"));
        }
    };

    public static void main(String[] args) {
        new Application().run();
    }

    private void run() {
        final String sumoBin = getAbsolutePathWithFileChooser(binFileChooser);
        final String sumoCfg = getAbsolutePathWithFileChooser(configFileChooser);

        if (SUMO_BIN_REG.matcher(sumoBin).matches()
                && CONFIG_REG.matcher(sumoCfg).matches()) {
            try {
                new Sumo(sumoBin, sumoCfg).run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getAbsolutePathWithFileChooser(JFileChooser fileChooser) {
        int res = fileChooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return "";
    }

}
