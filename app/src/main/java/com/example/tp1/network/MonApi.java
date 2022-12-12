package com.example.tp1.network;

import com.example.tp1.data.ComptePOJO;
import com.example.tp1.data.CompteResult;
import com.example.tp1.data.CreationCompteData;
import com.example.tp1.data.Entreprise;
import com.example.tp1.data.LoginData;

import java.util.HashMap;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MonApi {

    @Headers({
            "Content-Type:application/json"
    })

    @POST("/auth/deconnexion")
    Call<ResponseBody> deconnecter(@Header("Authorization") String token);

    @POST("/auth/connexion")
    Call<CompteResult> connecter(@Body LoginData loginData);

    @POST("/inscription")
    Call<ComptePOJO> creerCompte(@Body CreationCompteData loginData);


    //creer entreprise
    //pk un creer entreprise si on a deja creer compte
    @POST("/entreprise")
    Call<Entreprise> creerEntreprise(@Header("Authorization") String token, @Body Entreprise entreprise);


    @POST("/auth/testerconnexion")
    Call<ResponseBody> testerConnexion(@Header("Authorization") String token, @Body Object userId);


    //L'API permet de récupérer les entreprises d'un étudiant:
    //commment on sait c quelle etudiant si on donne pas son uid
    @GET("/entreprise")
    Call<List<Entreprise>> lireEntreprises(@Header("Authorization") String token);


    //recuperer etudiant connecter
    @GET("/compte/getetudiant")
    Call<ComptePOJO> getEtudiantConnecte(@Header("Authorization") String token);


    // recuperer les comptes etudiants
    @GET("/compte/getcomptesetudiantsactifs")
    Call<List<ComptePOJO>> getComptesEleves(@Header("Authorization") String token);


    //modifier stage trouver
    @PATCH("/compte/stagetrouve")
    public Call<ComptePOJO> trouverStage(@Header("Authorization") String token);

    //supprimer un stage
    @DELETE("/stage/{idStage}")
    Call<ResponseBody> supprStage(@Header("Authorization") String token, @Path("idStage") String idStage);


    //  Supprimer une entreprise
    @DELETE("/entreprise/{idEntreprise}")
    Call<Entreprise> supprEntreprise(@Header("Authorization") String token, @Path("idEntreprise") String idEntreprise);


   // modifier une entreprise
    @PATCH("/entreprise/{idEntreprise}")
    Call<Entreprise> modifierEntreprise(@Header("Authorization") String token, @Path("idEntreprise") String idEntreprise, @Body Entreprise entreprise);
}
