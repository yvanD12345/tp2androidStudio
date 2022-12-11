package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

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
   Button bouttonDeco;
   ImageView bouttonMap, bouttonAjouterOffre;
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
        initialiserrecyclerView();
        afficherTouteLesOffre();
        AllerAlaPagePourPostUneOffre();
        lancerUneRecherche();
        afficherLaCarte();
        deconnexionUser();

    }
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
    //permet rechercher une offre dans la barre mais elle n'est pas encore fonctionnel
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
    //lance le google map activity en cliquant sur la carte
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

    private void effectuerUneRecherche(String recherche){
        if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.ETUDIANT){
            rechercherEntreprise(recherche);
        }
        else if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.PROFESSEUR){
            rechercherEtudiant(recherche);
        }
    }

    private void afficherTouteLesOffre() {
        //cherche dans la bd toute les offre
        if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.ETUDIANT){
          getEntreprise();
        }
        else if(ConnectUtils.authTypeUtilisateur == ComptePOJO.TypeCompte.PROFESSEUR){
            getEtudiants();
        }


    }

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
    private void rechercherEtudiant(String recherche){
     ArrayList<ComptePOJO> listTemp = new ArrayList<ComptePOJO>();
     for(ComptePOJO etudiant : listeEtudiants){
         if(etudiant.getNom().toLowerCase().contains(recherche.toLowerCase())){
             listTemp.add(etudiant);
         }
     }
    }
    private void getEtudiants() {

        client.getComptesEleves(ConnectUtils.authToken).enqueue(
                new Callback<List<ComptePOJO>>() {
                    @Override
                    public void onResponse(Call<List<ComptePOJO>> call, Response<List<ComptePOJO>> response) {
                        Log.d("tag","getstudent worked");
                        if (response.isSuccessful()) {
                            String affichage = "" + response.code();
                            List<ComptePOJO> comptes = response.body();
                            for (ComptePOJO compte : comptes) {
                                Log.d("tag","testons"+compte.getEmail()+" "+compte.getNom());
                                listeEtudiants.add(new ComptePOJO(compte.getNom(),compte.getPrenom())) ;
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<List<ComptePOJO>> call, Throwable t) {

                    }
                }
        );

    }
    private void getEntreprise(){

 /*   client.lireEntreprises(ConnectUtils.authToken).enqueue(
            new Callback<List<Entreprise>>() {
                @Override
                public void onResponse(Call<List<Entreprise>> call, Response<List<Entreprise>> response) {
                    if (response.isSuccessful()) {
                        Log.d("tag", "voic l'id de ce dernier "+ConnectUtils.authId);
                        List<Entreprise> lesEntreprises = response.body();
                        for(Entreprise entreprise : lesEntreprises){
                            if(entreprise.getId_etudiant()!= null){
                                if(entreprise.getId_etudiant().equals(ConnectUtils.authId)){
                                    listOffre.add(new OffreStageListModel(entreprise.getNom(),entreprise.getEmail()));
                                }
                            }


                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Entreprise>> call, Throwable t) {

                }
            }
    );*/
     client.getEtudiantConnecte(ConnectUtils.authToken).enqueue(new Callback<ComptePOJO>() {
         @Override
         public void onResponse(Call<ComptePOJO> call, Response<ComptePOJO> response) {
             if(response.isSuccessful()){
                 Log.d("tag","yess");
                 ComptePOJO etudiant = response.body();
                 for(Entreprise entreprise : etudiant.getEntreprises()){
                     listeEntreprises.add(new Entreprise(entreprise.getId(),entreprise.getNom(),entreprise.getContact(),entreprise.getEmail(),
                             entreprise.getTelephone(),entreprise.getSiteWeb(),entreprise.getAdresse(),entreprise.getVille(),
                             "",entreprise.getCodePostal(),"",entreprise.getEstFavorite(),""));
                 }
             }
         }

         @Override
         public void onFailure(Call<ComptePOJO> call, Throwable t) {

         }
     });
    }

    private void deconnexionUser(){
        bouttonDeco = findViewById(R.id.button);
        bouttonDeco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                deconnexion();
            }
        });
    }

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