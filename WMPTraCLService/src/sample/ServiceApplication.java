package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ServiceApplication extends Application {

    private Scene scene;

    @Override
    public void init() throws Exception {
        super.init();

        final Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        scene = new Scene(root, 400, 500);

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sumo Service");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
