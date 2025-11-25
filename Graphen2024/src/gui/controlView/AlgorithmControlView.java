package gui.controlView;

import de.jpp.algorithm.StartToDestStrategy;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.factory.SearchStopFactory;
import gui.GraphicConfigs;
import gui.GuiUtils;
import gui.control.Algorithm;
import gui.control.GraphController;
import gui.graphViews.GraphViews;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.util.List;
import java.util.function.Consumer;

public class AlgorithmControlView {


    private TwoDimGraph graph;

    private Consumer<String> handleErrorMsg;
    private GraphViews graphView;

    private VBox main;

    private ComboBox<Algorithm> algorithmChooser;
    private ToggleGroup strategyGroup;

    public AlgorithmControlView(TwoDimGraph graph, Consumer<String> handleErrorMsg, GraphViews graphView) {
        this.graphView = graphView;
        this.graph = graph;
        this.handleErrorMsg = handleErrorMsg;
    }


    public Pane getNode() {
        if (main == null) {
            main = new VBox();
            main.setSpacing(GuiUtils.SPACING_BIG);
            main.setAlignment(Pos.CENTER);
            main.getChildren().add(getAlgorithmControl());
        }
        return main;
    }

    private Pane getAlgorithmControl() {
        GridControl grid = new GridControl(graph, handleErrorMsg, 250);

        ComboBox<XYNode> startChooser = grid.addNodeBox(0, "Start Node:");

        List<Algorithm> algorithms = FXCollections.observableArrayList(Algorithm.DIJKSTRA, Algorithm.ASTAR, Algorithm.BREADTH, Algorithm.DEPTH);
        algorithmChooser = grid.addBox(1, "Algorithm:", algorithms);

        fillRadioButtons(grid);

        Slider speedSlider = grid.addSlider(5, "Speed:", 1, 1500, GraphicConfigs.animationWaitTime);
        speedSlider.valueProperty().addListener((obsVal, oldVal, newVal) -> GraphicConfigs.animationWaitTime = newVal.intValue());

        Button startSearch = new Button("Start Search");
        Button stopSearch = new Button("Stop Search");

        grid.addLine(6, startSearch, stopSearch);

        startSearch.setOnAction(e -> GraphController.startSearch(graph, graphView, handleErrorMsg, algorithmChooser.getValue(), startChooser.getValue(), readStrategy(), graphView));
        stopSearch.setOnAction(e -> GraphicConfigs.setSearch(null, graphView));

        return grid.getNode();

    }

    private SearchStopStrategy<XYNode> readStrategy() {
        try {
            return (SearchStopStrategy<XYNode>) strategyGroup.getSelectedToggle().getUserData();
        } catch (Exception e) {
            return null;
        }
    }


    private void fillRadioButtons(GridControl grid) {

        //RADIO BUTTONS
        RadioButton expandRB = new RadioButton("Expand All");
        grid.addLine(2, expandRB);

        RadioButton startDestRB = new RadioButton("Dest Node:");
        ComboBox<XYNode> destChooser = grid.addNodeBox(3, startDestRB);

        RadioButton maxStepRB = new RadioButton("Max Steps:");
        Spinner<Integer> stepSpinner = grid.addSpinner(4, maxStepRB, 1, 1000, 10);

        //USERDATA
        expandRB.setUserData(new SearchStopFactory().expandAllNodes());
        maxStepRB.setUserData(new SearchStopFactory().maxNodeCount(10));

        //STRATEGY GROUP
        strategyGroup = new ToggleGroup();
        expandRB.setToggleGroup(strategyGroup);
        startDestRB.setToggleGroup(strategyGroup);
        maxStepRB.setToggleGroup(strategyGroup);

        //CTRL:
        destChooser.getSelectionModel().selectedItemProperty().addListener((obsVal, oldVal, newVal) -> {
            startDestRB.setUserData(newVal != null ? new SearchStopFactory().startToDest(newVal) : null);
            strategyGroup.selectToggle(startDestRB);
        });

        stepSpinner.valueProperty().addListener((obsVal, oldVal, newVal) -> {
                    stepSpinner.setUserData(newVal != null ? new SearchStopFactory().maxNodeCount(newVal) : null);
                    strategyGroup.selectToggle(maxStepRB);
                }
        );

        strategyGroup.selectedToggleProperty().addListener(
                (obsVal, oldVal, newVal) -> {
                    if (startDestRB.equals(newVal)) {
                        stepSpinner.setDisable(true);
                        destChooser.setDisable(false);
                    } else if (maxStepRB.equals(newVal)) {
                        stepSpinner.setDisable(false);
                        destChooser.setDisable(true);
                    } else {
                        stepSpinner.setDisable(true);
                        destChooser.setDisable(true);
                    }
                }
        );

        algorithmChooser.getSelectionModel().selectedItemProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == Algorithm.ASTAR) {
                strategyGroup.selectToggle(startDestRB);
                expandRB.setDisable(true);
                maxStepRB.setDisable(true);
            } else {
                expandRB.setDisable(false);
                maxStepRB.setDisable(false);
            }
        });


    }


}
