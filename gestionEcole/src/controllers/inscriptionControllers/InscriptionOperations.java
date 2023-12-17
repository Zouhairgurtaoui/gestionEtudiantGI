package controllers.inscriptionControllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


import classes.Inscription;
import connection.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InscriptionOperations implements Initializable{

    @FXML
    private TableColumn<Inscription, String> cneCol;

    @FXML
    private TableColumn<Inscription, String> dateInscriptionCol;


    @FXML
    private ImageView homeImage;


    @FXML
    private Button home;

    @FXML
    private TableColumn<Inscription, Integer> idInscriptionCol;

    @FXML
    private TableColumn<Inscription, String> nomCol;

    @FXML
    private TableColumn<Inscription, String> prenomCol;

    @FXML
    private TableView<Inscription> inscriptionTable;

    @FXML
    private ComboBox<String> semestreBox;

    private ObservableList<Inscription> dataList = FXCollections.observableArrayList();
    Stage stage;
    FXMLLoader loader;
    Parent root;
    Scene scene;
    boolean isFullScreen;
    Connection connection;
    PreparedStatement preparedStatement;
    Statement statement;
    ResultSet resultSet;
    String query;
    Inscription inscription;

    double x,y;


    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idInscriptionCol.setCellValueFactory(new PropertyValueFactory<Inscription,Integer>("idInscription"));
        cneCol.setCellValueFactory(new PropertyValueFactory<Inscription,String>("cne"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Inscription,String>("nomEtudiant"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Inscription,String>("prenomEtudiant"));
        dateInscriptionCol.setCellValueFactory(new PropertyValueFactory<Inscription,String>("dateInscription"));
        loadData();

    }

    
    private void displayAll(){
        try {
            connection = DbConnection.getConnectDB();
            dataList.clear();
            query="SELECT DISTINCT INS.idinscription,INS.dateInscription,INS.cne,ET.nomEtudiant,ET.prenomEtudiant FROM inscription As INS,etudiant AS ET  WHERE INS.cne = ET.cne GROUP BY INS.cne";
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                dataList.add(new Inscription(resultSet.getInt("INS.idinscription"), resultSet.getString("INS.cne"),
                resultSet.getString("ET.nomEtudiant"),resultSet.getString("ET.prenomEtudiant"),resultSet.getDate("INS.dateInscription")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private void refreshTable(String semestre){
        try {
            connection = DbConnection.getConnectDB();
            dataList.clear();
            query="SELECT DISTINCT ET.cne,INS.idinscription,INS.dateInscription,ET.nomEtudiant,ET.prenomEtudiant FROM inscription As INS,etudiant AS ET WHERE INS.cne = ET.cne AND INS.semestreInscription=? GROUP BY INS.cne";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, semestre);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dataList.add(new Inscription(resultSet.getInt("INS.idinscription"), resultSet.getString("ET.cne"),
                resultSet.getString("ET.nomEtudiant"),resultSet.getString("ET.prenomEtudiant"),resultSet.getDate("INS.dateInscription")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    void loadData(){
        semestreBox.getItems().addAll("Toute","S1","S2","S3","S4");
        displayAll();
        inscriptionTable.setItems(dataList);
    }
    
    @FXML
    void changeSemestre(ActionEvent event) {
        String selected=semestreBox.getValue();
        if(selected.equalsIgnoreCase("Toute")){
            displayAll();
        }else{
        refreshTable(selected);
        inscriptionTable.setItems(dataList);
        }
    }

    @FXML
    void inscrirEtudiant(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/inscriptionFxmls/inscrirEtudiant.fxml"));
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
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @FXML
    void home(ActionEvent event) {
        try{
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/gererScene.fxml"));
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    void desinscrir(){
        connection = DbConnection.getConnectDB();
        query="DELETE FROM inscription WHERE idinscription =?";
        inscription= inscriptionTable.getSelectionModel().getSelectedItem();
        try {
           preparedStatement = connection.prepareStatement(query);
           preparedStatement.setInt(1,inscription.getIdInscription());
           int res = preparedStatement.executeUpdate(); 
           System.out.println("rows Affected "+res);
           displayAll();
        }catch(NullPointerException nullPointerException){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("ERROR");
            alert.setContentText("You need to Select an etudiant to disinscrir");
            alert.show();
            System.out.println(nullPointerException);
        }
         catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally{
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @FXML
    void displayCours(ActionEvent event){
         stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {

            inscription = inscriptionTable.getSelectionModel().getSelectedItem();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/inscriptionFxmls/courInscription.fxml"));
            CourInscription.inscription=inscription;
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
            stage.setScene(scene);
            stage.show();
        }catch(NullPointerException nullPointerException){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("ERROR");
            alert.setContentText("You need to Select an etudiant to display his cours");
            alert.show();
            System.out.println(nullPointerException);
        }
         catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}

