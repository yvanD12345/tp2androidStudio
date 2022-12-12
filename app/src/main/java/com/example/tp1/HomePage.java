package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.tp1.bds.DBHelper;
import com.example.tp1.data.ComptePOJO;
import com.example.tp1.network.ConnectUtils;
import com.example.tp1.network.MonAPIClient;
import com.example.tp1.network.MonApi;
import com.example.tp1.data.Entreprise;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  HomePage extends AppCompatActivity {
   DBHelper Tp1bd;
   SearchView elementRechercher;
   TextView nomEtudiant ;
   Button bouttonDeco, bouttonStageTrouver;
   ImageView bouttonMap, bouttonAjouterOffre, imagePourStage;
   ToggleButton bouttonActiverFiltreFav;
    RecyclerView recyclerView;
    RecyclerView recyclerViewEtudiant;
    ArrayList<Entreprise> listeEntreprises;
    ArrayList<ComptePOJO> listeEtudiants;


    private  boolean mLocationPermissionGranted= false;

    adapterEntreprise leAdapterEntreprise;
    adapterEtudiant leAdapterEtudiant;

    private MonApi client =   MonAPIClient.getRetrofit().create(MonApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        Tp1bd = new DBHelper(this);

        listeEntreprises = new ArrayList<Entreprise>();
        listeEtudiants = new ArrayList<ComptePOJO>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerViewEtudiant = findViewById(R.id.recyclerview);
        bouttonStageTrouver = findViewById(R.id.bouttonStageTrouver);
        nomEtudiant = findViewById(R.id.nom);
        bouttonActiverFiltreFav = findViewById(R.id.filtreFavori);
        initialiserrecyclerView();
        afficherTouteLesOffre();
        AllerAlaPagePourPostUneOffre();
        lancerUneRecherche();
        afficherLaCarte();
        stageTrouver();
        deconnexionUser();
        afficherLesEntreprise();

    }

    /**
     * affiche les entreprise en favori si le toggle boutton pour favori a étét cliquer s'il a étét décliquer on affiche les entreprise normalement
     */
    private void afficherLesEntreprise(){
     bouttonActiverFiltreFav.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.ETUDIANT){

                 if(bouttonActiverFiltreFav.isChecked()){
                     afficherentrepriseFav();
                 }
                 else if(!bouttonActiverFiltreFav.isChecked()){
                     getEntreprise();
                 }
             }
         }
     });
    }

    /**
     * lorsqu'on appuie sur le boutton trouver stage ca mets trouver stage = true pour l'étudiant
     */
    private void stageTrouver(){
        bouttonStageTrouver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.ETUDIANT) {
                    client.trouverStage(ConnectUtils.authToken).enqueue(new Callback<ComptePOJO>() {
                        @Override
                        public void onResponse(Call<ComptePOJO> call, Response<ComptePOJO> response) {
                            if (response.isSuccessful()) {
                                Log.d("tag", "stage trouver en fait");
                            }
                        }

                        @Override
                        public void onFailure(Call<ComptePOJO> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    /**
     * initialise le recyclerview avec soit la liste entreprise ou etudiants en fonction de son type
     */
    private void initialiserrecyclerView(){
        if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.ETUDIANT){
            leAdapterEntreprise = new adapterEntreprise(this, listeEntreprises);
            recyclerView.setAdapter(leAdapterEntreprise);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else if (ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.PROFESSEUR){
            leAdapterEtudiant = new adapterEtudiant(this,listeEtudiants);
            recyclerView.setAdapter(leAdapterEtudiant);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    /**
     * affiche que l'élement matchant l'input
     */
    private void lancerUneRecherche(){
        elementRechercher = findViewById(R.id.barreDeRecherche);
       elementRechercher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
                effectuerUneRecherche(newText);
               return true;
           }
       });

    }

    /**
     * affiche la carte
     */
    private void afficherLaCarte(){
        bouttonMap = findViewById(R.id.buttonMap);
        bouttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent afficherLaCarte = new Intent(HomePage.this,MapsActivity.class);

                startActivity(afficherLaCarte);
            }
        });
    }

    /**
     *effectue une recherche avec l'input entrer et recherche entreprise ou etudiant en fonction de l,utilisateur
     * @param recherche element recherer
     */
    private void effectuerUneRecherche(String recherche){
        if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.ETUDIANT){
            rechercherEntreprise(recherche);
        }
        else if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.PROFESSEUR){
            rechercherEtudiant(recherche);
        }
    }

    /**
     * fait la vue lorsqu'on rentre dans homepage tout dependamment sai on est un etudiant ou un proff
     */
    private void afficherTouteLesOffre() {
        //cherche dans la bd toute les offre
        if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.ETUDIANT){
          getEntreprise();
        }
        else if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.PROFESSEUR){
            getEtudiants();
        }


    }

    /**
     * recherche si une des entreprise de l'utilisateur a le nom qui match avec la recherche et actualise la vue
     * @param recherche est l'element rechercher
     */
    private void rechercherEntreprise(String recherche){
        ArrayList<Entreprise> listTemp = new ArrayList<Entreprise>();
        for(Entreprise entreprise : listeEntreprises){
            if(entreprise.getNom().toLowerCase().contains(recherche.toLowerCase())){
                listTemp.add(entreprise);
            }
        }
        if(!listTemp.isEmpty()){
            leAdapterEntreprise.changerEffectuer(listTemp);
        }
    }

    /**
     * recherche si un utilisateur a le nom qui match avec la recherche et actualise la vue
     * @param recherche
     */
    private void rechercherEtudiant(String recherche){
     ArrayList<ComptePOJO> listTemp = new ArrayList<ComptePOJO>();
     for(ComptePOJO etudiant : listeEtudiants){
         if(etudiant.getNom().toLowerCase().contains(recherche.toLowerCase())){
             listTemp.add(etudiant);
         }
     }
    }

    /**
     * affiche les entreprise qui sont favori
     */
    private void afficherentrepriseFav(){
        ArrayList<Entreprise> listTemp = new ArrayList<Entreprise>();
        for(Entreprise entreprise : listeEntreprises){
            if(entreprise.getEstFavorite()){
                listTemp.add(entreprise);
            }
        }
        if(!listTemp.isEmpty()){
            leAdapterEntreprise.changerEffectuer(listTemp);
        }
    }

    /**
     * affiche tous les étudiants pour les profs
     */
    private void getEtudiants() {

        Context context = HomePage.this;

        client.getComptesEleves(ConnectUtils.authToken).enqueue(
                new Callback<List<ComptePOJO>>() {
                    @Override
                    public void onResponse(Call<List<ComptePOJO>> call, Response<List<ComptePOJO>> response) {

                        if (response.isSuccessful()) {

                            List<ComptePOJO> comptes = response.body();
                            for (ComptePOJO compte : comptes) {

                                listeEtudiants.add(new ComptePOJO(compte.getNom(),compte.getPrenom(),compte.getStageTrouver()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ComptePOJO>> call, Throwable t) {

                    }
                }
        );

    }

    /**
     * obtient les infos de l'étudiant et prend sa liste entreprise et l'affiche ses entreprises
     */
    private void getEntreprise(){

     client.getEtudiantConnecte(ConnectUtils.authToken).enqueue(new Callback<ComptePOJO>() {
         @Override
         public void onResponse(Call<ComptePOJO> call, Response<ComptePOJO> response) {
             if(response.isSuccessful()){

                 ComptePOJO etudiant = response.body();
                 listeEntreprises = new ArrayList<Entreprise>();
                 for(Entreprise entreprise : etudiant.getEntreprises()){
                     listeEntreprises.add(new Entreprise(entreprise.getId(),entreprise.getNom(),entreprise.getContact(),entreprise.getEmail(),
                             entreprise.getTelephone(),entreprise.getSiteWeb(),entreprise.getAdresse(),entreprise.getVille(),
                             "",entreprise.getCodePostal(),"",entreprise.getEstFavorite()));
                 }
                 leAdapterEntreprise.changerEffectuer(listeEntreprises);
             }
         }

         @Override
         public void onFailure(Call<ComptePOJO> call, Throwable t) {

         }
     });
    }

    /**
     * deconnecte l'utilisateur lorsqu'on appuie sur le boutton
     */
    private void deconnexionUser(){
        bouttonDeco = findViewById(R.id.button);
        bouttonDeco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                deconnexion();
            }
        });
    }

    /**
     * appelle l'api pour la deconnexion
     */
    private void deconnexion(){

        client.deconnecter (ConnectUtils.authToken).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("tag","chu deco");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void AllerAlaPagePourPostUneOffre() {
        bouttonAjouterOffre = findViewById(R.id.buttonAddOffre);
        bouttonAjouterOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,RenplirOffreStage.class));
            }
        });
    }


}