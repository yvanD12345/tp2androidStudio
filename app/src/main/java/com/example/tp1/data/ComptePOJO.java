
package com.example.tp1.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ComptePOJO implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("prenom")
    @Expose
    private String prenom;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("stageTrouve")
    @Expose
    private Boolean stageTrouve;
    @SerializedName("typeCompte")
    @Expose
    private TypeCompte typeCompte;

    public enum TypeCompte {
        ADMINISTRATEUR,
        PROFESSEUR,
        ETUDIANT
    }
    @SerializedName("entreprises")
    @Expose
    private List<Entreprise> entreprises = null;

    private final static long serialVersionUID = -1412352379743458039L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ComptePOJO() {
    }
    public ComptePOJO(String nom, String prenom,Boolean stageTrouve){
        this.nom = nom;
        this.prenom = prenom;
        this.stageTrouve = stageTrouve;
    }


    /**
     *
     * @param entreprises
     * @param typeCompte
     * @param id
     * @param nom
     * @param prenom
     * @param email
     */
    public ComptePOJO(String id, String nom, String prenom, String email, TypeCompte typeCompte, List<Entreprise> entreprises) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeCompte = typeCompte;
        this.entreprises = entreprises;
    }
    public Boolean getStageTrouver() {
        return stageTrouve;
    }

    public void setStageTrouver(Boolean stageTrouver) {
        this.stageTrouve = stageTrouver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    public List<Entreprise> getEntreprises() {
        return entreprises;
    }

    public void setEntreprises(List<Entreprise> entreprises) {
        this.entreprises = entreprises;
    }

    @Override
    public String toString() {
        return "ComptePOJO{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", typeCompte='" + typeCompte + '\'' +
                ", entreprises=" + entreprises +
                '}';
    }


}
