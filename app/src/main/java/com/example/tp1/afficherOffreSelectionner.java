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

        mettreAJourEntreprise();

        supprimerUneCompanie();
        initialiserLesChamps();

    }
    /**
     * sert à mettre à jour l'entreprise lorsque le boutton est sélectionner
     * @param
     */
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


                client.modifierEntreprise(ConnectUtils.authToken, extras.getString("id"),entreprisePourLaMofication).enqueue(new Callback<Entreprise>() {
                    @Override
                    public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                        if(response.isSuccessful()){

                            Toast.makeText(afficherOffreSelectionner.this,"mets a jour",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {
                        Toast.makeText(afficherOffreSelectionner.this,"mets pas a jour",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    /**
     * Cette  methode retourne une entreprise temporaire qui elle va etre modifier en fonction de ce que l'utilisateur aura entrer et cette entreprise sera utiliser pour la mise a jour
     * @param entreprise est l'entreprise temporaire qui va etre changer puis retourner
     * @param valueInput contient le dictionnaireDonnee qui lui contient le contenue de tous les inputs de l'activité
     * @return une entreprise temporaire qui sera utiliser pour la mise à jour de l'entreprise dans le serveur distant
     */
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

    /**
     * initialise un dictionnaire qui va contenir toutes les valeurs de tous champs de l'activité
     * @param urlVerif est le string contenant le siteweb
     */
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
    /**
     * sert à retourner une entreprise temporaire avec les infos de l'entreprise qui avait été selectionner dans l'activité précédente
     * @param
     */
    public Entreprise initialiserUneNouvelleEntreprise(){
        Bundle extras = getIntent().getExtras();
      Entreprise entreprise = new Entreprise(extras.getString("id"),extras.getString("nom"), extras.getString("Contact")
              ,extras.getString("email"),extras.getString("telephone"),extras.getString("url"),
              extras.getString("adresse"),extras.getString("ville"),"",extras.getString("postalCode"),null,false);

      return entreprise;
    }

    /**
     * sert a initialiser le hint des editext avec les infos de l'entreprise
     * @param
     */
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



    /**
     * verifie l'url entrer
     * @param inputurl est l'url entrer par l'utilisateur
     * @return un string vide
     */
    public String verifierUrl(String inputurl) {
        if (inputurl.contains("https://")) {
            return inputurl;
        } else {
            return "";
        }
    }


    /**
     * permet d'appeler avec les infos de l'entreprise
     */
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



    /**
     * ouvre gmail avec le mail de l'entreprise
     */
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


    /**
     * permet d'aller sur le site web de l'entreprise
     */
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

    /**
     * permet de supprimer une entreprise avec l'id de cette derniere
     */
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

