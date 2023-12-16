package classes;

import java.sql.Date;

import javafx.beans.property.SimpleStringProperty;

public class Inscription {
    
    private Integer idInscription;
    private SimpleStringProperty cne = new SimpleStringProperty();
    private SimpleStringProperty nomEtudiant = new SimpleStringProperty();
    private SimpleStringProperty prenomEtudiant = new SimpleStringProperty();
    private Date dateInscription;

    public Inscription(int idInscription,String cne,String nomEtudiant,String prenomEtudiant,Date dateInscription){
        this.cne.set(cne);
        this.idInscription = idInscription;
        this.nomEtudiant.set(nomEtudiant);
        this.prenomEtudiant.set(prenomEtudiant);
        this.dateInscription = dateInscription;
    } 



    public String getCne() {
        return cne.get();
    }
    public Date getDateInscription() {
        return dateInscription;
    }
    public Integer getIdInscription() {
        return idInscription;
    }
    public String getNomEtudiant() {
        return nomEtudiant.get();
    }
    public String getPrenomEtudiant() {
        return prenomEtudiant.get();
    }

    public void setCne(String cne) {
        this.cne.set(cne);
    }
    
}
