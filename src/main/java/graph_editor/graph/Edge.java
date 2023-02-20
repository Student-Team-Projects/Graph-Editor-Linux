package graph_editor.graph;

import java.util.List;

// Immutable outside package.
public interface Edge {
    Vertex getSource();
    Vertex getTarget();
}