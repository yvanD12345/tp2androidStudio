package com.example.tp1.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Entreprise implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("siteWeb")
    @Expose
    private String siteWeb;
    @SerializedName("adresse")
    @Expose
    private String adresse;
    @SerializedName("ville")
    @Expose
    private String ville;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("codePostal")
    @Expose
    private String codePostal;
    @SerializedName("dateContact")
    @Expose
    private Object dateContact;
    @SerializedName("estFavorite")
    @Expose
    private Boolean estFavorite;

    private final static long serialVersionUID = 2797931101594408890L;

    /**
     * No args constructor for use in serialization
     *
     */


    /**
     *
     * @param dateContact
     * @param ville
     * @param province
     * @param estFavorite
     * @param siteWeb
     * @param contact
     * @param adresse
     * @param telephone
     * @param id
     * @param codePostal
     * @param nom
     * @param email
     */
    public Entreprise(String id, String nom, String contact, String email, String telephone, String siteWeb, String adresse, String ville, String province, String codePostal, Object dateContact, Boolean estFavorite) {
        super();
        this.id = id;
        this.nom = nom;
        this.contact = contact;
        this.email = email;
        this.telephone = telephone;
        this.siteWeb = siteWeb;
        this.adresse = adresse;
        this.ville = ville;
        this.province = province;
        this.codePostal = codePostal;
        this.dateContact = dateContact;
        this.estFavorite = estFavorite;

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public Object getDateContact() {
        return dateContact;
    }

    public void setDateContact(Object dateContact) {
        this.dateContact = dateContact;
    }

    public Boolean getEstFavorite() {
        return estFavorite;
    }

    public void setEstFavorite(Boolean estFavorite) {
        this.estFavorite = estFavorite;
    }

    @Override
    public String toString() {
        return "Entreprise{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", siteWeb='" + siteWeb + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", province='" + province + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", dateContact=" + dateContact +
                ", estFavorite=" + estFavorite +
                '}';
    }
}

