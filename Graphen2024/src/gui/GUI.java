package gui;


import de.jpp.factory.GraphFactory;
import gui.controlView.AlgorithmControlView;
import gui.controlView.GraphControlView;
import gui.graphViews.GraphViews;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import de.jpp.model.TwoDimGraph;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GUI extends Application {

    private TwoDimGraph graph = new GraphFactory().createNewTwoDimGraph();

    public static void main(String[] args) {
        launch(args);
    }

    private GraphViews graphView;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("JPP Graph Viewer 2024");
        primaryStage.setResizable(false);

        BorderPane mainPanel = new BorderPane();

        mainPanel.setCenter(getGraphView());
        mainPanel.setBottom(getSouthernBox());
        mainPanel.setLeft(getAlgorithmControl());

        primaryStage.setScene(new Scene(mainPanel, GraphicConfigs.FRAME_SIZE[0], GraphicConfigs.FRAME_SIZE[1]));
        primaryStage.show();

        //  new Thread(() -> graphView.loadGraphIntoView(graph, GraphFactory.getMaze(9, 9, 1, 1))).start();
    }

    private Pane getGraphView() {
        graphView = new GraphViews(graph, this::handleErrorMsg);
        return graphView.getNode();
    }

    private Pane getAlgorithmControl() {
        AlgorithmControlView acv = new AlgorithmControlView(graph, this::handleErrorMsg, graphView);
        Pane pane = acv.getNode();
        pane.setPrefSize(GraphicConfigs.FRAME_SIZE[0] - GraphicConfigs.GRAPH_PANE_SIZE[0], GraphicConfigs.GRAPH_PANE_SIZE[1]);
        return pane;
    }

    private Pane getSouthernBox() {
        HBox box = new HBox();
        box.setPrefSize(GraphicConfigs.FRAME_SIZE[0], GraphicConfigs.FRAME_SIZE[1] - GraphicConfigs.GRAPH_PANE_SIZE[1]);

        box.getChildren().add(getErrorField());

        GraphControlView gv = new GraphControlView(graph, graphView, this::handleErrorMsg);
        box.getChildren().add(gv.getNode());
        return box;
    }


    private TextArea errorField = null;

    private TextArea getErrorField() {
        if (errorField == null) {
            errorField = new TextArea();
            errorField.setPrefSize(GraphicConfigs.FRAME_SIZE[0] - GraphicConfigs.GRAPH_PANE_SIZE[0], GraphicConfigs.FRAME_SIZE[1] - GraphicConfigs.GRAPH_PANE_SIZE[1]);
            errorField.setMaxSize(GraphicConfigs.FRAME_SIZE[0] - GraphicConfigs.GRAPH_PANE_SIZE[0], GraphicConfigs.FRAME_SIZE[1] - GraphicConfigs.GRAPH_PANE_SIZE[1]);
            errorField.setPrefRowCount(6);
            errorField.setEditable(false);
            errorField.setStyle("-fx-text-fill: #f00;");
        }
        return errorField;
    }

    private void handleErrorMsg(String msg) {
        String time = DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now());
        getErrorField().setScrollTop(Double.MAX_VALUE);
        getErrorField().appendText(time + ": " + msg + "\n");
        System.out.println("error: " + msg);
    }

}