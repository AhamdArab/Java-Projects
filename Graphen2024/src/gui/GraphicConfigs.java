package gui;

import de.jpp.algorithm.interfaces.SearchAlgorithm;
import gui.graphViews.GraphViews;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

public class GraphicConfigs {

    // LAYOUT VARS
    public static final int[] FRAME_SIZE = new int[]{1280, 800};

    public static final int[] GRAPH_PANE_SIZE = new int[]{1000, 650};
    public static final int GRAPH_EDGE_RADIUS = 20;


    //OTHER CONFIG


    public static Paint paintOpen = Color.YELLOW;
    public static Paint paintClosed = Color.DARKORANGE;
    public static Paint paintEdgeOpen = Color.YELLOW;
    public static Paint paintEdgeClosed = Color.ORANGE;

    public static Paint paintPath = Color.RED;

    public static Paint nodePaint = Color.DARKGRAY;
    public static Paint edgePaint = Color.DARKGRAY;

    public static int animationWaitTime = 200; //ms


    private static SearchAlgorithm<XYNode, Double, TwoDimGraph> search = null;

    public static int[] getGraphSize() {
        return new int[]{
                GRAPH_PANE_SIZE[0] - 2 * GRAPH_EDGE_RADIUS,
                GRAPH_PANE_SIZE[1] - 2 * GRAPH_EDGE_RADIUS
        };
    }

    public static synchronized void setSearch(SearchAlgorithm<XYNode, Double, TwoDimGraph> pSearch, GraphViews view) {
        // System.out.println("setSearch(" + pSearch + ", " + view);
        if (search != null)
            search.stop();
        view.resetColors();
        search = pSearch;
    }

    private static boolean bulkOperation = false;

    public static synchronized void setBulkOperationActive(boolean isOngoing) {
        bulkOperation = isOngoing;
    }

    public static synchronized boolean isBulkOperationActive() {
        return bulkOperation;
    }


}
