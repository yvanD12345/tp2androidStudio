package com.example.tp1.data;

import java.io.Serializable;

public class CreationCompteData implements Serializable  {

    private String email;
    private String mot_de_passe;
    private String mot_de_passe_confirmation;
    private String nom;
    private String prenom;

    public CreationCompteData(String email, String mot_de_passe,
                              String mot_de_passe_confirmation, String nom, String prenom){
    this.email = email;
    this.mot_de_passe = mot_de_passe;
    this.mot_de_passe_confirmation = mot_de_passe_confirmation;
    this.nom = nom;
    this.prenom = prenom;
    }
}
