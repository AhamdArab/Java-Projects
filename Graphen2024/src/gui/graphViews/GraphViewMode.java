package gui.graphViews;

import de.jpp.model.TwoDimGraph;

public enum GraphViewMode {

    CIRCLE, PIXLE, RAW_PIXLE;


    public GraphView getView(TwoDimGraph graph) {

        if (this == CIRCLE) {
            return new GraphCircleView(graph);
        } else if (this == RAW_PIXLE) {
            return new GraphRawPixelView(graph);
        } else {
            return new GraphPixelView(graph);
        }


    }

}
