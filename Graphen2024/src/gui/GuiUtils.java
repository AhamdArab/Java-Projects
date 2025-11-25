package gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.ObservableGraph;

import java.util.ArrayList;
import java.util.Comparator;

public class GuiUtils {


    public static final int SPACING_BIG = 20;
    public static final int SPACING_SMALL = 5;

    private static <T> Pane getPaneWithoutListener(ObservableGraph<XYNode, Double> graph, ComboBox<T> choiceBox, String labelText, double labelWidth, double choiceBoxWidth) {
        HBox box = new HBox();

        Label label = new Label(labelText);
        label.setPrefWidth(labelWidth);
        // label.setAlignment(Pos.CENTER);
        choiceBox.setPrefWidth(choiceBoxWidth);

        box.getChildren().addAll(label, choiceBox);
        return box;

    }

    public static Pane buildEdgeChoosingPanel(ObservableGraph<XYNode, Double> graph, ComboBox<Edge<XYNode, Double>> choiceBox, String labelText, double labelWidth, double choiceBoxWidth) {
        graph.addEdgeAddedListener(edge -> updateEdgeBox(choiceBox, graph));
        graph.addEdgeRemovedListener(edge -> updateEdgeBox(choiceBox, graph));

        return getPaneWithoutListener(graph, choiceBox, labelText, labelWidth, choiceBoxWidth);
    }

    public static Pane buildNodeChoosingPanel(ObservableGraph<XYNode, Double> graph, ComboBox<XYNode> choiceBox, String labelText, double labelWidth, double choiceBoxWidth) {
        graph.addNodeAddedListener(node -> updateNodeBox(choiceBox, graph));
        graph.addNodeRemovedListener(node -> updateNodeBox(choiceBox, graph));

        return getPaneWithoutListener(graph, choiceBox, labelText, labelWidth, choiceBoxWidth);
    }

    public static void addEdgeListener(ComboBox<Edge<XYNode, Double>> box, TwoDimGraph graph) {
        graph.addEdgeAddedListener(edge -> updateEdgeBox(box, graph));
        graph.addEdgeRemovedListener(edge -> updateEdgeBox(box, graph));
    }

    public static void addNodeListener(ComboBox<XYNode> box, TwoDimGraph graph) {
        graph.addNodeAddedListener(node -> updateNodeBox(box, graph));
        graph.addNodeRemovedListener(node -> updateNodeBox(box, graph));
    }

    private static void updateNodeBox(ComboBox<XYNode> box, Graph<XYNode, Double> graph) {
        if (GraphicConfigs.isBulkOperationActive()) {
            return;
        }

        ObservableList<XYNode> nodes = FXCollections.observableList(new ArrayList<>(graph.getNodes()));
        nodes.sort(Comparator.comparing(XYNode::getX).thenComparing(XYNode::getY));

        Platform.runLater(() -> {
            box.setItems(nodes);
            if (!nodes.isEmpty())
                box.setValue(nodes.get(0));
        });
    }

    private static void updateEdgeBox(ComboBox<Edge<XYNode, Double>> box, Graph<XYNode, Double> graph) {
        if (GraphicConfigs.isBulkOperationActive()) {
            return;
        }
        ObservableList<Edge<XYNode, Double>> edges = FXCollections.observableList(new ArrayList<>(graph.getEdges()));
        edges.sort(Comparator.comparing(Edge::toString));

        Platform.runLater(() -> {
            box.setItems(edges);
            if (!edges.isEmpty())
                box.setValue(edges.get(0));
        });
    }


}
