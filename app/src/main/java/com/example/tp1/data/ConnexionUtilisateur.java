package com.example.tp1.data;

public class ConnexionUtilisateur {

    private String email;
    private String mot_de_passe;
    private String id_compte;
    public ConnexionUtilisateur() {
    }

    public String getId_compte() {
        return id_compte;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public void setId_compte(String id_compte) {
        this.id_compte = id_compte;
    }

    public String getEmail() {
        return email;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }
}
