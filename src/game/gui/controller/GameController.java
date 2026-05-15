package game.gui.controller;

import game.engine.*;
import game.engine.cells.*;
import game.engine.monsters.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;

public class GameController {

    @FXML
    private VBox playerPanel;
    @FXML
    private VBox opponentPanel;
    @FXML
    private StackPane boardContainer;
    @FXML
    private Label statusLabel;

    private Game game;
    private StackPane[][] cellViews = new StackPane[10][10];

    public void initGame(Game game) {
        this.game = game;
        buildBoard();
        buildPlayerPanels();
        statusLabel.setText(game.getCurrent().getName() + "'s turn!");
    }

    private void buildBoard() {
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        Cell[][] cells = game.getBoard().getBoardCells();

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Cell cell = cells[row][col];
                int index = linearIndex(row, col);
                StackPane cellPane = createCellPane(cell, index);
                cellViews[row][col] = cellPane;
                // row 0 of board = bottom of screen, so display inverted
                grid.add(cellPane, col, 9 - row);
            }
        }

        boardContainer.getChildren().add(grid);
    }

    // Convert 2D array position back to linear board index
    private int linearIndex(int row, int col) {
        // odd rows are reversed in the snake pattern
        int actualCol = (row % 2 == 0) ? col : (9 - col);
        return row * 10 + actualCol;
    }

    private StackPane createCellPane(Cell cell, int index) {
        StackPane pane = new StackPane();
        pane.setPrefSize(64, 64);

        String imagePath = getImagePath(cell, index);

        ImageView imgView = new ImageView();
        imgView.setFitWidth(64);
        imgView.setFitHeight(64);
        imgView.setPreserveRatio(false);

        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            if (img.isError()) throw new Exception("Image error");
            imgView.setImage(img);
        } catch (Exception e) {
            // fallback colored rectangle
            Rectangle fallback = new Rectangle(64, 64);
            fallback.setArcWidth(12);
            fallback.setArcHeight(12);
            fallback.setFill(getFallbackColor(cell));
            pane.getChildren().add(fallback);
            Label idxLabel = new Label(String.valueOf(index));
            idxLabel.setStyle("-fx-text-fill: white; -fx-font-size: 8px;");
            StackPane.setAlignment(idxLabel, Pos.TOP_LEFT);
            pane.getChildren().add(idxLabel);
            return pane;
        }

        // Clip to rounded corners
        Rectangle clip = new Rectangle(64, 64);
        clip.setArcWidth(14);
        clip.setArcHeight(14);
        imgView.setClip(clip);

        // Index label top-left
        Label idxLabel = new Label(String.valueOf(index));
        idxLabel.setStyle(
                "-fx-text-fill: white; -fx-font-size: 8px;" +
                        "-fx-background-color: rgba(0,0,0,0.55); -fx-padding: 1 2 1 2;"
        );
        StackPane.setAlignment(idxLabel, Pos.TOP_LEFT);

        pane.getChildren().addAll(imgView, idxLabel);

        // Door energy value bottom-right
        if (cell instanceof DoorCell) {
            DoorCell door = (DoorCell) cell;
            Label energyLabel = new Label(
                    (door.getEnergy() > 0 ? "+" : "") + door.getEnergy()
            );
            energyLabel.setStyle(
                    "-fx-text-fill: #FFD700; -fx-font-size: 8px;" +
                            "-fx-background-color: rgba(0,0,0,0.55); -fx-padding: 1 2 1 2;"
            );
            StackPane.setAlignment(energyLabel, Pos.BOTTOM_RIGHT);
            pane.getChildren().add(energyLabel);
        }

        // Monster cell: show stationed monster name
        if (cell instanceof MonsterCell) {
            MonsterCell mc = (MonsterCell) cell;
            Label monsterLabel = new Label(mc.getCellMonster().getName());
            monsterLabel.setStyle(
                    "-fx-text-fill: #00FF99; -fx-font-size: 7px;" +
                            "-fx-background-color: rgba(0,0,0,0.55); -fx-padding: 1 2 1 2;"
            );
            StackPane.setAlignment(monsterLabel, Pos.BOTTOM_LEFT);
            pane.getChildren().add(monsterLabel);
        }

        return pane;
    }

    private String getImagePath(Cell cell, int index) {
        // Winning cell
        if (index == 99) return "/images/boo's door.png";

        // Check cell type using instanceof on the actual engine object
        if (cell instanceof MonsterCell) {
            MonsterCell mc = (MonsterCell) cell;
            return getMonsterImagePath(mc.getCellMonster());
        }
        if (cell instanceof CardCell) return "/images/cards.png";
        if (cell instanceof ContaminationSock) return "/images/contamination sock.png";
        if (cell instanceof ConveyorBelt) return "/images/gopass.png";
        if (cell instanceof DoorCell) {
            DoorCell door = (DoorCell) cell;
            if (index == 99) return "/images/boo's door.png";
            return door.getRole() == Role.SCARER
                    ? "/images/scarer cell.png"
                    : "/images/laugher cell.png";
        }
        // Normal cell
        return "/images/normal cell.png";
    }

    private String getMonsterImagePath(Monster monster) {
        String type = monster.getClass().getSimpleName().toLowerCase();
        Role role = monster.getRole();

        switch (type) {
            case "dasher":
                return role == Role.SCARER ?
                        "/images/dasher scarer.png" : "/images/dahser laugher.png";
            case "dynamo":
                return role == Role.SCARER ?
                        "/images/dynamo scarer.png" : "/images/dynamo laugher.png";
            case "multitasker":
                return role == Role.SCARER ?
                        "/images/multitasker scarer.png" : "/images/multitastker laugher.png";
            case "schemer":
                return role == Role.SCARER ?
                        "/images/schemer1.png" : "/images/schemer2.png";
            default:
                return "/images/normal cell.png";
        }
    }

    private Color getFallbackColor(Cell cell) {
        if (cell instanceof DoorCell) {
            DoorCell door = (DoorCell) cell;
            return door.getRole() == Role.SCARER
                    ? Color.web("#2E4A7A") : Color.web("#7A4A1E");
        }
        if (cell instanceof CardCell) return Color.web("#4A2E7A");
        if (cell instanceof MonsterCell) return Color.web("#1A5C3A");
        if (cell instanceof ConveyorBelt) return Color.web("#5C4A1A");
        if (cell instanceof ContaminationSock) return Color.web("#5C1A1A");
        return Color.web("#1E3A5C");
    }

    private void buildPlayerPanels() {
        buildPanel(playerPanel, game.getPlayer(), "PLAYER");
        buildPanel(opponentPanel, game.getOpponent(), "OPPONENT");
    }

    private void buildPanel(VBox panel, Monster monster, String title) {
        panel.getChildren().clear();
        panel.setMinWidth(200);
        panel.setMaxWidth(220);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 14px; -fx-font-weight: bold;");

        ImageView avatar = new ImageView();
        avatar.setFitWidth(150);
        avatar.setFitHeight(150);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);

        try {
            Image img = new Image(getClass().getResourceAsStream(getMonsterNobgPath(monster)));
            avatar.setImage(img);
            System.out.println("Avatar loaded: " + !img.isError());
        } catch (Exception e) {
            System.out.println("Avatar error: " + e.getMessage());
        }
        System.out.println("Loading image: " + getMonsterNobgPath(monster));
        System.out.println("Stream: " + getClass().getResourceAsStream(getMonsterNobgPath(monster)));

        Label nameLabel = new Label(monster.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        Label typeLabel = new Label("Type: " + monster.getClass().getSimpleName());
        typeLabel.setStyle("-fx-text-fill: #ccccee; -fx-font-size: 11px;");

        Label roleLabel = new Label("Role: " + monster.getRole());
        roleLabel.setStyle("-fx-text-fill: #ccccee; -fx-font-size: 11px;");

        Label energyLabel = new Label("⚡ Energy: " + monster.getEnergy());
        energyLabel.setStyle("-fx-text-fill: #00FF99; -fx-font-size: 12px; -fx-font-weight: bold;");

        Label posLabel = new Label("📍 Position: " + monster.getPosition());
        posLabel.setStyle("-fx-text-fill: #ccccee; -fx-font-size: 11px;");

        panel.getChildren().addAll(titleLabel, avatar, nameLabel, typeLabel, roleLabel, energyLabel, posLabel);
    }

    private String getMonsterNobgPath(Monster monster) {
        String type = monster.getClass().getSimpleName().toLowerCase();
        Role role = monster.getRole();
        switch (type) {
            case "dasher":
                return role == Role.SCARER ?
                        "/images/dasher scarer_nobg.png" : "/images/dasher laugher_nobg.png";
            case "dynamo":
                return role == Role.SCARER ?
                        "/images/dynamo scarer_nobg.png" : "/images/dynamo laugher_nobg.png";
            case "multitasker":
                return role == Role.SCARER ?
                        "/images/multitasker scarer_nobg.png" : "/images/multitasker laugher_nobg.png";
            case "schemer":
                return role == Role.SCARER ?
                        "/images/schemer1_nobg.png" : "/images/schemer2_nobg.png";
            default:
                return "/images/normal cell.png";
        }
    }
}