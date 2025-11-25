package jpp.tcrush.gui;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jpp.tcrush.gamelogic.Shape;
import jpp.tcrush.gamelogic.utils.Coordinate2D;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class TCrushGui extends Application {
    GridPane grid = new GridPane();
    VBox shapesBox = new VBox(10);

    String currentLevelPath;

    Button btnLevelSpeichern;

    Button btnLevelZuruecksetzen;

    Label totalShapesLabel = new Label();

    Label messageLabel = new Label();

    ScrollPane shapesScrollPane;

    LevelState initialState;

    GridPane selectedShapeGrid = null;
    String resourcesPath = Paths.get(System.getProperty("user.home"), "MyGameResources").toString();

    @Override
    public void start(Stage primaryStage) {

        File resourcesDir = new File(resourcesPath);
        if (!resourcesDir.exists()) {
            resourcesDir.mkdirs();
        }

        Button btnLevelLaden = new Button("Level laden");
        btnLevelSpeichern = new Button("Level speichern");
        btnLevelZuruecksetzen = new Button("Level zur\u00fccksetzen");
        btnLevelSpeichern.setDisable(true);
        btnLevelZuruecksetzen.setDisable(true);

        btnLevelLaden.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(resourcesPath));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                loadLevel(selectedFile.getAbsolutePath());
            }
        });

        btnLevelSpeichern.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(resourcesPath));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                saveLevelToFile(file.getAbsolutePath());
            }
        });

        btnLevelZuruecksetzen.setOnAction(e -> resetLevel());
        btnLevelZuruecksetzen.setDisable(true);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(btnLevelLaden, btnLevelSpeichern, btnLevelZuruecksetzen);
        buttonBox.setAlignment(Pos.CENTER);

        totalShapesLabel = new Label();

        grid.setAlignment(Pos.CENTER);

        HBox mainContent = new HBox(10);
        mainContent.getChildren().addAll(grid, shapesBox);
        mainContent.setAlignment(Pos.BASELINE_CENTER);

        VBox gridAndTotalShapesContainer = new VBox(10);
        gridAndTotalShapesContainer.getChildren().addAll(grid, totalShapesLabel);
        gridAndTotalShapesContainer.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane mainLayout = new BorderPane();

        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setAlignment(Pos.CENTER);
        mainLayout.setCenter(messageLabel);

        mainLayout.setTop(buttonBox);
        mainLayout.setStyle("-fx-background-color: #333333;");

        shapesScrollPane = new ScrollPane(shapesBox);
        shapesScrollPane.setStyle("-fx-background: SILVER;");
        shapesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        shapesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        shapesScrollPane.setVisible(false);
        mainLayout.setBottom(shapesScrollPane);
        buttonBox.setStyle("-fx-background-color: SILVER ");
        shapesBox.setStyle("-fx-background-color: SILVER");

        mainLayout.setCenter(grid);

        HBox totalShapesContainer = new HBox(totalShapesLabel);
        totalShapesContainer.setAlignment(Pos.CENTER);
        mainLayout.setBottom(totalShapesContainer);

        VBox bottomContainer = new VBox(totalShapesContainer, shapesScrollPane);
        mainLayout.setBottom(bottomContainer);


        Scene scene = new Scene(mainLayout, 550, 600);
        primaryStage.setTitle("TCrush");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node getNodeByRowColumnIndex(final int row, final int col, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    private String colorToCode(Color color) {
        if (color.equals(Color.BLUE)) return "b";
        if (color.equals(Color.GREEN)) return "g";
        if (color.equals(Color.RED)) return "r";
        if (color.equals(Color.YELLOW)) return "y";
        if (color.equals(Color.PURPLE)) return "p";
        if (color.equals(Color.ORANGE)) return "o";
        if (color.equals(Color.BLACK)) return "B";
        if (color.equals(Color.TRANSPARENT)) return "n";
        if (color.equals(Color.LIGHTGRAY)) return "+";
        return "#";
    }

    private void saveLevelToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("TCrush-LevelDefinition:\n");
            for (int row = 0; row < grid.getRowCount(); row++) {
                for (int col = 0; col < grid.getColumnCount(); col++) {
                    Node node = getNodeByRowColumnIndex(row, col, grid);
                    if (node instanceof StackPane stackPane) {
                        if (!stackPane.getChildren().isEmpty()) {
                            if (stackPane.getChildren().size() == 1) {
                                writer.write("+");
                            } else {
                                Node child = stackPane.getChildren().get(1);
                                if (child instanceof Circle circle) {
                                    Color color = (Color) circle.getFill();
                                    writer.write(colorToCode(color));
                                }
                            }
                        }
                    } else {
                        writer.write("#");
                    }
                }
                writer.newLine();
            }
            writer.write("\nShapes:\n");
            List<Shape> shapesToSave = initialState.shapes.stream()
                    .filter(shape -> shape.getAmount() > 0).toList();

            for (Shape shape : shapesToSave) {
                writer.write(shape.getAmount() + ":");
                for (Coordinate2D point : shape.getPoints()) {
                    writer.write("(" + point.getX() + "," + (point.getY() * -1) + ");");
                }
                writer.newLine();
            }
            checkIfAllShapesUsed();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    private void resetLevel() {
        if (currentLevelPath != null && !currentLevelPath.isEmpty()) {
            loadLevel(currentLevelPath);
        }
    }

    private void loadLevel(String filePath) {
        currentLevelPath = filePath;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            grid.getChildren().clear();

            boolean isReadingLevel = false;
            boolean isReadingShapes = false;
            List<Shape> shapes = new ArrayList<>();
            int rowIndex = 0;
            int totalShapes = 0;

            for (String line : lines) {
                if (line.startsWith("TCrush-LevelDefinition:")) {
                    isReadingLevel = true;
                    isReadingShapes = false;
                } else if (line.startsWith("Shapes:")) {
                    isReadingShapes = true;
                    isReadingLevel = false;
                } else if (isReadingLevel) {
                    for (int colIndex = 0; colIndex < line.length(); colIndex++) {
                        char c = line.charAt(colIndex);
                        if (c != '#') {
                            StackPane shape = createCircleFromChar(c);
                            grid.add(shape, colIndex, rowIndex);
                        }

                    }
                    rowIndex++;
                } else if (isReadingShapes) {

                    System.out.println("Gelesene Shape-Koordinaten: " + line);
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        int amount = Integer.parseInt(parts[0].trim());
                        String shapeDef = parts[1].trim();
                        System.out.println("Verarbeitete Koordinaten für Shape: " + shapeDef + " mit Menge " + amount);
                        Shape shape = switch (shapeDef) {
                            case "point" -> Shape.getPointShape(amount);
                            case "sRow" -> Shape.getsRowShape(amount);
                            case "sColumn" -> Shape.getsColumnShape(amount);
                            case "column" -> Shape.getColumnShape(amount);
                            case "row" -> Shape.getRowShape(amount);
                            case "downT" -> Shape.getUpTShape(amount);
                            case "upT" -> Shape.getDownTShape(amount);
                            case "rightT" -> Shape.getRightTShape(amount);
                            case "leftT" -> Shape.getLeftTShape(amount);
                            default -> createShapeFromCoordinates(shapeDef, amount);
                        };

                        shapes.add(shape);
                        totalShapes += amount;

                    }
                }
            }
            initialState = new LevelState(new ArrayList<>(lines), new ArrayList<>(shapes));
            displayShapes(shapes);
            shapesScrollPane.setVisible(true);
            btnLevelSpeichern.setDisable(false);
            btnLevelZuruecksetzen.setDisable(false);
            System.out.println(totalShapes + " Shapes insgesamt enthält");
            totalShapesLabel.setText("Total Shapes: " + totalShapes);
            isGameStarted = false;
            applyGravity();
        } catch (IndexOutOfBoundsException | IOException ignored) {

        }
    }

    private Shape createShapeFromCoordinates(String coordinates, int amount) {
        List<Coordinate2D> points = new ArrayList<>();
        String[] coords = coordinates.split(";");


        for (String coord : coords) {
            coord = coord.substring(1, coord.length()-1);
            String[] xy = coord.split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            points.add(new Coordinate2D(x, -y));
        }

        return new Shape(points, amount);
    }


    private void displayShapes(List<Shape> shapes) {

        shapesBox.getChildren().clear();
        HBox shapesContainer = new HBox(65);

        for (Shape shape : shapes) {
            GridPane shapeGrid = new GridPane();
            shapeGrid.setPadding(new Insets(10));
            shapeGrid.setHgap(2);
            shapeGrid.setVgap(2);

            int minX = shape.getPoints().stream().min(Comparator.comparingInt(Coordinate2D::getX)).orElse(new Coordinate2D(0, 0)).getX();
            int minY = shape.getPoints().stream().min(Comparator.comparingInt(Coordinate2D::getY)).orElse(new Coordinate2D(0, 0)).getY();
            shapeGrid.setOnMouseEntered(e -> shapeGrid.setStyle("-fx-background-color: #FF6666;"));
            shapeGrid.setOnMouseExited(e -> {
                if (shapeGrid != selectedShapeGrid) {
                    shapeGrid.setStyle("-fx-background-color: transparent;");
                }
            });

            shapeGrid.setOnMouseClicked(e -> handleShapeGridClick(shape, shapeGrid));
            for (Coordinate2D point : shape.getPoints()) {
                Rectangle square = new Rectangle(35, 35, Color.WHITE);
                square.setStrokeWidth(5);
                shapeGrid.add(square, point.getX() - minX, point.getY() - minY);
            }

            Label label = new Label("x" + shape.getAmount());
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
            label.setAlignment(Pos.CENTER);


            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.getChildren().addAll(shapeGrid, label);

            VBox vbox = new VBox(1, stackPane);
            vbox.setAlignment(Pos.CENTER);
            shapesContainer.getChildren().add(vbox);
        }
        shapesBox.getChildren().add(shapesContainer);
    }

    private Shape selectedShape;

    private void handleShapeGridClick(Shape shape, GridPane shapeGrid) {
        System.out.println("Anzahl: " + shape.getAmount());
        if (selectedShapeGrid != null && selectedShapeGrid != shapeGrid) {
            selectedShapeGrid.setStyle("");
        }
        selectedShape = shape;
        selectedShapeGrid = shapeGrid;
        shapeGrid.setStyle("-fx-background-color: #FF6666;");

        //shapeGrid.setBorder(new Border(new BorderStroke(Color.SILVER, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        shapesBox.getChildren().forEach(child -> {
            if (child instanceof HBox) {
                ((HBox) child).getChildren().forEach(shapeContainer -> {
                    if (shapeContainer instanceof VBox) {
                        ((VBox) shapeContainer).getChildren().forEach(grid -> {
                            if (grid != shapeGrid && grid instanceof GridPane) {
                                ((GridPane) grid).setBorder(Border.EMPTY);
                            }
                        });
                    }
                });
            }
        });
    }

    private void showShapeOverlay(Shape shape, int baseX, int baseY, boolean show) {
        for (Coordinate2D point : shape.getPoints()) {
            int actualX = baseX + point.getX();
            int actualY = baseY + point.getY();


            for (Node node : grid.getChildren()) {
                if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
                    if (GridPane.getRowIndex(node) == actualY && GridPane.getColumnIndex(node) == actualX) {
                        Rectangle rect = (Rectangle) ((StackPane) node).getChildren().get(0);
                        rect.setFill(show ? Color.web("#FF6666") : Color.LIGHTGRAY);
                    }
                }
            }
        }
    }

    private StackPane createCircleFromChar(char c) {
        Rectangle square = new Rectangle();
        square.setFill(Color.LIGHTGRAY);
        square.setStroke(Color.DARKGRAY);

        Circle circle = null;

        square.widthProperty().bind(grid.widthProperty().divide(8));
        square.heightProperty().bind(grid.heightProperty().divide(8));

        switch (c) {
            case 'b' -> {
                circle = new Circle();
                circle.setFill(Color.BLUE);
            }
            case 'g' -> {
                circle = new Circle();
                circle.setFill(Color.GREEN);
            }
            case 'r' -> {
                circle = new Circle();
                circle.setFill(Color.RED);
            }
            case 'y' -> {
                circle = new Circle();
                circle.setFill(Color.YELLOW);
            }
            case 'p' -> {
                circle = new Circle();
                circle.setFill(Color.PURPLE);
            }
            case 'o' -> {
                circle = new Circle();
                circle.setFill(Color.ORANGE);
            }
            case 'B' -> {
                circle = new Circle();
                circle.setFill(Color.BLACK);
            }
            case 'n' -> {
                square.setFill(Color.LIGHTGRAY);
                square.setStroke(Color.DARKGRAY);

            }
            case '+' -> {
                square.setStroke(Color.WHITE);
            }
            case '#' -> {

            }
            default -> {
                circle = new Circle();
                circle.setFill(Color.TRANSPARENT);
            }
        }
        StackPane stack = new StackPane();
        stack.getChildren().add(square);
        if (circle != null) {
            circle.radiusProperty().bind(Bindings.min(square.widthProperty(), square.heightProperty()).divide(2.1));
            stack.getChildren().add(circle);
        }
        stack.setOnMouseEntered(event -> {
            stack.setScaleX(1.05);
            stack.setScaleY(1.05);
            square.setFill(Color.RED);
        });
        stack.setOnMouseExited(event -> {
            stack.setScaleX(1.0);
            stack.setScaleY(1.0);
            square.setFill(Color.LIGHTGRAY);
        });

        stack.setOnMouseEntered(event -> {
            if (selectedShape != null) {
                Integer rowIndex = GridPane.getRowIndex(stack);
                Integer colIndex = GridPane.getColumnIndex(stack);
                if (rowIndex != null && colIndex != null) {
                    showShapeOverlay(selectedShape, colIndex, rowIndex, true);
                }
            }
        });

        stack.setOnMouseExited(event -> {
            if (selectedShape != null) {
                Integer rowIndex = GridPane.getRowIndex(stack);
                Integer colIndex = GridPane.getColumnIndex(stack);
                if (rowIndex != null && colIndex != null) {
                    showShapeOverlay(selectedShape, colIndex, rowIndex, false);
                }
            }
        });

        stack.setOnMouseClicked(event -> {
            if (selectedShape != null) {
                Integer rowIndex = GridPane.getRowIndex(stack);
                Integer colIndex = GridPane.getColumnIndex(stack);
                if (rowIndex != null && colIndex != null) {
                    placeShapeOnBoard(colIndex, rowIndex);
                }
            }
        });
        return stack;
    }

    private boolean doesShapeFit(int baseX, int baseY, Shape shape) {
        Color matchingColor = null;
        for (Coordinate2D point : shape.getPoints()) {
            int x = baseX + point.getX();
            int y = baseY + point.getY();
            Node node = getNodeByRowColumnIndex(y, x, grid);
            if (!(node instanceof StackPane stackPane)) {
                return false;
            }
            if(stackPane.getChildren().size() < 2) {
                return false;
            }
            Circle circle = (Circle) stackPane.getChildren().get(1);
            Color circleColor = (Color) circle.getFill();
            if (circleColor.equals(Color.TRANSPARENT) || (matchingColor != null && !circleColor.equals(matchingColor))) {
                return false;
            }
            if (matchingColor == null) {
                matchingColor = circleColor;
            }
        }
        return true;
    }

    private boolean isGameStarted = false;
    private void applyGravity() {
        boolean moved;
        if (!isGameStarted) {
            return;
        }
        do {
            moved = false;
            for (int col = 0; col < grid.getColumnCount(); col++) {
                for (int row = grid.getRowCount() - 1; row > 0; row--) {
                    Node belowNode = getNodeByRowColumnIndex(row, col, grid);
                    Node aboveNode = getNodeByRowColumnIndex(row - 1, col, grid);

                    if (belowNode instanceof StackPane belowStack && aboveNode instanceof StackPane aboveStack) {
                        Circle belowCircle = null;
                        Circle aboveCircle = null;

                        for (Node node : belowStack.getChildren()) {
                            if (node instanceof Circle) belowCircle = (Circle) node;
                        }
                        for (Node node : aboveStack.getChildren()) {
                            if (node instanceof Circle) aboveCircle = (Circle) node;
                        }

                        boolean isBelowEmptyOrPlus = belowCircle == null || belowCircle.getFill().equals(Color.TRANSPARENT);
                        if (isBelowEmptyOrPlus && aboveCircle != null && !aboveCircle.getFill().equals(Color.TRANSPARENT)) {
                            Color temp = (Color) aboveCircle.getFill();
                            aboveCircle.setFill(Color.TRANSPARENT);
                            if (belowCircle != null) {
                                belowCircle.setFill(temp);
                            } else {
                                Circle newCircle = new Circle();
                                newCircle.setFill(temp);
                                newCircle.radiusProperty().bind(Bindings.min(belowStack.widthProperty(), belowStack.heightProperty()).divide(2.1));
                                belowStack.getChildren().add(newCircle);
                            }
                            moved = true;
                        }
                    }
                }
            }
        } while (moved);
    }


    private void placeShapeOnBoard(int baseX, int baseY) {
        if (selectedShape != null && selectedShape.getAmount() > 0 && doesShapeFit(baseX, baseY, selectedShape)) {
            isGameStarted = true;
            for (Coordinate2D point : selectedShape.getPoints()) {
                int x = baseX + point.getX();
                int y = baseY + point.getY();
                Node node = getNodeByRowColumnIndex(y, x, grid);
                if (node instanceof StackPane stackPane) {
                    Circle circle = (Circle) stackPane.getChildren().get(1);
                    circle.setFill(Color.TRANSPARENT);
                }
            }
            selectedShape.reduceAmount();
            for (Shape shape : initialState.shapes) {
                if (shape.equals(selectedShape)) {
                    shape.setAmount(selectedShape.getAmount());
                    break;
                }
            }

            updateShapesDisplay();
            applyGravity();
            updateTotalShapes();
            checkIfAllShapesUsed();
        }
    }

    private boolean checkForPossibleMoves() {
        for (Shape shape : initialState.shapes) {
            if (shape.getAmount() > 0) {
                for (int row = 0; row < grid.getRowCount(); row++) {
                    for (int col = 0; col < grid.getColumnCount(); col++) {
                        if (doesShapeFit(col, row, shape)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private void updateShapesDisplay() {
        shapesBox.getChildren().clear();

        List<Shape> shapesWithCountGreaterThanZero = initialState.shapes.stream()
                .filter(shape -> shape.getAmount() > 0)
                .collect(Collectors.toList());

        if (shapesWithCountGreaterThanZero.isEmpty()) {
            shapesScrollPane.setVisible(false);
        } else {
            displayShapes(shapesWithCountGreaterThanZero);
        }
    }

    private void updateTotalShapes() {
        int totalShapes = Integer.parseInt(totalShapesLabel.getText().replaceAll("\\D", "")) - 1;
        totalShapesLabel.setText("Total Shapes: " + totalShapes);
    }

    private void checkIfAllShapesUsed() {
        int totalShapes = Integer.parseInt(totalShapesLabel.getText().replaceAll("\\D", ""));
        if (totalShapes == 0) {
            showEndGameOptions();
        }
        else if (!checkForPossibleMoves()) {
            endGame();
        }
    }

    private void showEndGameOptions() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Spiel beendet");
        alert.setHeaderText("\t\tAlle Shapes und Kreisen wurden verwendet!\n\t\t\t\t\tLevel gel\u00f6st!");
        alert.setContentText("W\u00e4hlen Sie eine der folgenden Optionen:");

        ButtonType buttonTypeRestart = new ButtonType("Level zur\u00fccksetzen");
        ButtonType buttonTypeNewLevel = new ButtonType("Neues Level laden");
        ButtonType buttonTypeExit = new ButtonType("Beenden", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeRestart, buttonTypeNewLevel, buttonTypeExit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeRestart) {
            resetLevel();
        } else if (result.isPresent() && result.get() == buttonTypeNewLevel) {
            loadNewLevel();
        } else if (result.isPresent() && result.get() == buttonTypeExit) {
            System.exit(0);
        }
    }

    private void endGame() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Keine Z\u00fcge m\u00f6glich");
            alert.setHeaderText("Es gibt keine m\u00f6glichen Z\u00fcge mehr.");
            alert.setContentText("W\u00e4hlen Sie eine der folgenden Optionen:");

            ButtonType buttonTypeRestart = new ButtonType("Level zur\u00fccksetzen");
            ButtonType buttonTypeNewLevel = new ButtonType("Neues Level laden");
            ButtonType buttonTypeExit = new ButtonType("Beenden");

            alert.getButtonTypes().setAll(buttonTypeRestart, buttonTypeNewLevel, buttonTypeExit);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeRestart) {
                resetLevel();
            } else if (result.isPresent() && result.get() == buttonTypeNewLevel) {
                loadNewLevel();
            } else if (result.isPresent() && result.get() == buttonTypeExit) {
                System.exit(0);
            }
        });
    }
    private void loadNewLevel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(resourcesPath));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            loadLevel(selectedFile.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class LevelState {
    List<String> gridState;
    List<Shape> shapes;

    public LevelState(List<String> gridState, List<Shape> shapes) {
        this.gridState = gridState;
        this.shapes = shapes;
    }
}