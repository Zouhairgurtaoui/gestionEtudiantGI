package controllers.inscriptionControllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import classes.Cour;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class InscrirEtudiant implements Initializable{

    @FXML
    private ComboBox<String> cneBox;

    @FXML
    private TableColumn<Cour,String> codeCourCol;

    @FXML
    private TableView<Cour> courList;

    @FXML
    private TableColumn<Cour, String> nomCourCol;

    @FXML
    private ImageView exit;

    @FXML
    private ImageView fullScreen;

    @FXML
    private ImageView hide;

    @FXML
    private ImageView homeImage;

     @FXML
    private DatePicker dateInscription;

    @FXML
    private ComboBox<String> semestre;


    Stage stage;
    FXMLLoader loader;
    Parent root;
    Scene scene;
    Connection connection;
    PreparedStatement preparedStatement;
    Statement statement;
    String query;
    ResultSet resultSet;
    boolean isFullScreen;
    ObservableList<Cour> dataList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCne();
        semestre.getItems().addAll("S1","S2","S3","S4");
        codeCourCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("codeCour"));
        nomCourCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("nomCour"));
        courList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        homeImage.setOnMouseClicked(e ->{
            ActionEvent event = new ActionEvent(homeImage, MouseEvent.NULL_SOURCE_TARGET);
            home(event);
        });
        
        exit.setOnMouseClicked(e -> {
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.close();
        });

        
        fullScreen.setOnMouseClicked(e ->{
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            
            
            if(!isFullScreen){
                stage.setFullScreen(true);
                fullScreen.setImage(new Image(getClass().getResourceAsStream("/icons/exit-fullscreen.png")));
                isFullScreen=true;
            }else{
                stage.setFullScreen(false);
                fullScreen.setImage(new Image(getClass().getResourceAsStream("/icons/full-screen-icon-28.png")));
                isFullScreen=false;
            }
        });
        hide.setOnMouseClicked(e ->{
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setY(Stage.getWindows().size() + 100);
        });

    }

    private void initializeCne(){
        query="SELECT cne FROM etudiant";
        dataList.clear();
        try {
            connection = DbConnection.getConnectDB();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                cneBox.getItems().add(resultSet.getString("cne"));
            }
      
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally{
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private void refreshList(String semestre){
        connection = DbConnection.getConnectDB();
        dataList.clear();
        try {

            query="SELECT codeCour,nomCour FROM cour WHERE semestre=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, semestre);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dataList.addAll(new Cour(resultSet.getString("codeCour"),resultSet.getString("nomCour"),null,null,null,null));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @FXML
    void changeSemestre(ActionEvent event) {
        String selected = semestre.getValue();
        refreshList(selected);
        courList.setItems(dataList);
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
    void retourner(ActionEvent event) {
        try{
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/inscriptionFxmls/inscriptionOperations.fxml"));
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void inscrir(ActionEvent event){
        try{
        ObservableList<Cour>selectedCour = courList.getSelectionModel().getSelectedItems();
        String cne = cneBox.getValue();
        String selectedSemestre = semestre.getValue();
        String dateInsc = dateInscription.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        selectedCour.forEach(c->{
            insert(cne, c.getCodeCour(), selectedSemestre, dateInsc);
        });
    }catch(NullPointerException nullPointerException){
        Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Fields are empty");
            alert.show();
    }
       
    }
    private void insert(String cne,String codeCour,String semestre,String dateInsc){
        connection = DbConnection.getConnectDB();
        query="INSERT INTO inscription(cne,codeCour,semestreInscription,dateInscription) VALUES (?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cne);
            preparedStatement.setString(2, codeCour);
            preparedStatement.setString(3, semestre);
            preparedStatement.setString(4, dateInsc);
            int res = preparedStatement.executeUpdate();
            System.out.println("rows Affected "+res);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @FXML
    void annuler(ActionEvent event){
        retourner(event);
    }

    

}
