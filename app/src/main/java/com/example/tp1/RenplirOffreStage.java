package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tp1.bds.DBHelper;
import com.example.tp1.data.ComptePOJO;
import com.example.tp1.data.Entreprise;
import com.example.tp1.network.ConnectUtils;
import com.example.tp1.network.MonAPIClient;
import com.example.tp1.network.MonApi;

import java.util.List;
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
        nomCompanie = findViewById(R.id.companyName);
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
    //s'occupe de poster l'offre d'emploi avec les donnée dans les input lorsque le boutton pour poster est cliqué
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
                //si le string == "" c'est qu'il a echouer la  sa verification en n'etant pas conforme et si l'adresse == ""
                //ca veux dire qu'un des n'a pas été rempli


                /*
                if(UrlEnString.equals("") || AdresseEnString.equals("")) {

                    Toast.makeText(RenplirOffreStage.this, "l'offre de stage n'a pas été postuléw", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean insertionReussi = Tp1bd.insererUneOffre(nomCompanyEnString, emailEnString, contactEnString, nomPosteEnString, VilleEnString, PostalCodeEnString, AdresseEnString, telephoneEnString,UrlEnString);
                    if (insertionReussi == true) {
                        Toast.makeText(RenplirOffreStage.this, "une nouvelle offre de stage a été postulée", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RenplirOffreStage.this, "l'offre de stage n'a pas été postuléw", Toast.LENGTH_SHORT).show();
                    }

                }

                 */

                ajouterEntrepriseEnEtantConnecter();
            }

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

                            Log.d("tag","ajout d'entreprise reussi");
                            Toast.makeText(RenplirOffreStage.this,"mets a jour",Toast.LENGTH_SHORT).show();
                       //      ajouterLentrepriseALetudiant(entreprise);
                        }
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {

                    }
                });
            }
          /*  public void ajouterLentrepriseALetudiant(Entreprise entreprise){
                client.getEtudiantConnecte(ConnectUtils.authToken).enqueue(new Callback<List<ComptePOJO>>() {
                    @Override
                    public void onResponse(Call<List<ComptePOJO>> call, Response<List<ComptePOJO>> response) {
                        if(response.isSuccessful()){
                            List<ComptePOJO> etudiant = response.body();
                            Log.d("tag","voici le nombre d'etudiant trouver"+" "+etudiant.size());
                            etudiant.get(0).ajouterUneEntreprise(entreprise);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ComptePOJO>> call, Throwable t) {
                        Log.d("tag","echec");
                    }
                });
            }*/

//creer l'adresse à partir de la concatenation de la rue , nom de rue et la ville
            public String concatenationAdresse(){
                String adresse = "";

                    adresse = numeroDeRue.getText().toString()+" "+ nomRue.getText().toString()+" "+ville.getText().toString();

                return adresse;
            }
            public UUID generateUid(){
                UUID luid = UUID.randomUUID();
                return luid;
            }

            //si le string ne contient pas https je retourne un url conforme
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