package de.jpp.model;

import java.util.Objects;

public class XYNode {

    String label;
    double x;
    double y;

    public XYNode(String label, double x, double y) {
        this.label = Objects.requireNonNullElse(label, "");
        this.x = x;
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double euclidianDistTo(XYNode other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XYNode xyNode)) return false;
        return Double.compare(xyNode.x, x) == 0 && Double.compare(xyNode.y, y) == 0 && Objects.equals(label, xyNode.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, x, y);
    }

    @Override
    public String toString() {
        return "XYNode{" +
                "label='" + label + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
