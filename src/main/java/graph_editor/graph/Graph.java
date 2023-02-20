package graph_editor.graph;

import java.util.List;

// Immutable
public interface Graph {
    List<Edge> getEdges();
    List<Vertex> getVertices();
}