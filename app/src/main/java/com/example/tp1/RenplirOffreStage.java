package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RenplirOffreStage extends AppCompatActivity {

    //vont contenir les infos entré dans les inputs
    EditText nomCompanie,email,Contact,poste,ville,numeroDeRue,nomRue,telephone,url,postalCode;
    //boutton pour poster l'offre
    Button bouttonPoster;
    //creation de la bd
    DBHelper Tp1bd;
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
            }

//creer l'adresse à partir de la concatenation de la rue , nom de rue et la ville
            public String concatenationAdresse(){
                String adresse = "";

                    adresse = numeroDeRue.getText().toString()+" "+ nomRue.getText().toString()+" "+ville.getText().toString();

                return adresse;
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