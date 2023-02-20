package graph_editor.graph;

import java.util.*;
import java.lang.*;

public class GraphBuilder {
    public GraphBuilder(int vertex_count) {
        vertices = new ArrayList<VertexImpl>();
        for (int i = 0; i < vertex_count; i++) {
            vertices.add(new VertexImpl(i));
        }
    }

    public void addVertex() {
        vertices.add(new VertexImpl(vertices.size()));
    }

    public void addEdge(int source_index, int target_index) {
        if (source_index >= vertices.size() || target_index >= vertices.size()) {
            throw new IllegalArgumentException("Node index out of bounds.");
        }

        Edge edge = new EdgeImpl(vertices.get(source_index), vertices.get(target_index));
        Edge rev_edge = new EdgeImpl(vertices.get(target_index), vertices.get(source_index));
        vertices.get(source_index).addEdge(edge);
        vertices.get(target_index).addEdge(rev_edge);
    }

    public Graph build() {
        return new GraphImpl(vertices.stream().map(v -> (Vertex)v).toList());
    }

    private ArrayList<VertexImpl> vertices;
}