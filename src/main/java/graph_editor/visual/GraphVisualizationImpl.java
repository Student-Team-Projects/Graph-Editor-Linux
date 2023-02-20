package graph_editor.visual;

import graph_editor.graph.*;
import java.util.*;

import graph_editor.geometry.Point;

// Only mutable in this package.
class GraphVisualizationImpl implements GraphVisualization {

    GraphVisualizationImpl(Graph graph) {
        vertex_coord = new HashMap<Vertex, Point>();
        this.graph = graph;
    }

    void setCoord(Vertex vertex, Point point) {
        vertex_coord.put(vertex, point);
    }

    @Override
    public Point getVertexPoint(Vertex vertex) {
        return vertex_coord.get(vertex);
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public Map<Vertex, Point> getVisualization() {
        return vertex_coord;
    }

    private Map<Vertex, Point> vertex_coord; 
    private Graph graph;
}