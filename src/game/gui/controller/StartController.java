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
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();
        controller.initGame(game);
        Stage stage = (Stage) scarerButton.getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }

    @FXML
    private void handleInstructions(ActionEvent event) {
        showFancyInstructions();
    }

    private void showFancyInstructions() {
        Stage instrStage = new Stage();
        instrStage.setTitle("Game Instructions - DoorDash");

        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #1E3248;");

        // TITLE
        VBox titleBox = new VBox(8);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 20, 15, 20));
        titleBox.setStyle("-fx-background-color: #1E3248;");

        Label titleLabel = new Label("DOORDASH - GAME GUIDE");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        titleLabel.setStyle("-fx-text-fill: #FFD700;");

        Label subtitleLabel = new Label("Scare vs Laugh Touchdown - The Floor Awaits!");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #CCCCEE;");

        Separator titleSep = new Separator();
        titleSep.setStyle("-fx-background-color: #FFD700;");
        titleBox.getChildren().addAll(titleLabel, subtitleLabel, titleSep);

        // SCROLL AREA
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: #1E3248; -fx-background-color: #1E3248;");
        scrollPane.setFitToWidth(true);

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #1E3248;");

        // WINNING CONDITIONS
        VBox winBox = section("rgba(255,215,0,0.08)", "#FFD700");
        Label winTitle = sectionTitle("WINNING CONDITIONS", "#FFD700");
        Label winContent = new Label(
                "BOTH conditions must be met to win:\n" +
                        "  1) Land EXACTLY on cell 99 (Boo's Door)\n" +
                        "  2) Have at least 1000 energy in your canister\n\n" +
                        "If you pass cell 99 without landing exactly, you wrap around!"
        );
        winContent.setWrapText(true);
        winContent.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 13px;");
        winBox.getChildren().addAll(winTitle, winContent);

        // TURN SEQUENCE
        VBox gameplayBox = section("rgba(46,74,122,0.3)", "#2E4A7A");
        Label gameplayTitle = sectionTitle("TURN SEQUENCE", "#FFD700");
        Label gameplayContent = sectionText(
                "1) POWER-UP PHASE (Optional):\n" +
                        "   - Pay 500 energy to activate your monster's special ability\n" +
                        "   - FREE if you landed on a Monster Cell with same role!\n\n" +
                        "2) DICE ROLL: Roll a standard 6-sided dice (1-6)\n\n" +
                        "3) MOVEMENT:\n" +
                        "   - Move forward by dice roll amount\n" +
                        "   - If destination occupied by opponent: STAY and RETRY!\n\n" +
                        "4) CELL EFFECT: Activate the cell's special effect\n\n" +
                        "5) CHECK WIN CONDITION: If not met, switch turns"
        );
        gameplayBox.getChildren().addAll(gameplayTitle, gameplayContent);

        // DOOR MECHANICS
        VBox doorBox = section("rgba(122,74,30,0.3)", "#7A4A1E");
        Label doorTitle = sectionTitle("DOOR MECHANICS", "#FFD700");
        Label doorContent = sectionText(
                "- 50 doors alternate: SCARER doors (odd cells 1,3,5...99) and LAUGHER doors\n" +
                        "- AFFECTS ENTIRE TEAM! When you land on a door, ALL monsters of your role gain/lose energy\n" +
                        "- ONE-TIME USE: Door becomes EXHAUSTED after first landing\n" +
                        "- Role MATCH: Your team GAINS the door's energy\n" +
                        "- Role MISMATCH: Your team LOSES the door's energy\n" +
                        "- SHIELD PROTECTION: If shield blocks energy loss, the door is NOT exhausted!"
        );
        doorBox.getChildren().addAll(doorTitle, doorContent);

        // SPECIAL CELLS
        VBox cellBox = section("rgba(46,74,122,0.3)", "#2E4A7A");
        Label cellTitle = sectionTitle("SPECIAL CELLS", "#FFD700");
        GridPane cellGrid = new GridPane();
        cellGrid.setHgap(15);
        cellGrid.setVgap(8);
        cellGrid.setPadding(new Insets(5, 0, 0, 0));
        String[][] cellData = {
                {"DOORS",         "Team-wide energy gain/loss",                                        "#FFD700"},
                {"MONSTER",       "Same role: FREE power-up | Opposite: SWAP energy if you have more", "#FFD700"},
                {"CARD",          "Draw random card (25 total, reshuffle when empty)",                  "#FFD700"},
                {"CONVEYOR",      "Move FORWARD (like a ladder)",                                       "#FFD700"},
                {"CONTAMINATION", "Move BACKWARD + LOSE 100 energy!",                                   "#FFD700"},
                {"NORMAL",        "Safe passage - no effect",                                           "#CCCCEE"}
        };
        for (int i = 0; i < cellData.length; i++) {
            Label t = new Label(cellData[i][0]);
            t.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            t.setStyle("-fx-text-fill: " + cellData[i][2] + ";");
            t.setMinWidth(120);
            Label d = new Label(cellData[i][1]);
            d.setStyle("-fx-text-fill: #CCCCEE; -fx-font-size: 12px;");
            d.setWrapText(true);
            cellGrid.add(t, 0, i);
            cellGrid.add(d, 1, i);
        }
        cellBox.getChildren().addAll(cellTitle, cellGrid);

        // MONSTER TYPES
        VBox monsterBox = section("rgba(122,74,30,0.3)", "#7A4A1E");
        Label monsterTitle = sectionTitle("MONSTER TYPES & POWER-UPS", "#FFD700");
        GridPane monsterGrid = new GridPane();
        monsterGrid.setHgap(15);
        monsterGrid.setVgap(8);
        monsterGrid.setPadding(new Insets(5, 0, 0, 0));
        String[][] monsters = {
                {"DASHER",      "2x speed | Powerup: 3x speed for 3 turns",                       "#FFD700"},
                {"DYNAMO",      "ALL gains/losses DOUBLED | Powerup: Freeze opponent 1 turn",      "#FFD700"},
                {"MULTITASKER", "1/2 speed but +200 energy bonus | Powerup: Normal speed 2 turns", "#FFD700"},
                {"SCHEMER",     "+10 bonus to all changes | Powerup: Steal 10 from ALL monsters",  "#FFD700"}
        };
        for (int i = 0; i < monsters.length; i++) {
            Label t = new Label(monsters[i][0]);
            t.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            t.setStyle("-fx-text-fill: " + monsters[i][2] + ";");
            t.setMinWidth(110);
            Label d = new Label(monsters[i][1]);
            d.setStyle("-fx-text-fill: #CCCCEE; -fx-font-size: 12px;");
            d.setWrapText(true);
            monsterGrid.add(t, 0, i);
            monsterGrid.add(d, 1, i);
        }
        monsterBox.getChildren().addAll(monsterTitle, monsterGrid);

        // MONSTER ROSTER
        VBox rosterBox = section("rgba(46,74,122,0.3)", "#2E4A7A");
        Label rosterTitle = sectionTitle("MONSTER ROSTER & STARTING ENERGY", "#FFD700");
        GridPane rosterGrid = new GridPane();
        rosterGrid.setHgap(20);
        rosterGrid.setVgap(6);
        rosterGrid.setPadding(new Insets(5, 0, 0, 0));
        String[] headers = {"Monster", "Type", "Role", "Energy"};
        for (int h = 0; h < headers.length; h++) {
            Label hLabel = new Label(headers[h]);
            hLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            hLabel.setStyle("-fx-text-fill: #FFD700;");
            rosterGrid.add(hLabel, h, 0);
        }
        String[][] roster = {
                {"Sulley",     "Dynamo",      "SCARER",  "300"},
                {"Mike",       "Dasher",      "LAUGHER", "100"},
                {"Randall",    "Schemer",     "SCARER",  "20"},
                {"Celia",      "MultiTasker", "LAUGHER", "50"},
                {"Roz",        "MultiTasker", "SCARER",  "100"},
                {"Fungus",     "Dasher",      "LAUGHER", "50"},
                {"Waternoose", "Schemer",     "SCARER",  "70"},
                {"Yeti",       "Dynamo",      "LAUGHER", "100"}
        };
        for (int i = 0; i < roster.length; i++) {
            Label name = new Label(roster[i][0]);
            name.setStyle("-fx-text-fill: #CCCCEE;");
            Label type = new Label(roster[i][1]);
            type.setStyle("-fx-text-fill: #CCCCEE;");
            Label role = new Label(roster[i][2]);
            role.setStyle("-fx-text-fill: " + (roster[i][2].equals("SCARER") ? "#2E4A7A" : "#7A4A1E") + "; -fx-font-weight: bold;");
            Label energy = new Label(roster[i][3]);
            energy.setStyle("-fx-text-fill: #FFD700;");
            rosterGrid.add(name,   0, i + 1);
            rosterGrid.add(type,   1, i + 1);
            rosterGrid.add(role,   2, i + 1);
            rosterGrid.add(energy, 3, i + 1);
        }
        rosterBox.getChildren().addAll(rosterTitle, rosterGrid);

        // CARDS
        VBox cardBox = section("rgba(122,74,30,0.3)", "#7A4A1E");
        Label cardTitle = sectionTitle("CARDS (25 Total - Shuffled)", "#FFD700");
        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(15);
        cardGrid.setVgap(6);
        cardGrid.setPadding(new Insets(5, 0, 0, 0));
        String[][] cards = {
                {"Swapper",           "x4", "Swap position with opponent (if behind)"},
                {"Start Over (Self)", "x2", "You return to cell 0"},
                {"Start Over (Opp.)", "x3", "Opponent returns to cell 0"},
                {"Small Snatcher",    "x3", "Steal 50 energy from opponent"},
                {"Sneaky Thief",      "x2", "Steal 100 energy from opponent"},
                {"Mega Drain",        "x1", "Steal 150 energy from opponent"},
                {"Super Shield",      "x5", "Block next negative effect"},
                {"Mind Scramble",     "x3", "Both confused for 2 turns"},
                {"Total Confusion",   "x2", "Both confused for 3 turns"}
        };
        for (int i = 0; i < cards.length; i++) {
            Label n = new Label(cards[i][0]);
            n.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            n.setStyle("-fx-text-fill: #FFD700;");
            n.setMinWidth(140);
            Label c = new Label(cards[i][1]);
            c.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 12px;");
            c.setMinWidth(30);
            Label ef = new Label(cards[i][2]);
            ef.setStyle("-fx-text-fill: #CCCCEE; -fx-font-size: 12px;");
            ef.setWrapText(true);
            cardGrid.add(n,  0, i);
            cardGrid.add(c,  1, i);
            cardGrid.add(ef, 2, i);
        }
        cardBox.getChildren().addAll(cardTitle, cardGrid);

        // STATUS EFFECTS
        VBox statusBox = section("rgba(46,74,122,0.3)", "#2E4A7A");
        Label statusTitle = sectionTitle("STATUS EFFECTS", "#FFD700");
        GridPane statusGrid = new GridPane();
        statusGrid.setHgap(15);
        statusGrid.setVgap(8);
        statusGrid.setPadding(new Insets(5, 0, 0, 0));
        String[][] statuses = {
                {"SHIELD",    "Blocks next energy loss for the entire team",           "#FFD700"},
                {"FREEZE",    "Skip your next turn entirely",                          "#CCCCEE"},
                {"CONFUSION", "Roles swapped for duration (affects door interactions)","#FFD700"}
        };
        for (int i = 0; i < statuses.length; i++) {
            Label t = new Label(statuses[i][0]);
            t.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            t.setStyle("-fx-text-fill: " + statuses[i][2] + ";");
            t.setMinWidth(100);
            Label d = new Label(statuses[i][1]);
            d.setStyle("-fx-text-fill: #CCCCEE; -fx-font-size: 12px;");
            statusGrid.add(t, 0, i);
            statusGrid.add(d, 1, i);
        }
        statusBox.getChildren().addAll(statusTitle, statusGrid);

        // STRATEGY TIPS
        VBox tipsBox = section("rgba(122,74,30,0.3)", "#7A4A1E");
        Label tipsTitle = sectionTitle("STRATEGY TIPS", "#FFD700");
        Label tipsContent = sectionText(
                "- Save your 500-energy power-up for critical moments!\n" +
                        "- Land on Monster Cells of your role for FREE power-ups\n" +
                        "- Doors affect your ENTIRE team - coordinate strategies!\n" +
                        "- Contamination Socks hurt (100 energy loss) - avoid if possible!\n" +
                        "- Card Cells can turn the game around instantly\n" +
                        "- Dynamo doubles EVERYTHING - both gains AND losses!\n" +
                        "- Schemer's power-up steals from ALL monsters (even teammates!)"
        );
        tipsBox.getChildren().addAll(tipsTitle, tipsContent);

        contentBox.getChildren().addAll(
                winBox,      createDivider(),
                gameplayBox, createDivider(),
                doorBox,     createDivider(),
                cellBox,     createDivider(),
                monsterBox,  createDivider(),
                rosterBox,   createDivider(),
                cardBox,     createDivider(),
                statusBox,   createDivider(),
                tipsBox
        );

        scrollPane.setContent(contentBox);

        // CLOSE BUTTON
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        buttonBox.setStyle("-fx-background-color: #1E3248;");

        Button closeBtn = new Button("GOT IT! CHOOSE YOUR SIDE");
        closeBtn.setStyle(
                "-fx-background-color: #FFD700;" +
                        "-fx-text-fill: #1E3248;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 35;" +
                        "-fx-background-radius: 30;" +
                        "-fx-cursor: hand;"
        );
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(
                "-fx-background-color: #2E4A7A;" +
                        "-fx-text-fill: #FFD700;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 35;" +
                        "-fx-background-radius: 30;" +
                        "-fx-cursor: hand;"
        ));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(
                "-fx-background-color: #FFD700;" +
                        "-fx-text-fill: #1E3248;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 35;" +
                        "-fx-background-radius: 30;" +
                        "-fx-cursor: hand;"
        ));
        closeBtn.setOnAction(e -> instrStage.close());
        buttonBox.getChildren().add(closeBtn);

        mainPane.setTop(titleBox);
        mainPane.setCenter(scrollPane);
        mainPane.setBottom(buttonBox);

        mainPane.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(400), mainPane);
        ft.setToValue(1);
        ft.play();

        Scene scene = new Scene(mainPane, 750, 750);
        instrStage.setScene(scene);
        instrStage.show();
    }

    private VBox section(String bgColor, String borderColor) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(12));
        box.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1.5;"
        );
        return box;
    }

    private Label sectionTitle(String text, String color) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        label.setStyle("-fx-text-fill: " + color + ";");
        return label;
    }

    private Label sectionText(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: #CCCCEE; -fx-font-size: 13px;");
        return label;
    }

    private Separator createDivider() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #FFD700;");
        separator.setMaxWidth(500);
        return separator;
    }
}