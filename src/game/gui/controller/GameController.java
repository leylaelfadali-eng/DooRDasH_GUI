package game.gui.controller;

import game.engine.Game;
import game.engine.Role;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class GameController {

    @FXML private VBox playerPanel;
    @FXML private VBox opponentPanel;
    @FXML private StackPane boardContainer;
    @FXML private Label statusLabel;

    private Game game;

    public void initGame(Game game) {
        this.game = game;
        statusLabel.setText("Game started! Your turn.");
    }
}