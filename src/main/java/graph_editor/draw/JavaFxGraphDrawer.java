package graph_editor.draw;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.io.FileWriter;
import java.io.IOException;

import graph_editor.visual.*;
import graph_editor.graph.*;
import graph_editor.geometry.Point;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
import java.io.File;

import java.util.*;

public class JavaFxGraphDrawer extends AbstractGraphDrawer {
    public JavaFxGraphDrawer() {
        this.edge_path = new Path();
        this.group = new Group();
    }

    public static Group getGraphGroup(GraphVisualization visual) {
        JavaFxGraphDrawer drawer = new JavaFxGraphDrawer();
        drawer.drawGraph(visual);
        if ( !redoUndo ) {
            System.out.println(redoUndo);
            saveforUndoRedo();
        } else {
            JavaFxGraphDrawer.redoUndo = false;
        }
        return drawer.getJavaFxGroup();
    }

    public static Group drawGraph(Graph graph, int width, int height, boolean redoUndo) {
        JavaFxGraphDrawer.redoUndo = redoUndo;
        verteces = graph.getVertices();
        GraphVisualization visual = RandomGraphVisualizer.getVisualization(graph, width, height);
        System.out.println(redoUndo);
        return JavaFxGraphDrawer.getGraphGroup(visual);
    }

    public Group getJavaFxGroup() {
        selectedVertex = null;
        group.getChildren().add(edge_path);
        return group;
    }
    
    @Override
    public void moveCursorTo(Point p) {
        edge_path.getElements().add(new MoveTo(p.getX(), p.getY()));
    }

