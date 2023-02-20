package graph_editor.graph;

import java.util.*;

class GraphImpl implements Graph {
    GraphImpl(List<Vertex> vertices) {
        this.vertices = vertices;
        edges = new ArrayList<Edge>();
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.getEdges()) {
                edges.add(edge);
            }
        }
    } 

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }

    private List<Edge> edges;
    private List<Vertex> vertices;
}