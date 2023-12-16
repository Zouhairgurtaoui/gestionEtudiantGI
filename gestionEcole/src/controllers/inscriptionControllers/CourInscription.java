package controllers.inscriptionControllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import classes.Cour;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CourInscription implements Initializable {

    @FXML
    private TableColumn<Cour, String> codeCourCol;

    @FXML
    private TableView<Cour> courTable;

    @FXML
    private TableColumn<Cour, String> ensaignatCol;

    @FXML
    private ImageView exit;

    @FXML
    private ImageView fullScreen;

    @FXML
    private ImageView hide;

    @FXML
    private ImageView homeImage;

    @FXML
    private TableColumn<Cour, String> horaireCol;

    @FXML
    private TableColumn<Cour, String> nomCourCol;

    @FXML
    private TableColumn<Cour, String> salleCol;

    @FXML
    private TableColumn<Cour, String> semestreCol;

    ObservableList<Cour> courList = FXCollections.observableArrayList();
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
    static Inscription inscription;

    double x,y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        codeCourCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("codeCour"));
        nomCourCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("nomCour"));
        ensaignatCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("ensaignant"));
        salleCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("salle"));
        horaireCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("horaire"));
        semestreCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("semestre"));
        refreshTable();
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


    
    void refreshTable(){
        try {
            connection = DbConnection.getConnectDB();
            courList.clear();
            
            query = "SELECT cour.* FROM cour,inscription WHERE inscription.codeCour=cour.codeCour AND cour.semestre=inscription.semestreInscription AND inscription.cne=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, inscription.getCne());
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                courList.add(new Cour(resultSet.getString("codeCour"),resultSet.getString("nomCour") ,
                resultSet.getDate("horaire"), resultSet.getString("ensaignantRespo"), resultSet.getString("salle"),resultSet.getString("semestre")));
            }

            courTable.setItems(courList);
            
            
        } catch (SQLException ex) {
            System.out.println(ex.getSQLState());
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

    /*public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }*/
    

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

    
}
