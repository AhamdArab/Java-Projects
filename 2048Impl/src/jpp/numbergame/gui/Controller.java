package jpp.numbergame.gui;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import jpp.numbergame.Direction;
import jpp.numbergame.Move;
import jpp.numbergame.NumberGame;

import java.net.URL;
import java.util.*;


public class Controller implements Initializable {

    @FXML
    Label points;

    @FXML
    Label score;

    @FXML
    Label errMsg;

    @FXML
    GridPane grid;

    @FXML
    GridPane highScores;

    @FXML
    AnchorPane mainPain;

    @FXML
    AnchorPane homePane;

    @FXML
    VBox gamePane;

    @FXML
    Button startNewGameButton;

    @FXML
    Button submitButton;


    String currentPlayer = "";

    @FXML
    TextField playerName;
    NumberGame game;

    Map<String, Integer> playersScore = new HashMap<>();
    EventType<KeyEvent> event = KeyEvent.KEY_PRESSED;

    @FXML
    Label message;
    @FXML
    Label playerNameLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void activateEvent() {
        mainPain.addEventFilter(event, this::handleArrowKey);
    }

    public void deactivateEvent() {
        mainPain.removeEventFilter(event, this::handleArrowKey);

    }

    public void initialGame() {
        game = new NumberGame(4, 4, 2);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int value = game.get(x, y);
                Rectangle rectangle = new Rectangle(70, 70);
                Text text = new Text(Integer.toString(value));
                text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                if (value == 2) {
                    rectangle.setFill(Color.valueOf("#D77E7EFF"));
                }
                if (value == 4) {
                    rectangle.setFill(Color.valueOf("#5D3C3CFF"));
                    text.setFill(Color.valueOf("#FFFFFF"));
                }
                if (value == 8) {
                    rectangle.setFill(Color.valueOf("#545050FF"));
                    text.setFill(Color.valueOf("#FFFFFF"));

                }
                if (value == 16) {
                    rectangle.setFill(Color.valueOf("#6CE71FFF"));
                }
                if (value == 32) {
                    rectangle.setFill(Color.valueOf("#0ADFFCFF"));
                }
                if (value == 64) {
                    rectangle.setFill(Color.valueOf("#6DA249FF"));
                }
                if (value == 256) {
                    rectangle.setFill(Color.valueOf("#6AA649FF"));
                }
                if (value == 128) {
                    rectangle.setFill(Color.valueOf("#6DA249FF"));
                }
                if (value == 512){
                    rectangle.setFill(Color.valueOf("#6CE71FFF"));
                }

                if (value == 1024){
                    rectangle.setFill(Color.valueOf("#1B9F9CFF"));
                }
                if (value == 2048){
                    rectangle.setFill(Color.valueOf("#614949FF"));
                }

                // Set font size
                StackPane stackPane = new StackPane(rectangle, text);
                GridPane.setHalignment(text, Pos.CENTER.getHpos()); // Center-align text
                if (value > 0)
                    grid.add(stackPane, x, y);

            }
        }
        System.out.println(game);
    }


    public void changeGrid() {
        grid.getChildren().removeIf(node -> node instanceof StackPane);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int value = game.get(x, y);
                Rectangle rectangle = new Rectangle(70, 70);
                Text text = new Text(Integer.toString(value));
                if (value == 2) {
                    rectangle.setFill(Color.valueOf("#D77E7EFF"));
                }
                if (value == 4) {
                    rectangle.setFill(Color.valueOf("#5D3C3CFF"));
                    text.setFill(Color.valueOf("#FFFFFF"));
                }
                if (value == 8) {
                    rectangle.setFill(Color.valueOf("#545050FF"));
                    text.setFill(Color.valueOf("#FFFFFF"));

                }
                if (value == 16) {
                    rectangle.setFill(Color.valueOf("#6CE71FFF"));
                }
                if (value == 32) {
                    rectangle.setFill(Color.valueOf("#0ADFFCFF"));
                }
                if (value == 64) {
                    rectangle.setFill(Color.valueOf("#6DA249FF"));
                }
                if (value == 128) {
                    rectangle.setFill(Color.valueOf("#6DA249FF"));
                }
                if (value == 256) {
                    rectangle.setFill(Color.valueOf("#6AA649FF"));
                }
                if (value == 512){
                    rectangle.setFill(Color.valueOf("#6CE71FFF"));
                }

                if (value == 1024){
                    rectangle.setFill(Color.valueOf("#1B9F9CFF"));
                }
                if (value == 2048){
                    rectangle.setFill(Color.valueOf("#614949FF"));
                }


                text.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Set font size
                StackPane stackPane = new StackPane(rectangle, text);
                GridPane.setHalignment(text, Pos.CENTER.getHpos()); // Center-align text
                if (value > 0)
                    grid.add(stackPane, x, y);


            }
        }
        points.setText(game.getPoints() + "");
        System.out.println(game);

    }

    @FXML
    public void onReset() {
        // Safely remove the node using the iterator
        grid.getChildren().removeIf(node -> node instanceof StackPane);
        points.setText("0");
        message.setVisible(false);
        initialGame();
    }

    @FXML
    public void submitName() {
        String name = playerName.getText();
        if (name.contains(" ") || name.isBlank()) {
            errMsg.setVisible(true);
        } else {
            errMsg.setVisible(false);
            if (!playersScore.containsKey(name))
                playersScore.put(name, 0);
            submitButton.setDisable(true);
            startNewGameButton.setDisable(false);
            playerName.setDisable(true);
        }
    }

    @FXML
    public void startNewGame() {
        try {
            currentPlayer = playerName.getText();
            playerNameLabel.setText("Name: " + currentPlayer);
            homePane.setOpacity(0);
            gamePane.setOpacity(1);
            grid.getChildren().removeIf(node -> node instanceof StackPane);
            points.setText("0");
            initialGame();
            activateEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleArrowKey(KeyEvent event) {
        List<Move> result = new ArrayList<>();
        switch (event.getCode()) {
            case UP:
                result = game.move(Direction.UP);
                break;
            case DOWN:
                result = game.move(Direction.DOWN);
                break;
            case LEFT:
                result = game.move(Direction.LEFT);
                break;
            case RIGHT:
                result = game.move(Direction.RIGHT);
                break;
            default:
                // Ignore other keys
        }

        if (!result.isEmpty()) {
            game.addRandomTile();
            changeGrid();
            if (!game.canMove()) {
                gamePane.setOpacity(0.2);
                homePane.setOpacity(1);
                score.setText("Score: " + points.getText());
                int currentPoints = game.getPoints();
                if (playersScore.get(currentPlayer) < currentPoints) {
                    playersScore.put(currentPlayer, currentPoints);
                }
                deactivateEvent();
                message.setText("Out Of Moves, GameOver!");
                highScores.getChildren().removeIf(node -> node instanceof Text);

                List<PlayerScore> playerScores = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : playersScore.entrySet()) {
                    playerScores.add(new PlayerScore(entry.getKey(), entry.getValue()));
                }
                playerScores.sort((o1, o2) -> Integer.compare(o2.points, o1.points));

                for (int i = 0; i < Math.min(5, playerScores.size()); i++) {
                    Text name = new Text(playerScores.get(i).name);
                    name.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Set font size
                    GridPane.setHalignment(name, Pos.CENTER.getHpos()); // Center-align text

                    Text score = new Text(Integer.toString(playerScores.get(i).points));
                    score.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Set font size
                    GridPane.setHalignment(score, Pos.CENTER.getHpos()); // Center-align text
                    highScores.add(name, 0, i);
                    highScores.add(score, 1, i);
                }
                startNewGameButton.setDisable(true);
                submitButton.setDisable(false);
                playerName.setDisable(false);
                playerName.setText("");
                score.setVisible(true);

            }
        }
    }
}
