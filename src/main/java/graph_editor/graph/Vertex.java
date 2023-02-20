package graph_editor.graph;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.List;
import java.util.Map;

// Immutable outside package.
public interface Vertex {
    int getIndex();
    List<Edge> getEdges();
    List<Vertex> getAdjacent();
    Circle getCircle();
    void setCircle(Circle other);
    Map<Vertex, Line> getLines();

    void setLines(Map<Vertex, Line> other);
}