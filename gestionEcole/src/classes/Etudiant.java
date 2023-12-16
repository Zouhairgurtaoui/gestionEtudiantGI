package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


import connection.DbConnection;

public class Etudiant {
    private final String cne ;
    private String nom ;
    private String prenom ;
    private String dateNaissance ;
    private String adresse ;
    private String semestre ;
    private String tele ;


    public boolean conatainDigit(String string){
        for (int i=0 ; i<string.length() ; i++ ){
            if (!Character.isDigit(string.charAt(i)))
                return false;
        }
        return true;
    }

    public boolean containCharachter(String tele){
        for (int i=0 ; i<tele.length() ; i++) {
            if (!Character.isLetter(tele.charAt(i)))
                return false;
        }
        return true;
    }
    public Etudiant(String cne, String nom, String prenom, String dateNaissance, String adresse,String tele , String semestre ) {

        if (!containCharachter(nom) || !containCharachter(prenom) || !conatainDigit(tele)){
            throw new IllegalArgumentException();
        }
        else {
            this.cne = cne;
            this.nom = nom;
            this.prenom = prenom;
            this.dateNaissance = dateNaissance;
            this.adresse = adresse;
            this.semestre = semestre;
            this.tele = tele;
        }
    }

    public String getCne() {
        return cne;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance.toString();
    }

    public String getAdresse() {
        return adresse;
    }

    public String getSemestre() {
        return semestre;
    }

    public String getTele() {
        return tele;
    }

    public void ajouterEtudiant() throws SQLException {
        try {
            Connection connection = DbConnection.getConnectDB();
            String query = "insert into etudiant (cne,nomEtudiant,prenomEtudiant,dateNaissance,addresse,numTele,semestre) values (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, this.cne);
            preparedStatement.setString(2, this.nom);
            preparedStatement.setString(3, this.prenom);
            preparedStatement.setString(4, this.dateNaissance);
            preparedStatement.setString(5, this.adresse);
            preparedStatement.setString(6, this.tele);
            preparedStatement.setString(7, this.semestre);
            preparedStatement.executeUpdate();
            connection.close();
        }
        catch (SQLIntegrityConstraintViolationException exception){
            throw new SQLIntegrityConstraintViolationException();
        }
    }

    public void modifierInfoEtudiant() throws SQLException {
        Connection connection = DbConnection.getConnectDB();
        String query = "update etudiant set nomEtudiant =? , prenomEtudiant=? , dateNaissance=? , addresse=? , numTele=? , semestre=? where cne=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,this.nom);
        preparedStatement.setString(2,this.prenom);
        preparedStatement.setString(3,this.dateNaissance);
        preparedStatement.setString(4,this.adresse);
        preparedStatement.setString(5,this.tele);
        preparedStatement.setString(6,this.semestre);
        preparedStatement.setString(7,this.cne);
        preparedStatement.executeUpdate();
        connection.close();
    }

    public void supprimerEtudiant() throws SQLException {
        Connection connection = DbConnection.getConnectDB();
        String query = "delete from etudiant where cne=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,this.cne);
        preparedStatement.executeUpdate();
    }
}
