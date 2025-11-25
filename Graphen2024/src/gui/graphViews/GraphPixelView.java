package gui.graphViews;

import gui.GraphicConfigs;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;

import java.util.HashMap;
import java.util.Map;

public class GraphPixelView implements GraphView {

    private static final int PX_SIZE = 3;
    private static final int PX_EXTRA_NODE = 2;

    private Pane main;

    private GraphicsContext canvas;

    private TwoDimGraph graph;

    private Map<XYNode, Paint> nodePaint = new HashMap<>();
    private Map<Edge<XYNode, Double>, Paint> edgePaint = new HashMap<>();

    public GraphPixelView(TwoDimGraph graph) {
        this.graph = graph;

        main = new Pane();

        canvas = new Canvas(GraphicConfigs.getGraphSize()[0], GraphicConfigs.getGraphSize()[1]).getGraphicsContext2D();
        main.getChildren().add(canvas.getCanvas());

        graph.addNodeAddedListener(node -> setColor(node, GraphicConfigs.nodePaint));
        graph.addNodeRemovedListener(node -> redrawGraph());
        graph.addEdgeAddedListener(edge -> setColor(edge, GraphicConfigs.edgePaint));
        graph.addEdgeRemovedListener(edge -> redrawGraph());
    }

    public void redrawGraph() {
        if (isActive())
            return;

        for (XYNode node : graph.getNodes())
            redrawNode(node);

        for (Edge<XYNode, Double> edge : graph.getEdges())
            redrawEdge(edge);

    }


    @Override
    public GraphViewMode calculateActiveMode() {
        return GraphViewMode.PIXLE;
    }


    private boolean active = true;

    @Override
    public boolean isActive() {
        return !GraphicConfigs.isBulkOperationActive() && active;
    }

    @Override
    public void setInactive() {
        active = false;
    }

    private static int[] getGridStartPos(double[] pos) {
        return new int[]{
                PX_SIZE * 2 + (int) (pos[0] * 3 * PX_SIZE),
                PX_SIZE * 2 + (int) (pos[1] * 3 * PX_SIZE)
        };
    }

    private static int[] getNodePixel(XYNode node) {
        int[] drawPos = getGridStartPos(new double[]{node.getX(), node.getY()});
        return new int[]{drawPos[0] + PX_SIZE, drawPos[1] + PX_SIZE};
    }

    private int[] getEdgePixel(Edge<XYNode, Double> edge) {
        double dx = edge.getDestination().getX() - edge.getStart().getX();
        double dy = edge.getDestination().getY() - edge.getStart().getY();

        double dist = Math.sqrt(dx * dx + dy * dy);

        int idx = (int) (PX_SIZE * Math.round(dx / dist));
        int idy = (int) (PX_SIZE * Math.round(dy / dist));

        int[] nodePos = getNodePixel(edge.getStart());

        return new int[]{
                nodePos[0] + idx,
                nodePos[1] + idy
        };
    }


    @Override
    public Pane getNode() {
        return main;
    }

    public void redrawNode(XYNode node) {

        int[] pos = getNodePixel(node);
        Paint paint = nodePaint.containsKey(node) ? nodePaint.get(node) : GraphicConfigs.nodePaint;
        canvas.setFill(paint);
        canvas.fillRect(pos[0] - PX_EXTRA_NODE, pos[1] - PX_EXTRA_NODE, PX_SIZE + 2 * PX_EXTRA_NODE, PX_SIZE + 2 * PX_EXTRA_NODE);
    }

    public void redrawEdge(Edge<XYNode, Double> edge) {
        int[] pos = getEdgePixel(edge);
        Paint paint = edgePaint.containsKey(edge) ? edgePaint.get(edge) : GraphicConfigs.edgePaint;
        canvas.setFill(paint);
        canvas.fillRect(pos[0], pos[1], PX_SIZE, PX_SIZE);
    }

    @Override
    public void setColor(XYNode node, Paint paint) {
        nodePaint.put(node, paint);
        redrawNode(node);
    }

    @Override
    public void setColor(Edge<XYNode, Double> edge, Paint paint) {
        edgePaint.put(edge, paint);
        redrawEdge(edge);
    }

    @Override
    public void resetColors() {
        nodePaint.clear();
        edgePaint.clear();
        redrawGraph();
    }

    @Override
    public int getSafetyDist() {
        return 1;
    }
}
