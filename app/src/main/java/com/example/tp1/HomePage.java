package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp1.bds.DBHelper;
import com.example.tp1.data.ComptePOJO;
import com.example.tp1.data.CompteResult;
import com.example.tp1.data.ConnexionUtilisateur;
import com.example.tp1.data.LoginData;
import com.example.tp1.network.ConnectUtils;
import com.example.tp1.network.MonAPIClient;
import com.example.tp1.network.MonApi;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  HomePage extends AppCompatActivity {
   DBHelper Tp1bd;
   EditText elementRechercher;
   TextView bouttonRechercher;
   Button bouttonDeco;
   ImageView bouttonMap, bouttonAjouterOffre;
    RecyclerView recyclerView;
    ArrayList<OffreStageListModel> listOffre;
    private  boolean mLocationPermissionGranted= false;

    adapter leAdapter;

    private MonApi client =   MonAPIClient.getRetrofit().create(MonApi.class);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        Tp1bd = new DBHelper(this);

         listOffre  = new ArrayList<OffreStageListModel>();
        recyclerView = findViewById(R.id.recyclerview);
        leAdapter = new adapter(this,listOffre);
        recyclerView.setAdapter(leAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        afficherTouteLesOffre();
        AllerAlaPagePourPostUneOffre();
        lancerUneRecherche();
        afficherLaCarte();
     //   deconnexionUser();

    }
    //permet rechercher une offre dans la barre mais elle n'est pas encore fonctionnel
    private void lancerUneRecherche(){
        elementRechercher = findViewById(R.id.inputRecherche);
        bouttonRechercher = findViewById(R.id.researchButton);
        bouttonRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementRechercher = findViewById(R.id.inputRecherche);
                String sRecherche = elementRechercher.getText().toString();
                Intent lancerRecherche = new Intent( HomePage.this,resultatsRechercheOffre.class);
                lancerRecherche.putExtra("key", sRecherche);
                startActivity(lancerRecherche);
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



    private void afficherTouteLesOffre() {
        //cherche dans la bd toute les offre
     getEtudiants();
    }

    private void affichageEleve(){

    }
    private void affichageEntreprise(){

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
                                listOffre.add(new OffreStageListModel(compte.getEmail(),compte.getNom())) ;
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<List<ComptePOJO>> call, Throwable t) {

                    }
                }
        );

    }
/*
    private void deconnexionUser(){
        bouttonDeco = findViewById(R.id.buttonDeco);
        bouttonDeco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = MonAPIClient.getRetrofit().create(MonApi.class);

                ConnexionUtilisateur user = new ConnexionUtilisateur();
                user.setId_compte(ConnectUtils.authId);
                client.testerConnexion(ConnectUtils.authToken, user.getId_compte()).enqueue(
                        new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (!response.isSuccessful()) {


                                }
                                else if(response.isSuccessful()){
                                    deconnexion();
                                }
                                else {



                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        }
                );
            }
        });
    }

    private void deconnexion(){

        client.deconnecter (ConnectUtils.authToken).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             Intent deco = new Intent(HomePage.this,page_de_connexion.class);
             startActivity(deco);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
*/
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