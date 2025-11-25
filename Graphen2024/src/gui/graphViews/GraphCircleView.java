package gui.graphViews;

import gui.Arrow;
import gui.GraphicConfigs;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;

import java.util.HashMap;
import java.util.Map;

public class GraphCircleView implements GraphView {

    private static final double XYNODE_RADIUS = 20;

    private Map<XYNode, Pane> nodeMap = new HashMap<>();
    private Map<Edge<XYNode, Double>, Arrow> edgeMap = new HashMap<>();
    private Pane mainPane;

    private TwoDimGraph graph;

    public boolean active = true;

    public GraphCircleView(TwoDimGraph graph) {
        this.graph = graph;

        mainPane = new Pane();
        mainPane.setPrefSize(GraphicConfigs.getGraphSize()[0], GraphicConfigs.getGraphSize()[1]);
        mainPane.setMaxSize(GraphicConfigs.getGraphSize()[0], GraphicConfigs.getGraphSize()[1]);

        graph.addNodeAddedListener(this::redrawNode);

        graph.addNodeRemovedListener(node -> {
            if (nodeMap.containsKey(node))
                mainPane.getChildren().remove(nodeMap.get(node));
            nodeMap.remove(node);
        });

        graph.addEdgeAddedListener(this::redrawEdge);


        graph.addEdgeRemovedListener(edge -> {
            if (edgeMap.containsKey(edge))
                mainPane.getChildren().remove(edgeMap.get(edge));
            edgeMap.remove(edge);
        });

    }

    public Pane getNode() {
        return mainPane;
    }


    public void setColor(XYNode node, Paint paint) {
        if (!nodeMap.containsKey(node))
            return;
        try {
            Shape s = (Shape) nodeMap.get(node).lookup("#circle");
            s.setFill(paint);
        } catch (Exception e) {
            System.out.println("This should never happen");
            e.printStackTrace();
        }
    }

    public void setColor(Edge<XYNode, Double> edge, Paint paint) {
        if (!edgeMap.containsKey(edge))
            return;

        edgeMap.get(edge).setFill(paint);
        edgeMap.get(edge).toFront();
    }

    public void resetColors() {
        for (Edge<XYNode, Double> edge : edgeMap.keySet())
            setColor(edge, GraphicConfigs.edgePaint);

        for (XYNode node : nodeMap.keySet())
            setColor(node, GraphicConfigs.nodePaint);
    }

    @Override
    public int getSafetyDist() {
        return (int) (2 * XYNODE_RADIUS);
    }


    private static Pane getNode(XYNode node) {
        StackPane box = new StackPane();

        box.setLayoutX(node.getX() - XYNODE_RADIUS);
        box.setLayoutY(node.getY() - XYNODE_RADIUS);

        Circle circle = new Circle(XYNODE_RADIUS, XYNODE_RADIUS, XYNODE_RADIUS, GraphicConfigs.nodePaint);
        circle.setId("circle");

        Label label = new Label(node.getLabel());


        box.getChildren().addAll(circle, label);

        return box;

    }

    @Override
    public void redrawGraph() {
        if (!isActive())
            return;

        nodeMap.clear();
        edgeMap.clear();

        for (XYNode node : graph.getNodes())
            redrawNode(node);

        for (Edge<XYNode, Double> edge : graph.getEdges())
            redrawEdge(edge);
    }

    @Override
    public GraphViewMode calculateActiveMode() {
        return GraphViewMode.CIRCLE;
    }


    public void redrawNode(XYNode node) {

        if (!isActive())
            return;

        if (!GraphicConfigs.isBulkOperationActive())
            if (!nodeMap.containsKey(node)) {
                Pane nodeFX = getNode(node);
                mainPane.getChildren().add(nodeFX);
                nodeMap.put(node, nodeFX);
            }
    }


    public void redrawEdge(Edge<XYNode, Double> edge) {

        if (!isActive())
            return;

        if (!edgeMap.containsKey(edge)) {
            Arrow arrow = getArrow(edge);
            mainPane.getChildren().add(arrow);
            edgeMap.put(edge, arrow);
        }
    }

    private static Arrow getArrow(Edge<XYNode, Double> edge) {

        double[] dir = {
                edge.getStart().getX() - edge.getDestination().getX(),
                edge.getStart().getY() - edge.getDestination().getY()
        };

        double length = Math.sqrt(dir[0] * dir[0] + dir[1] * dir[1]);
        dir = new double[]{
                Math.abs(dir[0] / length * (XYNODE_RADIUS + 5)),
                Math.abs(dir[1] / length * (XYNODE_RADIUS + 5))
        };

        double[] arr = new double[]{edge.getStart().getX(), edge.getStart().getY(), edge.getDestination().getX(), edge.getDestination().getY()};

        if (edge.getStart().getX() < edge.getDestination().getX()) {
            arr[0] += dir[0];
            arr[2] -= dir[0];
        } else {
            arr[0] -= dir[0];
            arr[2] += dir[0];
        }

        if (edge.getStart().getY() < edge.getDestination().getY()) {
            arr[1] += dir[1];
            arr[3] -= dir[1];
        } else {
            arr[1] -= dir[1];
            arr[3] += dir[1];
        }
        Arrow arrow = new Arrow(arr[0], arr[1], arr[2], arr[3]);
        arrow.setId("arrow");
        arrow.setFill(GraphicConfigs.edgePaint);
        return arrow;
    }

    public boolean isActive() {
        return !GraphicConfigs.isBulkOperationActive() && active;
    }

    public void setInactive() {
        active = false;
    }


}


