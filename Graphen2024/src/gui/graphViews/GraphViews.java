package gui.graphViews;

import gui.GraphicConfigs;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class GraphViews implements GraphView {


    private HBox main;

    private SimpleObjectProperty<GraphViewMode> mode = new SimpleObjectProperty<>(null);
    private GraphView view;
    private static final int RADIUS = 20;

    private Consumer<String> handleErrorMsg;

    private TwoDimGraph graph;

    public GraphViews(TwoDimGraph graph, Consumer<String> handleErrorMsg) {
        this.handleErrorMsg = handleErrorMsg;
        this.graph = graph;

        initPane();

        mode.addListener(this::handleModeChanged);
        mode.setValue(GraphViewMode.CIRCLE);

    }

    private void initPane() {

        main = new HBox();
        main.setAlignment(Pos.CENTER);
        main.setPrefSize(GraphicConfigs.GRAPH_PANE_SIZE[0], GraphicConfigs.GRAPH_PANE_SIZE[1]);
        main.setMaxSize(GraphicConfigs.GRAPH_PANE_SIZE[0], GraphicConfigs.GRAPH_PANE_SIZE[1]);
        main.setBorder(new Border(new BorderStroke(
                Color.DARKGRAY,
                BorderStrokeStyle.SOLID,
                new CornerRadii(RADIUS),
                new BorderWidths(5, 5, 5, 5))));

    }

    public void handleModeChanged(ObservableValue<? extends GraphViewMode> mode, GraphViewMode oldMode, GraphViewMode newMode) {


        if (newMode == null) {
            main.getChildren().clear();
            return;
        }

        if (newMode == calculateActiveMode()) {
            return;
        }


        int objCount = graph.getNodes().size() + graph.getEdges().size();
        if (newMode == GraphViewMode.CIRCLE && objCount > 500) {
            handleErrorMsg.accept("Cannot change to circle mode: too many objects (" + objCount + ", max 500)");
            this.mode.setValue(oldMode);
            return;
        }


        //remove old stuff:
        if (view != null) {
            view.setInactive();
            view.getNode().getChildren().clear();
        }

        main.getChildren().clear();


        view = newMode.getView(graph);
        view.getNode().setStyle("-fx-background-color:#eee");

        redrawGraph();

        main.getChildren().add(view.getNode());


    }


    @Override
    public Pane getNode() {
        return main;
    }

    public GraphViewMode calculateActiveMode() {
        return view != null ? view.calculateActiveMode() : null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setInactive() {
    }

    @Override
    public void setColor(XYNode node, Paint paint) {
        view.setColor(node, paint);
    }

    @Override
    public void setColor(Edge<XYNode, Double> edge, Paint paint) {
        view.setColor(edge, paint);
    }

    @Override
    public void resetColors() {
        view.resetColors();
    }

    @Override
    public int getSafetyDist() {
        return view.getSafetyDist();
    }

    public Property<GraphViewMode> getMode() {
        return mode;
    }

    public void redrawGraph() {
        view.redrawGraph();
    }

    public void loadGraphIntoView(TwoDimGraph curGraph, TwoDimGraph toLoad) {

        Platform.runLater(() -> {
            List<XYNode> nodes = new ArrayList<>(toLoad.getNodes());
            List<Edge<XYNode, Double>> edges = new ArrayList<>(toLoad.getEdges());

            //Bulk Nodes
            GraphicConfigs.setBulkOperationActive(true);

            curGraph.clear();

            int objCount = toLoad.getNodes().size() + toLoad.getEdges().size();
            if (objCount > 500)
                mode.setValue(GraphViewMode.PIXLE);


            for (int i = 1; i < nodes.size(); i++)
                curGraph.addNode(nodes.get(i));

            GraphicConfigs.setBulkOperationActive(false);

            //Last node
            if (nodes.size() > 0)
                curGraph.addNode(nodes.get(0));

            //Bulk edges
            GraphicConfigs.setBulkOperationActive(true);
            for (int i = 1; i < edges.size(); i++) {
                curGraph.addEdge(edges.get(i).getStart(), edges.get(i).getDestination(), edges.get(i).getAnnotation());
            }

            //Last edge
            GraphicConfigs.setBulkOperationActive(false);
            if (edges.size() > 0)
                curGraph.addEdge(edges.get(0).getStart(), edges.get(0).getDestination(), edges.get(0).getAnnotation());


        });
    }

    public void colorPath(XYNode start, Collection<Edge<XYNode, Double>> path) {
        Platform.runLater(() -> {
            for (Edge<XYNode, Double> edge : path) {
                setColor(edge, GraphicConfigs.paintPath);
                setColor(edge.getDestination(), GraphicConfigs.paintPath);
            }
            setColor(start, GraphicConfigs.paintPath);
        });
    }

}
