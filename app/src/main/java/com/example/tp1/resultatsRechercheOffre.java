package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tp1.bds.DBHelper;

import java.util.ArrayList;
//sert à afficher la recherche ne le renez pas en compte je ne l'ai pas fini
public class resultatsRechercheOffre extends AppCompatActivity {
    RecyclerView recyclerView;

    ArrayList<OffreStageListModel> listOffreTrouve = new ArrayList<OffreStageListModel>();
    DBHelper Tp1bd;
    adapter adaptRecherche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultats_recherche_offre);
        Tp1bd = new DBHelper(this);
        recyclerView = findViewById(R.id.recyclerview);
        adaptRecherche = new adapter(this,listOffreTrouve);
        recyclerView.setAdapter(adaptRecherche);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        afficherResultatRecherche();
    }
//affiche le resultat de l'element rechercher
    private void afficherResultatRecherche() {
        Bundle extras = getIntent().getExtras();
        String strRecherche = extras.getString("key");
        //si aucun element n'a été entrée on ne fait rien
        if(strRecherche.equals("")){
            Toast.makeText(resultatsRechercheOffre.this,"aucune offre trouvé avec l'element recherchée",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Cursor resultatRecherche = Tp1bd.RechercherCompanie(strRecherche);
            if(resultatRecherche.getCount()== 0){
                Toast.makeText(resultatsRechercheOffre.this,"aucune offre trouvé avec l'element recherchée",Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                while(resultatRecherche.moveToNext()){
                    listOffreTrouve.add(new OffreStageListModel(resultatRecherche.getString(0),resultatRecherche.getString(3)));

                }
            }
        }

    }
}