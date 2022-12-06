package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
   DBHelper Tp1bd;
   EditText elementRechercher;
   TextView bouttonRechercher;
   ImageView bouttonMap, bouttonAjouterOffre;
    RecyclerView recyclerView;
    ArrayList<OffreStageListModel> listOffre;
    private  boolean mLocationPermissionGranted= false;

    adapter leAdapter;

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
        Cursor resultatRecherche = Tp1bd.RechercherCompanie("all");
        if(resultatRecherche.getCount()== 0){
            Toast.makeText(HomePage.this,"aucune offre trouvé avec l'element recherchée",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //ajoute dans la listeOffre toutes celle qui seront afficher et le contenu de ce dernier sera dans le recycler view
            while(resultatRecherche.moveToNext()){
                listOffre.add(new OffreStageListModel(resultatRecherche.getString(1),resultatRecherche.getString(4)));
            }
        }
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