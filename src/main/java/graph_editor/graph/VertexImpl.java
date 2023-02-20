package graph_editor.graph;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;

// Only for this package
class VertexImpl implements Vertex {
    VertexImpl(int index) {
        this.index = index;
        edges = new ArrayList<>();
    }

    void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public List<Vertex> getAdjacent() {
        return edges.stream().map(edge -> edge.getTarget()).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vertex v) {
            return v.getIndex() == getIndex();
        }
        return false;
    }

    @Override
    public Circle getCircle() {
        return circle;
    }

    @Override
    public void setCircle(Circle other) {
        circle = other;
    }

    @Override
    public Map<Vertex, Line> getLines() {
        return mapOfLines;
    }

    @Override
    public void setLines(Map<Vertex, Line> other) {
        mapOfLines = other;
    }

    @Override
    public int hashCode() {
        return index;
    }

    private int index;
    private ArrayList<Edge> edges;

    private Circle circle;
    private Map<Vertex, Line> mapOfLines;
}