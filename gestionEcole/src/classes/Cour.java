package classes;

import java.sql.Date;

import javafx.beans.property.SimpleStringProperty;

public class Cour {
   private SimpleStringProperty codeCour = new SimpleStringProperty();
   private SimpleStringProperty nomCour = new SimpleStringProperty();
   private SimpleStringProperty ensaignant = new SimpleStringProperty();
   private SimpleStringProperty salle = new SimpleStringProperty();
   private SimpleStringProperty semestre = new SimpleStringProperty();

   public Cour(String codeCour,String nomCour,String respo,String salle,String semestre){

    this.codeCour.set(codeCour);
    this.nomCour.set(nomCour);
    this.ensaignant.set(respo);
    this.salle.set(salle);
    this.semestre.set(semestre);
    
   }
   public String getCodeCour() {
       return codeCour.get();
   }
   public String getEnsaignant() {
       return ensaignant.get();
   }
   
   public String getNomCour() {
       return nomCour.get();
   }
   public String getSalle() {
       return salle.get();
   }
   public String getSemestre() {
       return semestre.get();
   }
   public void setSemestre(String semestre) {
       this.semestre.set(semestre);
   }
   public void setCodeCour(String codeCour) {
       this.codeCour.set(codeCour);;
   }
   public void setEnsaignant(String ensaignant) {
       this.ensaignant.set(ensaignant);
   }
   
   public void setNomCour(String nomCour) {
       this.nomCour.set(nomCour);
   }
   public void setSalle(String salle) {
       this.salle.set(salle);
   }
}
