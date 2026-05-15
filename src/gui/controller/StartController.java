package game.gui.controller;

import game.engine.Game;
import game.engine.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class StartController {

    @FXML
    private Button scarerButton;

    @FXML
    private Button laugherButton;

    @FXML
    private void handleScarer(ActionEvent event) throws Exception {
        loadGame(Role.SCARER);
    }

    @FXML
    private void handleLaugher(ActionEvent event) throws Exception {
        loadGame(Role.LAUGHER);
    }

    private void loadGame(Role role) throws Exception {
        Game game = new Game(role);
        // We'll load the game screen here next
        System.out.println("Starting game as: " + role);
    }
}