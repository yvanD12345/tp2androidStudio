package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp1.bds.DBHelper;
import com.example.tp1.data.ComptePOJO;
import com.example.tp1.data.CompteResult;
import com.example.tp1.data.CreationCompteData;
import com.example.tp1.network.MonAPIClient;
import com.example.tp1.network.MonApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class page_dinscription extends AppCompatActivity {
    EditText username, mdp, confMdp, email,name;

    Button registerBoutton;

    DBHelper Tp1bd;

    private MonApi client = MonAPIClient.getRetrofit().create(MonApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_inscription);
        AllerAlaPageConnexion();
        Tp1bd = new DBHelper(this);

        username = (EditText) findViewById(R.id.inputPswL);
        mdp = (EditText) findViewById(R.id.inputPsw);
        email = (EditText) findViewById(R.id.inputCourriell);
        confMdp = (EditText) findViewById(R.id.inputConfPsw);
        name = (EditText) findViewById(R.id.inputNom);


      //  InscrireUser();
InscrireCompte();
    }


/*
    protected void InscrireUser() {
        registerBoutton = (Button)  findViewById(R.id.buttonRegister);
        registerBoutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String utilisateur = page_dinscription.this.username.getText().toString();
             String motDePasse = mdp.getText().toString();
             String confirmationMotDePasse = confMdp.getText().toString();
             //si le champs du username , mot de passe et conf mot de passe sont nul l'inscription ne se fait pas
             if(utilisateur.equals("") || mdp.equals("") || confirmationMotDePasse.equals("")){
                 Toast.makeText(page_dinscription.this,"Tous les champs doivent etre rempli",Toast.LENGTH_SHORT).show();
             }
             else {
                 //si le mdp est le meme que la confirmation du mdp on valide l'utilisateur et on le creer
                 if(motDePasse.equals(confirmationMotDePasse)){
                     Boolean validationUser = Tp1bd.validationUsername(utilisateur);
                     if(validationUser == false){
                         Boolean insertion = Tp1bd.insertionUserDansLaBd(utilisateur,motDePasse);
                         if(insertion ==true){
                             Toast.makeText(page_dinscription.this,"inscription complété",Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(getApplicationContext(),HomePage.class);
                             startActivity(intent);
                         }
                         else {
                             Toast.makeText(page_dinscription.this,"inscription reussi",Toast.LENGTH_SHORT).show();
                         }
                     }
                     else {
                         Toast.makeText(page_dinscription.this,"utilisateur existe déja",Toast.LENGTH_SHORT).show();
                     }
                 }
                 else {
                     Toast.makeText(page_dinscription.this,"password et confirmpassword ne match pas ",Toast.LENGTH_SHORT).show();
                 }
             }
            }
        });
    }
*/
    protected void InscrireCompte(){
        registerBoutton = (Button)  findViewById(R.id.buttonRegister);
        registerBoutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utilisateur = username.getText().toString();
                String mdpS = mdp.getText().toString();
                String confirmationMdp = confMdp.getText().toString();
                String nameS = name.getText().toString();
                String prenom = username.getText().toString();

                if (!utilisateur.equals("") || !mdpS.equals("") || !confirmationMdp.equals("")
                        || !nameS.equals("") || !prenom.equals("")) {
               if(mdpS.equals(confirmationMdp)) {
                   CreationCompteData creationCompteData = new CreationCompteData(utilisateur, mdpS, confirmationMdp, nameS, prenom);
                   client.creerCompte(creationCompteData).enqueue(new Callback<ComptePOJO>() {
                       @Override
                       public void onResponse(Call<ComptePOJO> call, Response<ComptePOJO> response) {
                           Log.d("tag", "inscription réussi");
                       }

                       @Override
                       public void onFailure(Call<ComptePOJO> call, Throwable t) {
                           
                       }
                   });
               }
               else {
                   Toast.makeText(page_dinscription.this,"password et confirmpassword ne match pas ",Toast.LENGTH_SHORT).show();
               }
                }
                else {
                    Toast.makeText(page_dinscription.this,"veuillez remplir tout les champs ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected void AllerAlaPageConnexion(){

        TextView button = findViewById(R.id.userAlreadyexist);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(page_dinscription.this, page_de_connexion.class));
            }
        });
    }
}