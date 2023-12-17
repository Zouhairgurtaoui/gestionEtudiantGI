package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class MainClass extends Application{

    double x,y;

    public static void main(String ... args){
        launch(args);
    }

    @Override
    public void start(Stage stage){
        
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxmls/gererScene.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("controllers/tableview.css");
            //stage.initStyle(StageStyle.UNDECORATED);
            root.setOnMousePressed(e ->{
                x = e.getSceneX();
                y = e.getSceneY();
            });
            root.setOnMouseDragged(e ->{
                stage.setX(e.getScreenX() - x);
                stage.setY(e.getScreenY() - y);
            });
            
            stage.setTitle("LOGIN");
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
