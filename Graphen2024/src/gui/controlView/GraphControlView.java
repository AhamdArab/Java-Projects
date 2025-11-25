package gui.controlView;

import gui.GraphicConfigs;
import gui.GuiUtils;
import gui.control.GraphController;
import gui.graphViews.GraphViewMode;
import gui.graphViews.GraphViews;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;

public class GraphControlView {


    private TwoDimGraph graph;

    private Consumer<String> handleErrorMsg;

    private GraphViews graphView;

    private HBox main;

    public GraphControlView(TwoDimGraph graph, GraphViews graphView, Consumer<String> handleErrorMsg) {
        this.graph = graph;
        this.handleErrorMsg = handleErrorMsg;
        this.graphView = graphView;
    }


    public Pane getNode() {
        if (main == null) {
            main = new HBox();
            main.setAlignment(Pos.CENTER);
            main.setPrefSize(GraphicConfigs.GRAPH_PANE_SIZE[0], GraphicConfigs.FRAME_SIZE[1] - GraphicConfigs.GRAPH_PANE_SIZE[1]);
            main.getChildren().add(getContentPane());
        }

        return main;
    }

    public Pane getContentPane() {

        HBox contentPane = new HBox();

        contentPane.setAlignment(Pos.CENTER);
        contentPane.setSpacing(GuiUtils.SPACING_BIG);
        contentPane.getChildren().addAll(
                getLoadStorePane(200),
                getAddNodePane(200),
                getAddEdgePane(200),
                getRemovePane(200)
        );

        return contentPane;
    }


    private FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file for Graph");
        return fileChooser;
    }

    private Pane getLoadStorePane(int width) {
        GridControl grid = new GridControl(graph, handleErrorMsg, width);

        //LOAD

        Button loadGxl = grid.addButton(0, "Load Graph");
        loadGxl.setOnAction(
                e -> {
                    File file = getFileChooser().showOpenDialog(main.getScene().getWindow());
                    GraphController.loadGraph(graph, graphView, handleErrorMsg, file);
                });


        //STORE
        Button storeGraph = grid.addButton(1, "Store Graph");
        storeGraph.setOnAction(
                e -> {
                    File file = getFileChooser().showOpenDialog(main.getScene().getWindow());
                    GraphController.storeGraph(graph, handleErrorMsg, file);
                });


        //MODUS
        ToggleGroup modeGroup = new ToggleGroup();

        RadioButton pixelRB = new RadioButton("Pixel View");
        pixelRB.setToggleGroup(modeGroup);

        RadioButton circleRB = new RadioButton("Circle View");
        circleRB.setToggleGroup(modeGroup);

        RadioButton rawPixelRB = new RadioButton("Pixel View without Edges");
        rawPixelRB.setToggleGroup(modeGroup);

        Function<Toggle, GraphViewMode> buttonToMode = choice -> circleRB.equals(choice) ? GraphViewMode.CIRCLE : choice.equals(rawPixelRB) ? GraphViewMode.RAW_PIXLE : GraphViewMode.PIXLE;
        Function<GraphViewMode, Toggle> modeToButton = mode -> mode == GraphViewMode.CIRCLE ? circleRB : mode == GraphViewMode.RAW_PIXLE ? rawPixelRB : pixelRB;

        modeGroup.selectedToggleProperty().addListener(
                (obsVal, oldVal, newVal) -> graphView.getMode().setValue(buttonToMode.apply(newVal)));

        graphView.getMode().addListener(
                (obsVal, oldVal, newVal) -> modeGroup.selectToggle(modeToButton.apply(newVal)));

        modeGroup.selectToggle(circleRB);

        grid.addLine(2, pixelRB, circleRB);
        grid.addLine(3, rawPixelRB);


        return grid.getNode();

    }



    private Pane getAddEdgePane(int width) {

        GridControl grid = new GridControl(graph, handleErrorMsg, width);

        ComboBox<XYNode> startChooser = grid.addNodeBox(0, "Start Node:");
        ComboBox<XYNode> destChooser = grid.addNodeBox(1, "Destination:");
        TextField distanceInput = grid.addTextField(2, "Distance:");

        Button button = grid.addButton(3, "Add Edge to Graph");
        button.setOnAction(e -> GraphController.addEdge(graph, handleErrorMsg, startChooser.getValue(), destChooser.getValue(), distanceInput.getText()));

        grid.getNode().addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER)
                button.fire();
        });

        return grid.getNode();
    }

    public Pane getAddNodePane(int width) {
        GridControl grid = new GridControl(graph, handleErrorMsg, width);

        TextField labelInput = grid.addTextField(0, "Label:");
        TextField xInput = grid.addTextField(1, "X:");
        TextField yInput = grid.addTextField(2, "Y:");


        Button button = grid.addButton(3, "Add Node to Graph");
        button.setOnAction(e -> GraphController.addNode(graph, graphView, handleErrorMsg, labelInput.getText(), xInput.getText(), yInput.getText()));

        grid.getNode().addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER)
                button.fire();
        });

        return grid.getNode();

    }


    private Pane getRemovePane(int width) {
        GridControl grid = new GridControl(graph, handleErrorMsg, width);

        ComboBox<XYNode> nodeChooser = grid.addNodeBox(0, "Node:");
        Button button = grid.addButton(1, "Remove Node");
        button.setOnAction(e -> graph.removeNode(nodeChooser.getValue()));

        ComboBox<Edge<XYNode, Double>> edgeChooser = grid.addEdgeBox(2, "Edge:");
        button = grid.addButton(3, "Remove Edge");
        button.setOnAction(e -> graph.removeEdge(edgeChooser.getValue()));

        return grid.getNode();
    }


}
