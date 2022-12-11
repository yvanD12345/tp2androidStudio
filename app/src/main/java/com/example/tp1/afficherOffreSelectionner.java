package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp1.bds.DBHelper;
import com.example.tp1.data.Entreprise;
import com.example.tp1.network.ConnectUtils;
import com.example.tp1.network.MonAPIClient;
import com.example.tp1.network.MonApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class afficherOffreSelectionner extends AppCompatActivity {

    DBHelper Tp1bd;
    EditText email, Contact, ville, adresse, telephone, url, postalCode;
    ImageView bouttonMail,bouttonAppel,bouttonWeb;
    TextView nom, bouttonUpdate, bouttonSupprimerCompanie;
    List<Entreprise> listEntreprises;
    MonApi client =  MonAPIClient.getRetrofit().create(MonApi.class);

    //dictionnaire qui va stocquer les infos entrer dans chaque input. Chaque key correspond à un row de la table offre
    Map<String, String> dictionnaireDonner = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //recois le nom de la company grace aux extra et cherche les attribut de ce dernier
        //pour les afficher dans les input du layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_offre);

        Tp1bd = new DBHelper(this);

     //   String nom = extras.getString("companyName");

     //   Cursor resultatDeLaRecherche = Tp1bd.RechercherCompanie(nom);
        Contact = findViewById(R.id.inputContact);
        nom = findViewById(R.id.updateOffer);
        email = findViewById(R.id.inputCourriell);
        ville = findViewById(R.id.inputVillee);
        adresse = findViewById(R.id.inputAdressee);
        telephone = findViewById(R.id.inputTel);
        url = findViewById(R.id.inputSiteWebb);
        postalCode = findViewById(R.id.inputcodePostall);


        envoyerMail();
        allerSurSite();
        appeler();
      //  mettreAJourCompanie(nom);
        mettreAJourEntreprise();
/*
        while (resultatDeLaRecherche.moveToNext()) {
            Contact.setHint(resultatDeLaRecherche.getString(3));
            email.setHint(resultatDeLaRecherche.getString(2));
            companyName.setText(resultatDeLaRecherche.getString(1));
            ville.setHint(resultatDeLaRecherche.getString(5));
            postalCode.setHint(resultatDeLaRecherche.getString(6));
            adresse.setHint(resultatDeLaRecherche.getString(7));
            telephone.setHint(resultatDeLaRecherche.getString(8));
            url.setHint(resultatDeLaRecherche.getString(9));

          //prend les elements des inputs et update l'offre avec les elements entrer

        supprimerUneCompanie(companyName.getText().toString());

    }

 */
        supprimerUneCompanie();
        initialiserLesChamps();

    }
    public void mettreAJourEntreprise(){
        Entreprise entrepriseTemp = initialiserUneNouvelleEntreprise();
        Bundle extras = getIntent().getExtras();
        bouttonUpdate = findViewById(R.id.buttonUpdate);
        bouttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlVerif = "";
                urlVerif = verifierUrl(url.getText().toString());
                initialiserDictionnaire(urlVerif);
                Entreprise entreprisePourLaMofication = updateEntrepriseTemp(entrepriseTemp,dictionnaireDonner);
                Log.d("TAG","CONTACT new entreprise "+Contact.getText().toString()+ "ville "+ville.getText().toString());

                client.modifierEntreprise(ConnectUtils.authToken, extras.getString("id"),entreprisePourLaMofication).enqueue(new Callback<Entreprise>() {
                    @Override
                    public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                        if(response.isSuccessful()){
                            Log.d("TAG","yata man");
                            Toast.makeText(afficherOffreSelectionner.this,"mets a jour",Toast.LENGTH_SHORT).show();
                            Log.d("TAG"," mis a jour");
                        }
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {
                        Toast.makeText(afficherOffreSelectionner.this,"mets pas a jour",Toast.LENGTH_SHORT).show();
                        Log.d("TAG","pas de mis a jour");
                    }
                });
            }
        });
    }
    public Entreprise updateEntrepriseTemp(Entreprise entreprise, Map<String,String> valueInput ){
        for (String key : valueInput.keySet()) {
            switch(key){
                case "contact": if(!valueInput.get(key).equals("")){entreprise.setContact(Contact.getText().toString());}
                    break;
                case "email": if(!valueInput.get(key).equals("")){entreprise.setEmail(email.getText().toString());}
                    break;
                case "ville": if(!valueInput.get(key).equals("")){entreprise.setVille(ville.getText().toString());}
                    break;
                case "postalCode": if(!valueInput.get(key).equals("")){entreprise.setCodePostal(postalCode.getText().toString());}
                    break;
                case "adresse": if(!valueInput.get(key).equals("")){entreprise.setAdresse(adresse.getText().toString());}
                    break;
                case "telephone": if(!valueInput.get(key).equals("")){entreprise.setTelephone(telephone.getText().toString());}
                    break;
                case "url": if(!valueInput.get(key).equals("")){entreprise.setSiteWeb(url.getText().toString());}
                    break;

            }
        }
        return entreprise;
    }
    public void initialiserDictionnaire(String urlVerif){
        dictionnaireDonner.put("contact", Contact.getText().toString());
        dictionnaireDonner.put("companyName", nom.getText().toString());
        dictionnaireDonner.put("email",  email.getText().toString());
        dictionnaireDonner.put("ville", ville.getText().toString());
        dictionnaireDonner.put("adresse", adresse.getText().toString());
        dictionnaireDonner.put("telephone", telephone.getText().toString());
        dictionnaireDonner.put("postalCode", postalCode.getText().toString());
        dictionnaireDonner.put("url", urlVerif);
    }
    public Entreprise initialiserUneNouvelleEntreprise(){
        Bundle extras = getIntent().getExtras();
      Entreprise entreprise = new Entreprise(extras.getString("id"),extras.getString("nom"), extras.getString("Contact")
              ,extras.getString("email"),extras.getString("telephone"),extras.getString("url"),
              extras.getString("adresse"),extras.getString("ville"),"",extras.getString("postalCode"),null,false);

      return entreprise;
    }
    public void initialiserLesChamps(){
        Bundle extras = getIntent().getExtras();
        Log.d("TAG","voici extra"+" contact"+extras.getString("contact")+" site web"+extras.getString("telephone"));
       nom.setText(extras.getString("nom"));
       email.setHint(extras.getString("courriel"));
       telephone.setHint(extras.getString("telephone"));
       ville.setHint(extras.getString("ville"));
       adresse.setHint(extras.getString("adresse"));
       url.setHint(extras.getString("url"));
       postalCode.setHint(extras.getString("postalCode"));
       Contact.setHint(extras.getString("Contact"));
    }
