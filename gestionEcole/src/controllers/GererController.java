package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GererController {

    @FXML
    private Button coursEtudiant;

    @FXML
    private Button etudiantsButton;

    @FXML
    private Button inscriptionButton;

    @FXML
    private Text welcomeLabel;
   

    private FXMLLoader loader;
    private Stage stage;
    private Parent root;
    private Scene scene;
    double x,y;
    
    @FXML
    void gererCours(ActionEvent event) {
        try {
                    stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                   
                
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxmls/courFxmls/courOperations.fxml"));
                    
                    root = loader.load();
                    scene = new Scene(root);
                    scene.getStylesheets().add("controllers/tableview.css");
                    root.setOnMousePressed(e ->{
                        x = e.getSceneX();
                        y = e.getSceneY();
                    });
                    root.setOnMouseDragged(e ->{
                        stage.setX(e.getScreenX() - x);
                        stage.setY(e.getScreenY() - y);
                    });
                  
                   
                    //scene.getStylesheets().add(getClass().getResource("/controllers/tableview.css").toExternalForm());
                    stage.setTitle("Fillier Management");
                    stage.setScene(scene);
                    stage.show();
                    } catch (IOException e) {
                        
                        System.out.println("gere cour "+e);
                        e.printStackTrace();
                    }
    }

    @FXML
    void gererEtudiants(ActionEvent event) {
        
                    try {
                    stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxmls/etudiantFxmls/espace-etudiant.fxml"));

                    root = loader.load();
                   
                    
                    root.setOnMousePressed(e ->{
                        x = e.getSceneX();
                        y = e.getSceneY();
                    });
                    root.setOnMouseDragged(e ->{
                        stage.setX(e.getScreenX() - x);
                        stage.setY(e.getScreenY() - y);
                    });
                   
                   
                    scene = new Scene(root);
                    scene.getStylesheets().add("controllers/tableview.css");
                    //scene.getStylesheets().add(getClass().getResource("/controllers/tableview.css").toExternalForm());
                    stage.setTitle("Fillier Management");
                    stage.setScene(scene);
                    stage.show();
                    } catch (IOException e) {
                        
                        e.printStackTrace();
                        System.out.println("gere etudiant "+e);
                    }
                    
                    
    }

    @FXML
    void gererInscription(ActionEvent event) {

        try {
                    stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                   
                
                    loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxmls/inscriptionFxmls/inscriptionOperations.fxml"));
                    
                    root = loader.load();
                    scene = new Scene(root);
                    scene.getStylesheets().add("controllers/tableview.css");
                    root.setOnMousePressed(e ->{
                        x = e.getSceneX();
                        y = e.getSceneY();
                    });
                    root.setOnMouseDragged(e ->{
                        stage.setX(e.getScreenX() - x);
                        stage.setY(e.getScreenY() - y);
                    });
                    root.autosize();
                  
                   
                    //scene.getStylesheets().add(getClass().getResource("/controllers/tableview.css").toExternalForm());
                    stage.setTitle("Fillier Management");
                    stage.setScene(scene);
                    stage.show();
                    } catch (IOException e) {
                        
                        System.out.println("gere cour "+e);
                        e.printStackTrace();
                    }

    }

    public void setWelcomeText(String user){
        welcomeLabel.setText("Bonjour Mensieur "+user);
    }

}