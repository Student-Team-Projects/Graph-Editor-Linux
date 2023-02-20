package graph_editor.graph;

// Only for this package
class EdgeImpl implements Edge {
    EdgeImpl(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public Vertex getSource() {
        return source;
    }

    @Override
    public Vertex getTarget() {
        return target;
    }

    private Vertex source;
    private Vertex target;
}