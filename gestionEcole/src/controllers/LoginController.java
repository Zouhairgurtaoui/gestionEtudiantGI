package controllers;


import java.sql.*;

import connection.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField ID;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Label welcomeLabel;

    private Connection con;
    private PreparedStatement st;
    private ResultSet result; 
    private String query;
    private boolean isFound = false;
    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    void logIn(ActionEvent event) {
        String name = "";
        query = "SELECT motPass,nomResponsable FROM responsable WHERE idRespo=?";
        con = DbConnection.getConnectDB();
        try {
            st = con.prepareStatement(query);
            st.setString(1,ID.getText());
            result = st.executeQuery();
            while(result.next()){
                if(result.getString("motPass").equals(password.getText())){
                    name = result.getString("nomResponsable");
                    isFound = true;
                    break;
                }
                
            }
            if(isFound){
                try{
                    stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/gererScene.fxml"));
                    root = loader.load();
                    GererController control = loader.getController();
                    control.setWelcomeText(name);
                    scene = new Scene(root);
                    stage.setTitle("Fillier Management");
                    stage.setScene(scene);
                    stage.show();

                }catch(Exception e){
                    e.printStackTrace();
                }
                
            }else{
                Alert  alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Password or user invalid");
                    alert.showAndWait();
            }
            
        } catch (SQLException e) {
            try {
                st.close();
                con.close();
            } catch (SQLException e1) {
                
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        


    }

}

