package graph_editor.example;

import graph_editor.graph.*;

public class ExampleGraphs {
    public static Graph triangle() {
        GraphBuilder builder = new GraphBuilder(3);
        builder.addEdge(0, 1);
        builder.addEdge(1, 2);
        builder.addEdge(2, 0);
        return builder.build();
    }

    public static Graph tree() {
        GraphBuilder builder = new GraphBuilder(6);
        builder.addEdge(0, 3);
        builder.addEdge(1, 3);
        builder.addEdge(2, 3);
        builder.addEdge(3, 4);
        builder.addEdge(4, 5);
        return builder.build();
    }

    private ExampleGraphs() {}
}