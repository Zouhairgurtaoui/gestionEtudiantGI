package controllers.etudiantControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import classes.Etudiant;

import connection.DbConnection;

public class ControllerModifierEtudiant implements Initializable {

    @FXML
    private Label fxLabel;

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

    @FXML
    private Button change;

    @FXML
    private Button valide;

    @FXML
    private Button modifier;

    @FXML
    private Label attention;


    public void desactiverFields(){
        nom.setDisable(true);
        prenom.setDisable(true);
        adresse.setDisable(true);
        naissance.setDisable(true);
        tele.setDisable(true);
       
        modifier.setDisable(true);
        change.setDisable(true);
        cne.setDisable(false);
        valide.setDisable(false);
    }
    public void activerFields(){
        nom.setDisable(false);
        prenom.setDisable(false);
        adresse.setDisable(false);
        naissance.setDisable(false);
        tele.setDisable(false);
       
        modifier.setDisable(false);
        change.setDisable(false);
        cne.setDisable(true);
        valide.setDisable(true);

    }

    public boolean fieldAreEmpty(){
        if (cne.getText().trim().isEmpty() || nom.getText().trim().isEmpty() || prenom.getText().trim().isEmpty() || naissance.getValue().toString().isEmpty() || adresse.getText().trim().isEmpty() || tele.getText().trim().isEmpty() )
            return true ;
        else
            return false ;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        desactiverFields();

    }

    public void annuler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/etudiantFxmls/espace-etudiant.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("controllers/tableview.css");
        stage.setScene(scene);
        stage.show();
    }


    public void valider(ActionEvent event) throws SQLException {
        fxLabel.setText(null);
        activerFields();
        Connection connection = DbConnection.getConnectDB();
        String query = "Select * from etudiant where cne=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,cne.getText());
        ResultSet resultSet = preparedStatement.executeQuery();

        if(!resultSet.next()){
            fxLabel.setText("Etudiant non trouvee");
            desactiverFields();
        }
        else {
            nom.setText(resultSet.getString("nomEtudiant"));
            prenom.setText(resultSet.getString("prenomEtudiant"));
            naissance.setValue(LocalDate.parse(resultSet.getString("dateNaissance")));
            adresse.setText(resultSet.getString("addresse"));
            tele.setText(resultSet.getString("numTele"));
            
        }
    }

    public void changeCneButton(ActionEvent event){
        desactiverFields();
    }

    public void modifierInfoEtudiant(ActionEvent event) throws SQLException {
        if(!fieldAreEmpty()){
            try {
                Etudiant etudiant = new Etudiant(cne.getText().trim(), nom.getText().trim(), prenom.getText().trim(), naissance.getValue().toString(), adresse.getText().trim(), tele.getText().trim());
                etudiant.modifierInfoEtudiant();
                cne.clear();
                nom.clear();
                prenom.clear();
                adresse.clear();
                tele.clear();
                naissance.setValue(null);
                attention.setText(null);
            }catch (IllegalArgumentException exception){
                attention.setText("Verifier les informations");
            }
        }
        else
            attention.setText("remplis tous");
    }
    public void setTextFields(String cne){
        this.cne.setText(cne);
    }

    @FXML
    void retourner(ActionEvent event){
        try {
            annuler(event);
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }



}