    public static void saveGraphToFile(List<Pair<Integer, Integer>> edges, File file) {
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            writer.write(verteces.size() + "\n");

            for(Pair<Integer, Integer> edge : edges) {
                System.out.println(edge);
                writer.write(edge.getKey() + " " + edge.getValue() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }

    @Override
    public void drawLine(Line l) {
        group.getChildren().add(l);
        Text weight = new Text("0");

        weight.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) { // right button clicked
                    weight.setText(String.valueOf(Integer.parseInt(weight.getText()) + 1));
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    weight.setText(String.valueOf(Integer.parseInt(weight.getText()) - 1));
                }
            }
        });

        weight.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                weight.setText(String.valueOf(Integer.parseInt(weight.getText()) + (int)Math.signum(event.getDeltaY())));
            }
        });


        if (weightsVisible) {
            weight.setFont(Font.font("Monospace", WEIGHT_SIZE));
        } else {
            weight.setFont(Font.font("Monospace", 0));
        }

        group.getChildren().add(weight);
        mapOfWeights.put(l, weight);
        weight.setLayoutX((l.getStartX() + l.getEndX()) / 2);
        weight.setLayoutY((l.getStartY() + l.getEndY()) / 2);
        l.toBack();
        l.setSmooth(true);
        
    }

    public static void saveforUndoRedo(){
        currentState++;
        maxState = currentState;

        List<Pair<Integer, Integer>> edges = new ArrayList();
        
        for(Vertex vertex: verteces) {
            if (vertex.getLines() != null){
                for (Vertex adj: vertex.getLines().keySet()) {
                    if (vertex.getIndex() < adj.getIndex()){
                        edges.add(new Pair(vertex.getIndex(), adj.getIndex()));
                    }
                }
            }
        }

        System.out.println(System.getProperty("user.dir") + "/UndoRedo/graph" + currentState + ".txt");
        saveGraphToFile(edges, new File(System.getProperty("user.dir") + "/UndoRedo/graph" + currentState + ".txt"));
    }

    @Override
    public Circle drawCircle(Point p, Vertex v) {
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        Text text = new Text(String.valueOf(v.getIndex()));
        text.setFont(Font.font("Monospace", CIRCLE_RADIUS * NUMBER_RATIO));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, text);
        stackPane.setLayoutX(p.getX() - CIRCLE_RADIUS);
        stackPane.setLayoutY(p.getY() - CIRCLE_RADIUS);
        class Delta { double x, y; }
        final Delta dragDelta = new Delta();
        final Delta lineDelta = new Delta();

        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) { // left button clicked
                    return;
                } else if (selectedVertex == null) { // select first vertex
                    selectedVertex = v;
                    selectedPane = stackPane;
                    for (Node node : selectedPane.getChildren()) {
                        if (node instanceof Circle) {
                            Circle circle = (Circle) node;
                            circle.setRadius(CIRCLE_RADIUS + 5);
                        }
                    }
                } else if (v == selectedVertex) { // 2nd vertex is the same
                    for (Node node : selectedPane.getChildren()) {
                        if (node instanceof Circle) {
                            Circle circle = (Circle) node;
                            circle.setRadius(CIRCLE_RADIUS);
                        }
                    }
                    selectedVertex = null;
                    selectedPane = null;
                } else { // correctly selected nodes
                    if (v.getLines().containsKey(selectedVertex)) { // remove line
                        Line line = v.getLines().get(selectedVertex);
                        v.getLines().remove(selectedVertex);
                        selectedVertex.getLines().remove(v);
                        group.getChildren().remove(line);
                        group.getChildren().remove(mapOfWeights.get(line));
                        mapOfWeights.remove(line);
                        System.out.println(redoUndo);
                        if ( !redoUndo ) {
                            saveforUndoRedo();
                        } else {
                            JavaFxGraphDrawer.redoUndo = false;
                        }
                    } else { // add line
                        Line line = new Line();
                        if (v.getIndex() < selectedVertex.getIndex()) {
                            line.setStartX(stackPane.getLayoutX() + CIRCLE_RADIUS);
                            line.setStartY(stackPane.getLayoutY() +  CIRCLE_RADIUS);
                            line.setEndX(selectedPane.getLayoutX() + CIRCLE_RADIUS);
                            line.setEndY(selectedPane.getLayoutY() + CIRCLE_RADIUS);
                        } else {
                            line.setEndX(stackPane.getLayoutX() + CIRCLE_RADIUS);
                            line.setEndY(stackPane.getLayoutY() + CIRCLE_RADIUS);
                            line.setStartX(selectedPane.getLayoutX() + CIRCLE_RADIUS);
                            line.setStartY(selectedPane.getLayoutY() + CIRCLE_RADIUS);
                        }
                        drawLine(line);
                        v.getLines().put(selectedVertex, line);
                        selectedVertex.getLines().put(v, line);
                        System.out.println(redoUndo);
                        if ( !redoUndo ) {
                            saveforUndoRedo();
                        } else {
                            JavaFxGraphDrawer.redoUndo = false;
                        }
                    }
                    for (Node node : selectedPane.getChildren()) {
                        if (node instanceof Circle) {
                            Circle circle = (Circle) node;
                            circle.setRadius(CIRCLE_RADIUS);
                        }
                    }
                    selectedVertex = null;
                    selectedPane = null;
                }
            }
        });

        stackPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    return;
                }
                dragDelta.x = stackPane.getLayoutX() - event.getSceneX();
                dragDelta.y = stackPane.getLayoutY() - event.getSceneY();
                if (!v.getLines().isEmpty()) {
                    Vertex adj = (Vertex) v.getLines().keySet().toArray()[0];
                    if (v.getIndex() < adj.getIndex()) {
                        lineDelta.x = v.getLines().get(adj).getStartX() - event.getSceneX();
                        lineDelta.y = v.getLines().get(adj).getStartY() - event.getSceneY();
                    } else {
                        lineDelta.x = v.getLines().get(adj).getEndX() - event.getSceneX();
                        lineDelta.y = v.getLines().get(adj).getEndY() - event.getSceneY();
                    }
                }
            }
        });

        stackPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    return;
                }
                stackPane.setLayoutX(event.getSceneX() + dragDelta.x);
                stackPane.setLayoutY(event.getSceneY() + dragDelta.y);
                for (Vertex to : v.getLines().keySet()) {
                    Line line = v.getLines().get(to);
                    if (v.getIndex() < to.getIndex()) {
                        line.setStartX(event.getSceneX() + lineDelta.x);
                        line.setStartY(event.getSceneY() + lineDelta.y);
                    } else {
                        line.setEndX(event.getSceneX() + lineDelta.x);
                        line.setEndY(event.getSceneY() + lineDelta.y);
                    }
                    mapOfWeights.get(line).setLayoutX((line.getStartX() + line.getEndX()) / 2);
                    mapOfWeights.get(line).setLayoutY((line.getStartY() + line.getEndY()) / 2);
                }
            }
        });

        group.getChildren().add(stackPane);
        allStacks.add(stackPane);

        return circle;
    }

    public static int getCurrentState() {
        return currentState;
    }

    public static void setCurrentState ( int currentState ) {
        JavaFxGraphDrawer.currentState = currentState;
    }

    public static int getMaxState(){
        return maxState;
    }

    public static void setMaxState ( int maxState ) {
        JavaFxGraphDrawer.maxState = maxState;
    }

    public static final int CIRCLE_RADIUS = 20;
    public static final int WEIGHT_SIZE = 18;

    public static final double NUMBER_RATIO = 1.1;

    private Path edge_path;
    private static int currentState = -1;
    private static int maxState = -1;
    private static List<Vertex> verteces;
    private static boolean redoUndo = false;
    private Group group;
    public static StackPane selectedPane = null;
    public static Vertex selectedVertex = null;
    public static Set<StackPane> allStacks = new HashSet<>();
    public static Map<Line, Text> mapOfWeights = new HashMap<>();
    public static boolean weightsVisible = false;
}