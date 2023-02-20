import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.stage.Stage; 
import javafx.scene.shape.LineTo; 
import javafx.scene.shape.MoveTo; 
import javafx.scene.shape.Path;

import java.io.IOException;

import graph_editor.draw.JavaFxGraphDrawer;
import graph_editor.example.ExampleGraphs;

public class MainMenu extends Application {

  @Override
  public void start(Stage stage) throws IOException{   
    Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));   
    stage.setTitle("Graph Editor");
            
    // Group group = JavaFxGraphDrawer.drawGraph(ExampleGraphs.triangle(), 800, 800);
    Scene scene = new Scene(root, 1200, 800);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}