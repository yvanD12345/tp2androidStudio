package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tp1.bds.DBHelper;
import com.example.tp1.data.Entreprise;
import com.example.tp1.network.ConnectUtils;
import com.example.tp1.network.MonAPIClient;
import com.example.tp1.network.MonApi;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RenplirOffreStage extends AppCompatActivity {

    //vont contenir les infos entré dans les inputs
    EditText nomCompanie,email,Contact,poste,ville,numeroDeRue,nomRue,telephone,url,postalCode;
    //boutton pour poster l'offre
    Button bouttonPoster;
    //creation de la bd
    DBHelper Tp1bd;

    private MonApi client = MonAPIClient.getRetrofit().create(MonApi.class);

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renplir_offre_stage);
        Tp1bd = new DBHelper(this);
        nomCompanie = findViewById(R.id.nom);
        email = findViewById(R.id.emailCompany);
        Contact =  findViewById(R.id.Contact);
        poste = findViewById(R.id.poste);
        ville = findViewById(R.id.ville);
        numeroDeRue = findViewById(R.id.numeroRue);
        nomRue = findViewById(R.id.nomRue);
        url = findViewById(R.id.url);
        telephone = findViewById(R.id.editTextPhone);
        postalCode = findViewById(R.id.postalCode);
        ville = findViewById(R.id.ville);


        posterOffreStage();
    }

    public void posterOffreStage(){

        bouttonPoster = findViewById(R.id.buttonPosterOffre);
        bouttonPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialise s'occupe de prendre les données entrer sous forme de strings
                String nomCompanyEnString = nomCompanie.getText().toString();
                String emailEnString = email.getText().toString();
                String contactEnString= Contact.getText().toString();
                String nomPosteEnString = poste.getText().toString();
                String VilleEnString= ville.getText().toString();
                String AdresseEnString = concatenationAdresse();
                String UrlEnString = verifUrl();
                String telephoneEnString= telephone.getText().toString();
                String PostalCodeEnString = postalCode.getText().toString();


                ajouterEntrepriseEnEtantConnecter();
            }

            /**
             * prend le contenu des inputs entrer par l'utilisateur et initiluise une entreprise avec pour l'ajouter dans le serveur distant
             */
            public void ajouterEntrepriseEnEtantConnecter(){
                String id = generateUid().toString();
                String nomCompanyEnString = nomCompanie.getText().toString();
                String emailEnString = email.getText().toString();
                String contactEnString= Contact.getText().toString();
                String nomPosteEnString = poste.getText().toString();
                String VilleEnString= ville.getText().toString();
                String AdresseEnString = concatenationAdresse();
                String UrlEnString = verifUrl();
                String telephoneEnString= telephone.getText().toString();
                String PostalCodeEnString = postalCode.getText().toString();

                Entreprise entreprise = new Entreprise(id,nomCompanyEnString,contactEnString,
                        emailEnString,telephoneEnString,UrlEnString,AdresseEnString,
                        VilleEnString,"",PostalCodeEnString,"",false);
                client.creerEntreprise(ConnectUtils.authToken,entreprise).enqueue(new Callback<Entreprise>(){

                    @Override
                    public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                        if (response.isSuccessful()) {

                        }
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {

                    }
                });
            }

            /**
             * concatene l'adresse avec les champs les de numero de rue , nom  de rue et etc
             * @return une adresse
             */
            public String concatenationAdresse(){
                String adresse = "";

                    adresse = numeroDeRue.getText().toString()+" "+ nomRue.getText().toString()+" "+ville.getText().toString();

                return adresse;
            }
            public UUID generateUid(){
                UUID luid = UUID.randomUUID();
                return luid;
            }

            /**
             * verifie l'url
             * @return un url conforme
             */
            public String verifUrl(){
                String UrlEnString =   url.getText().toString();
                if(UrlEnString.startsWith("https://")){
                    return UrlEnString;
                }
                else{
                    return "https://"+UrlEnString;
                }

            }
        });
    }



}