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

public class GraphRawPixelView implements GraphView {

    private GraphicsContext canvas;

    private Pane main;
    private TwoDimGraph graph;

    Map<XYNode, Paint> paintMap = new HashMap<>();

    public GraphRawPixelView(TwoDimGraph graph) {
        System.out.println("created!");
        this.graph = graph;


        main = new Pane();
        canvas = new Canvas(GraphicConfigs.getGraphSize()[0], GraphicConfigs.getGraphSize()[1]).getGraphicsContext2D();
        main.getChildren().add(canvas.getCanvas());

    }

    @Override
    public Pane getNode() {
        return main;
    }

    @Override
    public void setColor(XYNode node, Paint paint) {
        paintMap.put(node, paint);
        canvas.setFill(paint);
        canvas.fillRect(node.getX(), node.getY(), 1, 1);
    }

    @Override
    public void setColor(Edge<XYNode, Double> edge, Paint paint) {
    }

    @Override
    public void resetColors() {
        paintMap.clear();
        redrawGraph();
    }

    @Override
    public int getSafetyDist() {
        return 0;
    }

    @Override
    public void redrawGraph() {
        if(GraphicConfigs.isBulkOperationActive())
            return;

        for (XYNode node : graph.getNodes())
            redrawNode(node);

    }

    private void redrawNode(XYNode node) {
        Paint paint = paintMap.containsKey(node) ? paintMap.get(node) : GraphicConfigs.nodePaint;
        canvas.setFill(paint);
        canvas.fillRect((int) node.getX(), (int) node.getY(), 1, 1);
    }

    @Override
    public GraphViewMode calculateActiveMode() {
        return GraphViewMode.RAW_PIXLE;
    }

    private boolean active = true;

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setInactive() {
        active = false;
    }

}
