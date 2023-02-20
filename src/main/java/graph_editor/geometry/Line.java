package graph_editor.geometry;

import graph_editor.geometry.Point;

public class Line implements Geometry {
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    private Point start;
    private Point end;
}