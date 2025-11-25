package gui.controlView;

import gui.GuiUtils;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;

import java.util.List;
import java.util.function.Consumer;

public class GridControl {


    private GridPane pane;

    private TwoDimGraph graph;
    private Consumer<String> handleErrorMsg;

    private int width, widthSmall, widthBig;
    private final int ELEMENT_HEIGHT = 25;

    public GridControl(TwoDimGraph graph, Consumer<String> handleErrorMsg, int width) {
        this.graph = graph;
        this.handleErrorMsg = handleErrorMsg;

        this.width = width;
        widthSmall = width / 3;
        widthBig = width * 2 / 3;

        pane = new GridPane();
        pane.setMaxWidth(width);
        pane.setVgap(GuiUtils.SPACING_SMALL);
    }


    public Spinner<Integer> addSpinner(int row, Control left, int minVal, int maxVal, int initVal) {

        Spinner spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(minVal, maxVal, initVal));

        return add(row, left, spinner);
    }

    public Slider addSlider(int row, String text, int minVal, int maxVal, int initVal) {
        return addSlider(row, getLabel(text), minVal, maxVal, initVal);
    }

    public Slider addSlider(int row, Control left, int minVal, int maxVal, int initVal) {
        Slider slider = new Slider();
        slider.setMin(minVal);
        slider.setMax(maxVal);

        add(row, left, slider);
        slider.setValue(initVal);
        return slider;
    }

    private <T extends Control> T addBig(int row, T object) {
        object.setPrefWidth(widthBig);
        object.setMaxWidth(widthBig);
        pane.add(object, 1, row);
        return object;
    }

    private Label getLabel(String text) {
        return new Label(text.trim());
    }


    public TextField addTextField(int row, String text) {
        return add(row, getLabel(text), new TextField());
    }


    public ComboBox<XYNode> addNodeBox(int row, Control left) {
        ComboBox<XYNode> box = new ComboBox<>();
        GuiUtils.addNodeListener(box, graph);
        return add(row, left, box);

    }

    public ComboBox<XYNode> addNodeBox(int row, String text) {
        return addNodeBox(row, getLabel(text));
    }

    public ComboBox<Edge<XYNode, Double>> addEdgeBox(int row, Control left) {
        ComboBox<Edge<XYNode, Double>> box = new ComboBox<>();
        GuiUtils.addEdgeListener(box, graph);
        return add(row, left, box);
    }

    public ComboBox<Edge<XYNode, Double>> addEdgeBox(int row, String text) {
        return addEdgeBox(row, getLabel(text));
    }

    public Button addButton(int row, String text) {
        Button button = new Button(text);
        button.setPrefSize(width, ELEMENT_HEIGHT);
        pane.add(button, 0, row, 2, 1);
        return button;
    }


    public <T> ComboBox<T> addBox(int row, String text, List<T> elements) {

        ComboBox<T> box = new ComboBox<>();
        box.setItems(FXCollections.observableList(elements));
        if (box.getItems() != null && box.getItems().size() > 0)
            box.setValue(box.getItems().get(0));

        return add(row, getLabel(text), box);
    }


    public HBox addLine(int row, Control... nodes) {

        HBox box = new HBox();
        for (Control cur : nodes) {
            cur.setPrefSize(width / nodes.length, ELEMENT_HEIGHT);
         //   cur.setMinWidth(width / nodes.length);
         //   cur.setMaxWidth(width / nodes.length);
            box.getChildren().add(cur);
        }
        pane.add(box, 0, row, nodes.length, 1);

        return box;

    }


    public <T extends Control> T add(int row, Control smallLeft, T bigRight) {
        smallLeft.setPrefSize(widthSmall, ELEMENT_HEIGHT);
        smallLeft.setMinWidth(widthSmall);
        smallLeft.setMaxWidth(widthSmall);

        bigRight.setPrefSize(widthBig, ELEMENT_HEIGHT);
        bigRight.setMinWidth(widthBig);
        bigRight.setMaxWidth(widthBig);

        pane.add(smallLeft, 0, row);
        pane.add(bigRight, 1, row);

        return bigRight;
    }


    public GridPane getNode() {
        return pane;
    }


}
