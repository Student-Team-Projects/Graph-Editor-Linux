package graph_editor.geometry;

import graph_editor.geometry.Point;

public class Circle implements Geometry {
    public Circle(Point center, double radius) {
        this.radius = radius;
        this.center = center;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    private double radius;
    private Point center;
}