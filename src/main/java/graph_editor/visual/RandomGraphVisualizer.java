package graph_editor.visual;

import java.util.*;

import graph_editor.graph.*;
import graph_editor.geometry.Point;

import static graph_editor.draw.JavaFxGraphDrawer.CIRCLE_RADIUS;

// Assigns random coord in rectangle.
public class RandomGraphVisualizer implements GraphVisualizer {
    
    public static RandomGraphVisualizer byWidthAndHeight(int width, int height) {
        return new RandomGraphVisualizer(width, height);
    }

    public static GraphVisualization getVisualization(Graph graph, int width, int height) {
        RandomGraphVisualizer visualizer = new RandomGraphVisualizer(width, height);
        return visualizer.generateVisual(graph);
    }

    @Override
    public GraphVisualization generateVisual(Graph graph) {
        GraphVisualizationImpl visualization = new GraphVisualizationImpl(graph);
        for (Vertex vertex : graph.getVertices()) {
            visualization.setCoord(
                vertex, new Point(Math.min(Math.max(CIRCLE_RADIUS, rng.nextInt(width) - 2 * CIRCLE_RADIUS) + CIRCLE_RADIUS, width - 2 * CIRCLE_RADIUS),
                            Math.min(Math.max(CIRCLE_RADIUS, rng.nextInt(height) - 2 * CIRCLE_RADIUS) + CIRCLE_RADIUS, height - 2 * CIRCLE_RADIUS)));
        }
        return visualization;
    }

    private RandomGraphVisualizer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private static Random rng = new Random();
    private int width;
    private int height;
} 