//mettre à jour la companie sellectionnner
    public void mettreAJourCompanie(String nom){
        bouttonUpdate = findViewById(R.id.buttonUpdate);
        bouttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlVerif;
                urlVerif = verifierUrl(url.getText().toString());
                /*
                dictionnaireDonner.put("contact", Contact.getText().toString());
                dictionnaireDonner.put("companyName", companyName.getText().toString());
                dictionnaireDonner.put("email",  email.getText().toString());
                dictionnaireDonner.put("ville", ville.getText().toString());
                dictionnaireDonner.put("adresse", adresse.getText().toString());
                dictionnaireDonner.put("telephone", telephone.getText().toString());
                dictionnaireDonner.put("postalCode", postalCode.getText().toString());
                dictionnaireDonner.put("url", urlVerif);
                if(Tp1bd.updateCompanie(dictionnaireDonner,nom)){
                    Toast.makeText(afficherOffreSelectionner.this, "l'update a été faite", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(afficherOffreSelectionner.this, "l'update n'a pas été faite", Toast.LENGTH_SHORT).show();
                }

                 */

            }
        });
    }
//retourne vide si l'url n'est pas conforme et n'update rien si c'est vide
    public String verifierUrl(String inputurl) {
        if (inputurl.contains("https://")) {
            return inputurl;
        } else {
            return "";
        }
    }
//appler la companie
    public void appeler(){

        bouttonAppel = findViewById(R.id.appeler);
        bouttonAppel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri appel = Uri.parse("tel:"+telephone.getHint().toString());
                Intent intentAppel = new Intent(Intent.ACTION_DIAL,appel);
                startActivity(intentAppel);
            }
        });


    }

//envoyer un mail à la companie
    public void envoyerMail(){
        bouttonMail = findViewById(R.id.envoiEmail);
        bouttonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent envoiMail = new Intent(Intent.ACTION_SENDTO);
                envoiMail.setData(Uri.parse("mailto:"+email.getHint()));
                envoiMail.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                envoiMail.putExtra(Intent.EXTRA_TEXT, "Body" );
                startActivity(envoiMail);
            }
        });

    }
    //aller sur le site de la companie
    public void allerSurSite(){
        bouttonWeb = findViewById(R.id.pageWeb);
        bouttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webSite = String.valueOf(url.getHint());
                if (!webSite.startsWith("http://") && !webSite.startsWith("https://")) {
                    webSite = ("http://" + webSite);
                }
                Uri myUri = Uri.parse(webSite);
                Intent pageWebIntent = new Intent(Intent.ACTION_VIEW);
                pageWebIntent.setData(myUri);
                startActivity(pageWebIntent);
            }
        });


    }
  /*  public void supprimerUneCompanie(String nomCompanie){
        bouttonSupprimerCompanie = findViewById(R.id.buttonSuppression);

        bouttonSupprimerCompanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean suppressionEffectuer = Tp1bd.supprimerUneCompanie(nomCompanie);
            if( suppressionEffectuer){
                Toast.makeText(afficherOffreSelectionner.this,"entreprise n'a pas été supprimer",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(afficherOffreSelectionner.this,"entreprise a été supprimer",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }*/
    public void supprimerUneCompanie(){
        Bundle extras = getIntent().getExtras();
        bouttonSupprimerCompanie = findViewById(R.id.buttonSuppression);
        bouttonSupprimerCompanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.supprEntreprise(ConnectUtils.authToken,extras.getString("id")).enqueue(new Callback<Entreprise>() {
                    @Override
                    public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                        Toast.makeText(afficherOffreSelectionner.this,"entreprise supprimer",Toast.LENGTH_SHORT).show();
                        Log.d("TAG"," entreprise supprimer");
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {
                        Toast.makeText(afficherOffreSelectionner.this,"entreprise toujours la",Toast.LENGTH_SHORT).show();
                        Log.d("TAG","entreprise toujour la");
                    }
                });
            }
        });
    }


}

