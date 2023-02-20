package graph_editor.visual;

import graph_editor.geometry.Point;
import graph_editor.graph.*;

import java.util.Map;

// Immutable
public interface GraphVisualization {
    Point getVertexPoint(Vertex vertex);
    Graph getGraph();
    Map<Vertex, Point> getVisualization();
}