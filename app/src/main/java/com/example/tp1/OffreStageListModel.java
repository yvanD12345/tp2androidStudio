package com.example.tp1;

public class OffreStageListModel {
    //ceci est le modele pour l'offre de stage
 private String nomCompanie;
 private String email;
 private String contact;
 private String poste;
 private String adresse;
 private String ville;
 private String codePostal;
 private int telephone;
 private String url;
 private int id_company;


 public OffreStageListModel(String nomCompanie, String email, String contact, int telephone, String codePostal, String adresse, String ville, String poste, String url){
     this.nomCompanie = nomCompanie;
     this.email = email;
     this.contact = contact;
     this.codePostal = codePostal;
     this.ville = ville;
     this.adresse = adresse;
     this.poste = poste;
     this.telephone = telephone;
     this.url = url;
 }
    public OffreStageListModel(String nomCompanie, String poste){
        this.nomCompanie = nomCompanie;

        this.poste = poste;

    }

 public int getId_company(){return id_company;}

 public String getNomCompanie() {return nomCompanie;}

 public String getEmail(){return  email;}

 public  String getContact(){return contact;}

    public  String getVille(){return ville;}

    public String getPoste(){return poste;}

    public String getCodePostal(){return codePostal;}

    public int getTelephone(){return telephone;}

    public String getAdresse(){return adresse;}

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public void setNomCompanie(String nomCompanie) {
        this.nomCompanie = nomCompanie;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
