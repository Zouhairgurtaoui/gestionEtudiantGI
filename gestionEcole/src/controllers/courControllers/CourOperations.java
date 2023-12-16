package controllers.courControllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;



import classes.Cour;

import connection.DbConnection;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class CourOperations implements Initializable{

    
    @FXML
    private TableView<Cour> courTable;

    @FXML
    private  TableColumn<Cour, String> codeCourCol;

    @FXML
    private  TableColumn<Cour, String> ensaignatCol;

    @FXML
    private ImageView search;

    @FXML
    private ImageView exit;
    @FXML
    private ImageView modiferImage;
    @FXML
    private ImageView homeImage;
    @FXML
    private ImageView supprimerImage;
    @FXML
    private ImageView addImage;

    @FXML
    private  TableColumn<Cour, String> horaireCol;

    @FXML
    private AnchorPane navigationPane;

    @FXML
    private  TableColumn<Cour, String> nomCourCol;

    @FXML
    private  TableColumn<Cour, String> salleCol;

    @FXML
    private TableColumn<Cour, String> semestreCol;

    @FXML
    private ImageView fullScreen;

    @FXML
    private ImageView hide;

    @FXML
    private TextField searchField;
    double x;
    double y;
    Stage stage;
    Parent root;
    FXMLLoader loader;
    Scene scene;
    private boolean isFullScreen;
    String query;
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Cour cour;
    ObservableList<Cour>  courList = FXCollections.observableArrayList();
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        codeCourCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("codeCour"));
        nomCourCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("nomCour"));
        ensaignatCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("ensaignant"));
        salleCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("salle"));
        horaireCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("horaire"));
        semestreCol.setCellValueFactory(new PropertyValueFactory<Cour,String>("semestre"));
        loadData();



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

        FilteredList<Cour> filterData = new FilteredList<>(courList, b -> true);
        searchField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue,String newValue){

                filterData.setPredicate(new Predicate<Cour>() {
                @Override
                public boolean test(Cour cour) {
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(cour.getCodeCour().toLowerCase().indexOf(lowerCaseFilter) != -1){
                        return true;
                    }else if(cour.getNomCour().toLowerCase().indexOf(lowerCaseFilter) != -1){
                        return true;
                    }else return false;
                    
                }
            });
            }
            });
        
            
        SortedList<Cour> sortData = new SortedList<>(filterData);
        sortData.comparatorProperty().bind(courTable.comparatorProperty());
        courTable.setItems(sortData);

        
            

    }

    
    void refreshTable(){
        try {
            connection = DbConnection.getConnectDB();
            courList.clear();
            
            query = "SELECT * FROM `cour`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                courList.add(new Cour(resultSet.getString("codeCour"),resultSet.getString("nomCour") ,
                resultSet.getDate("horaire"), resultSet.getString("ensaignantRespo"), resultSet.getString("salle"),resultSet.getString("semestre")));
            }
            
            
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
    public void loadData(){
        
        refreshTable();
        courTable.setItems(courList);
        
    }
    @FXML
    void ajouterCour(ActionEvent event) {
        try {
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/courFxmls/ajouterCour.fxml"));
            root = loader.load();

            AjouterCourController controller = loader.getController();
            controller.setController(this);
            
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
            System.out.println(e);
        }
    }

    @FXML
    void home(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/gererScene.fxml"));
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            
            System.out.println(e);
        }
    }

    @FXML
    void modifierCour(ActionEvent event) {
        cour = courTable.getSelectionModel().getSelectedItem();
        try {
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/courFxmls/ajouterCour.fxml"));
            root = loader.load();

            AjouterCourController controller = loader.getController();
            controller.setController(this);
            controller.setFieldText(cour.getCodeCour(), cour.getNomCour(), cour.getEnsaignant(), cour.getHoraire().toLocalDate(), cour.getSalle());
            controller.setUpdated(true);
            controller.setConfirmationBtn("Modifier");
            controller.setCodeCourEditability(false);

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

        } catch(NullPointerException nullPointerException){

            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("ERROR");
            alert.setContentText("You need to Select a cour to modifier");
            alert.show();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    void supprimerCour(ActionEvent event) {

        try{
        cour = courTable.getSelectionModel().getSelectedItem();
        query = "DELETE FROM cour WHERE codeCour =?";
        connection = DbConnection.getConnectDB();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, cour.getCodeCour());
        int res = preparedStatement.executeUpdate();
        System.out.println("rows affected "+res);
        loadData();
        
        }catch(NullPointerException nullPointerException){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("WARNING");
            alert.setContentText("You need to Select a cour to delete");
            alert.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
}