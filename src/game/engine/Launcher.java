package game.engine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/StartView.fxml"));
        Scene scene = new Scene(root, 1280, 720);

        Scale scale = new Scale();
        scale.xProperty().bind(scene.widthProperty().divide(1280));
        scale.yProperty().bind(scene.heightProperty().divide(720));
        root.getTransforms().add(scale);

        stage.setTitle("DooR DasH");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}