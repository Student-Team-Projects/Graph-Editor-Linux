package graph_editor.draw;

import graph_editor.visual.*;
import graph_editor.graph.*;
import graph_editor.geometry.Point;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;

public abstract class AbstractGraphDrawer implements GraphDrawer {
    @Override
    public void drawGraph(GraphVisualization visual) {
        this.visualization = visual;
        this.graph = visual.getGraph();
        visited_nodes = new HashSet<Vertex>();

        for (Vertex v : graph.getVertices()) {
            v.setLines(new HashMap<>());
            Point cur_point = visualization.getVertexPoint(v);
            moveCursorTo(cur_point);
            v.setCircle(drawCircle(cur_point, v));
        }

        Set<Vertex> visited = new HashSet<>();

        for (Vertex v : graph.getVertices()) {
            visited.add(v);
            Point cur_point = visualization.getVertexPoint(v);

            for (Vertex to : v.getAdjacent()) {
                if (visited.contains(to)) {
                    continue;
                }

                Point next_point = visualization.getVertexPoint(to);

                Line line = new Line();
                if (v.getIndex() < to.getIndex()) {
                    line.setStartX(cur_point.getX());
                    line.setStartY(cur_point.getY());
                    line.setEndX(next_point.getX());
                    line.setEndY(next_point.getY());
                } else {
                    line.setEndX(cur_point.getX());
                    line.setEndY(cur_point.getY());
                    line.setStartX(next_point.getX());
                    line.setStartY(next_point.getY());
                }

                v.getLines().put(to, line);
                to.getLines().put(v, line);
                drawLine(line);
            }
        }
    }

    abstract void moveCursorTo(Point p);
    abstract void drawLine(Line l);

    private Graph graph;
    private GraphVisualization visualization;
    private Set<Vertex> visited_nodes;

    public abstract Circle drawCircle(Point p, Vertex v);
}