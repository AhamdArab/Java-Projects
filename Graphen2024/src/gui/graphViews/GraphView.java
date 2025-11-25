package gui.graphViews;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;

public interface GraphView {

     Pane getNode();

     void setColor(XYNode node, Paint paint);

     void setColor(Edge<XYNode, Double> edge, Paint paint);

     void resetColors();

     int getSafetyDist();

     /*
    void redrawNode(XYNode node);

    void redrawEdge(Edge<XYNode, Double> edge);*/

    void redrawGraph();

    GraphViewMode calculateActiveMode();

    boolean isActive();

    void setInactive();

}
