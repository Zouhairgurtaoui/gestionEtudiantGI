package controllers.etudiantControllers;

import java.sql.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import classes.Etudiant;
import connection.DbConnection;


public class EtudiantOperations implements Initializable {

    @FXML
    private TableColumn<Etudiant, String> adresse;

    @FXML
    private TableColumn<Etudiant,String > cne;

    @FXML
    private TableColumn<Etudiant, String> naissance;

    @FXML
    private TableColumn<Etudiant, String> nom;

    @FXML
    private TableColumn<Etudiant, String> prenom;


    @FXML
    private TableView<Etudiant> tableEtudiant;

    @FXML
    private TableColumn<Etudiant, String> tele;

    @FXML
    private ImageView home;

    @FXML
    private ImageView search;

    @FXML
    private ImageView ajouter;

    @FXML
    private ImageView supprimer;

    @FXML
    private ImageView modifier;

    @FXML
    private TextField searchField;

    @FXML
    private Label attention;

    Connection connection;
    ObservableList<Etudiant> list = FXCollections.observableArrayList();


    


    public void ajouter(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/etudiantFxmls/ajouterEtudiant.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }


    public void initializeFromDB() throws SQLException {
        list.clear();
        connection = DbConnection.getConnectDB();
        String query = "select * from etudiant";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            list.add(new Etudiant(resultSet.getString("cne"),resultSet.getString("nomEtudiant"),resultSet.getString("prenomEtudiant"),resultSet.getString("dateNaissance"),resultSet.getString("addresse"),resultSet.getString("numTele")));
        }
        connection.close();


    }

    public void load() throws SQLException {
        initializeFromDB();
        tableEtudiant.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cne.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("cne"));
        nom.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("prenom"));
        naissance.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("dateNaissance"));
        adresse.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("adresse"));
        tele.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("tele"));

        try {
            load();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FilteredList<Etudiant> filteredList = new FilteredList<>(list, b->true);
        searchField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                filteredList.setPredicate(new Predicate<Etudiant>() {

                    @Override
                    public boolean test(Etudiant etudiant) {
                        if(newValue == null|| newValue.isEmpty()){
                            return true;
                        }
                        String lowerCase = newValue.toLowerCase();
                        if(etudiant.getCne().toLowerCase().indexOf(lowerCase) != -1){
                            return true;
                        }else if(etudiant.getNom().toLowerCase().indexOf(lowerCase) != -1){
                            return true;
                        }else return false;
                    }

                });
            }

        });
        SortedList<Etudiant> sortedList = new SortedList<Etudiant>(filteredList);
        sortedList.comparatorProperty().bind(tableEtudiant.comparatorProperty());
        tableEtudiant.setItems(sortedList);
    }

    public void supprimerEtudiant(ActionEvent event) throws SQLException {
        try {
            Etudiant etudiant = tableEtudiant.getSelectionModel().getSelectedItem();
            etudiant.supprimerEtudiant();
            load();
        }
        catch (NullPointerException exception){
            attention.setText("Aucun élève n'a été sélectionné");
        }

    }

    public void modifierInfoEtudiant(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/etudiantFxmls/modifier-etudiant.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        try{
            Etudiant etudiant = tableEtudiant.getSelectionModel().getSelectedItem();
            ControllerModifierEtudiant controllerModifierEtudiant = fxmlLoader.getController();
            controllerModifierEtudiant.setTextFields(etudiant.getCne());
        }catch(NullPointerException nullPointerException){
            
            attention.setText("Select an Etudiant");
        }   
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void home(ActionEvent event) {
        try{
           Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
           FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/gererScene.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
