package game.engine;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestFX extends Application {

    @Override
    public void start(Stage stage) {

        // Title label
        Label title = new Label("DooR DasH");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");

        // Subtitle
        Label subtitle = new Label("Scare vs. Laugh Touchdown");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        // Test button
        Button btn = new Button("Click to Test JavaFX ✅");
        btn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20 10 20;");
        btn.setOnAction(e -> btn.setText("JavaFX is working! 🎮"));

        // Layout
        VBox root = new VBox(16, title, subtitle, btn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f0f4ff;");

        // Scene and stage
        Scene scene = new Scene(root, 400, 250);
        stage.setTitle("DooR DasH — JavaFX Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}