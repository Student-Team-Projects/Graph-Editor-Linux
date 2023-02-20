import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import java.util.Scanner;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import graph_editor.graph.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.Group; 
import java.io.IOException;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;



import graph_editor.draw.JavaFxGraphDrawer;
import graph_editor.example.ExampleGraphs;

import static graph_editor.draw.JavaFxGraphDrawer.*;


public class MainMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane dataPane;

    @FXML
    private Button drawButton;

    @FXML
    private TextArea edgesField;

    @FXML
    private Menu fileButton;

    @FXML
    private Pane graphPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem newButton;

    @FXML
    private TextField numberOfVertices;

    @FXML
    private MenuItem saveAsButton;

    @FXML
    private MenuItem saveButton;

    @FXML
    private Button undo;

    @FXML
    private Button redo;

    @FXML
    void onRedoClicked(MouseEvent event) {
        if (JavaFxGraphDrawer.getCurrentState() < JavaFxGraphDrawer.getMaxState()) {
            File file = new File(System.getProperty("user.dir") + "/UndoRedo/graph" + 
                (JavaFxGraphDrawer.getCurrentState()+1) + ".txt");
            JavaFxGraphDrawer.setCurrentState(JavaFxGraphDrawer.getCurrentState() + 1);    
            try {
                drawGraph(parseGraphFromFile(file), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    

    @FXML
    void onUndoClicked(MouseEvent event) {
        if (JavaFxGraphDrawer.getCurrentState() > 0) {
            File file = new File(System.getProperty("user.dir") + "/UndoRedo/graph" + 
                (JavaFxGraphDrawer.getCurrentState() - 1) + ".txt");
            JavaFxGraphDrawer.setCurrentState(JavaFxGraphDrawer.getCurrentState() - 1);
            try {
                drawGraph(parseGraphFromFile(file), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void drawGraph(Graph graph, boolean redoUndo){
        Group canvasGroup = JavaFxGraphDrawer.drawGraph(graph, (int)graphPane.getScene().getWidth() - 400, (int)graphPane.getScene().getHeight() - 25, redoUndo);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(canvasGroup);
    }

    void saveGraphToFile(List<Vertex> verteces, List<Edge> edges, File file) throws IOException{
        FileWriter writer = new FileWriter(file);

        writer.write(verteces.size() + "\n");
        
        for(Edge edge : edges) {
            writer.write(edge.getSource().getIndex() + " " + edge.getTarget().getIndex() + "\n");
        }

        writer.close();
    }


    @FXML
    void saveAsButtonClicked(ActionEvent event) {
        FileChooser FC = new FileChooser();
        FC.setTitle("Save Graph");
        FC.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = FC.showSaveDialog(graphPane.getScene().getWindow());
        
        if (file != null) {
            try {
                Graph graph = parseGraphFromFile(new File(System.getProperty("user.dir") + "/UndoRedo/graph" + 
                    JavaFxGraphDrawer.getCurrentState() + ".txt"));
                List<Edge> edges = graph.getEdges();
                List<Vertex> verteces = graph.getVertices();
                saveGraphToFile(verteces, edges, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void OpenButtonClicked(ActionEvent event) {
        FileChooser FC = new FileChooser();
        FC.setTitle("Open Graph");
        FC.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = FC.showOpenDialog(graphPane.getScene().getWindow());
        
        if (file != null) {
            try {
                drawGraph(parseGraphFromFile(file), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    Graph parseGraphFromFile(File graphFile) throws FileNotFoundException{
        Scanner scanner = new Scanner(graphFile);
        int nodeNumber = Integer.parseInt(scanner.nextLine());;
        GraphBuilder builder = new GraphBuilder(nodeNumber);
        String[] edges = edgesField.getText().split(System.lineSeparator());

        while(scanner.hasNextLine()) {
            String[] edge = scanner.nextLine().split("\\s+");
            if (edge.length != 2) break;
            builder.addEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
        }
        
        Graph graph = builder.build();

        return graph;
    }

    @FXML
    void onDrawClicled(MouseEvent event) {
        int nodeNumber = Integer.parseInt(numberOfVertices.getText());
        GraphBuilder builder = new GraphBuilder(nodeNumber);
        String[] edges = edgesField.getText().split(System.lineSeparator());
        for (String s : edges) {
            String[] edge = s.split("\\s+");
            if (edge.length != 2) break;
            builder.addEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[1]));
        }
        Graph graph = builder.build();
        drawGraph(graph, false);
    }

    @FXML
    void onYellowClicked(MouseEvent event) {
        changeColorVertices(Color.YELLOW);
    }

    @FXML
    void onRedClicked(MouseEvent event) {
        changeColorVertices(Color.RED);
    }

    @FXML
    void onAquaClicked(MouseEvent event) {
        changeColorVertices(Color.AQUA);
    }

    @FXML
    void onGreenClicked(MouseEvent event) {
        changeColorVertices(Color.color(0, 1, 0));
    }

    @FXML
    void onNumbersVisibleClicked(MouseEvent event) {
        for (StackPane stackPane : allStacks) {
            for (Node node : stackPane.getChildren()) {
                if (node instanceof Text) {
                    Text text = (Text) node;
                    text.setFont(Font.font(NUMBER_RATIO * CIRCLE_RADIUS - text.getFont().getSize()));
                }
            }
        }
    }

    @FXML
    void onWeightsVisibleClicked(MouseEvent event) {
        weightsVisible = !weightsVisible;
        for (Line line : mapOfWeights.keySet()) {
            Text text = mapOfWeights.get(line);
            if (weightsVisible) {
                text.setFont(Font.font("Monospace", WEIGHT_SIZE));
            } else {
                text.setFont(Font.font(0));
            }

        }
    }

    private void changeColorVertices(Color color) {
        if (selectedPane == null) { // for all nodes
            for (StackPane stackPane : allStacks) {
                circleInStackPane(stackPane).setFill(color);
            }
        } else {
            circleInStackPane(selectedPane).setFill(color);
            circleInStackPane(selectedPane).setRadius(CIRCLE_RADIUS);
            selectedPane = null;
            selectedVertex = null;
        }
    }

    private Circle circleInStackPane(StackPane stackPane) {
        for (Node n : stackPane.getChildren()) {
            if (n instanceof Circle) {
                Circle circle = (Circle) n;
                return circle;
            }
        }
        return null;
    }

    @FXML
    void initialize() {
        assert dataPane != null : "fx:id=\"dataPane\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert drawButton != null : "fx:id=\"drawButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert edgesField != null : "fx:id=\"edgesField\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert fileButton != null : "fx:id=\"fileButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert graphPane != null : "fx:id=\"graphPane\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert newButton != null : "fx:id=\"newButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert numberOfVertices != null : "fx:id=\"numberOfVertices\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert saveAsButton != null : "fx:id=\"saveAsButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'MainMenu.fxml'.";

    }

}

