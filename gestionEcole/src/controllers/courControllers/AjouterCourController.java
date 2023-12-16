package controllers.courControllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


import connection.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.*;

public class AjouterCourController implements Initializable{

    @FXML
    private TextField codeCour;

    @FXML
    private Button confirmationBtn;

    @FXML
    private TextField ensaignant;

    @FXML
    private DatePicker horaire;

    @FXML
    private TextField nomCour;

    @FXML
    private TextField salle;

    @FXML
    private ComboBox<String> semestre;

    @FXML
    private ImageView exit;

    @FXML
    private ImageView fullScreen;

    @FXML
    private ImageView hide;

    @FXML
    private ImageView homeImage;

    private Stage stage;
    FXMLLoader loader;
    Parent root;
    Scene scene;
    private boolean isUpdated;
    boolean isFullScreen;
    private String query;
    private Connection connection;
    private PreparedStatement st;
    private CourOperations controller;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        semestre.getItems().addAll("S1","S2","S3","S4");

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


    @FXML
    void ajouter(ActionEvent event) {

        connection = DbConnection.getConnectDB();
        
        if(isUpdated){
            
            try{
            query = "UPDATE cour SET nomCour=? ,ensaignantRespo=? ,salle=? ,horaire=? ,semestre=? WHERE codeCour=?";
            st = connection.prepareStatement(query);
            if(nomCour.getText().isEmpty() || ensaignant.getText().isEmpty() || salle.getText().isEmpty() || horaire.getValue().toString().isEmpty() || semestre.getValue().isEmpty()){
                System.out.println("3amrhom");
            }else{
            st.setString(1, nomCour.getText());
            st.setString(2, ensaignant.getText());
            st.setString(3, salle.getText());
            st.setString(4, horaire.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            st.setString(5, semestre.getSelectionModel().getSelectedItem());
            st.setString(6, codeCour.getText());

            int res = st.executeUpdate();
            System.out.println("rows affected" + res);

            //onCourAdd();
            }
            }catch(NullPointerException nullPointerException){

            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Fields are empty");
            alert.show();
            }
            catch(SQLException sqlException){

                System.out.println(sqlException);
            }

        }else{
        query = "INSERT INTO cour(codeCour,nomCour,horaire,ensaignantRespo,salle,semestre) VALUES (?,?,?,?,?,?)";
        
        try {
            if(nomCour.getText().isEmpty() || ensaignant.getText().isEmpty() || salle.getText().isEmpty() || horaire.getValue().toString().isEmpty() || semestre.getValue().isEmpty()){
                System.out.println("3amrhom");
            }else{

            st = connection.prepareStatement(query);
            st.setString(1, codeCour.getText());
            st.setString(2, nomCour.getText());
            st.setString(3, horaire.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            st.setString(4, ensaignant.getText());
            st.setString(5, salle.getText());
            st.setString(6, semestre.getSelectionModel().getSelectedItem());
            int res = st.executeUpdate();
            System.out.println("rows affected" + res);

            //onCourAdd();
            }
        }catch(NullPointerException nullPointerException){

            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Fields are empty");
            alert.show();

        }
         catch (Exception e) {
            
            System.out.println(e);
        }
    }
    }

    @FXML
    void annuler(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        loader = new  FXMLLoader();
        
        try {
            loader.setLocation(getClass().getResource("/fxmls/courFxmls/courOperations.fxml"));
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        
    }

    public void setController(CourOperations controller) {
        this.controller = controller;
    }

    private void onCourAdd(){
        if(controller != null){
            controller.initialize(null, null);
        }else{
            System.out.println("mabghatch tkhdm");
        }
    }
    public void setUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }
    public void setFieldText(String codeCour,String nomCour,String ensaignant,LocalDate date,String salle){

        this.codeCour.setText(codeCour);
        this.nomCour.setText(nomCour);
        this.ensaignant.setText(ensaignant);
        this.horaire.setValue(date);
        this.salle.setText(salle);
        
    }
    public void setConfirmationBtn(String text) {
        this.confirmationBtn.setText(text);
    }
    public void setCodeCourEditability(boolean editable) {
        this.codeCour.setEditable(editable);
    }
    @FXML
    void home(ActionEvent event){

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
    void retourner(ActionEvent event) {
        try{
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/courFxmls/courOperations.fxml"));
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    

}
