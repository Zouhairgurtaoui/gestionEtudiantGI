package controllers.etudiantControllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import classes.Etudiant;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

import java.sql.*;



public class ControllerAjouterEtudiant{

    @FXML
    private Label attention;

    @FXML
    private TextField adresse;

    @FXML
    private TextField cne;

    @FXML
    private DatePicker naissance;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;


    @FXML
    private TextField tele;



    public void retourner(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/etudiantFxmls/espace-etudiant.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void home(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/gererScene.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("controllers/tableview.css");
        stage.setScene(scene);
        stage.show();
    }

    public boolean fieldAreEmpty(){
        if (cne.getText().trim().isEmpty() || nom.getText().trim().isEmpty() || prenom.getText().trim().isEmpty() || naissance.getValue().toString().isEmpty() || adresse.getText().trim().isEmpty() || tele.getText().trim().isEmpty() )
            return true ;
        else
            return false ;
    }

    public void ajouterEtudiant() throws SQLException {
        attention.setText(null);
        if (!fieldAreEmpty()){
            try {
                try {
                    Etudiant etudiant = new Etudiant(cne.getText().trim(), nom.getText().trim(), prenom.getText().trim(), naissance.getValue().toString(), adresse.getText().trim(), tele.getText().trim());
                    etudiant.ajouterEtudiant();
                    cne.clear();
                    nom.clear();
                    prenom.clear();
                    adresse.clear();
                    tele.clear();
                    naissance.setValue(null);
                   
                }catch (IllegalArgumentException exception){
                    attention.setText("Verifier les informations");
                }
            }
            catch (SQLIntegrityConstraintViolationException exception) {
                attention.setText("Etudiant deja existe");
                cne.clear();
                nom.clear();
                prenom.clear();
                adresse.clear();
                tele.clear();
                naissance.setValue(null);
            }
        }
        else {
            attention.setText("remplir tout");
        }


    }



   
}
