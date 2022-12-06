package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class page_de_connexion extends AppCompatActivity {

    EditText username, mdp;
    Button bouttonLogin;
    DBHelper Tp1bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_connexion);
        //methode permettant d'aller à ls psge d'inscription quand je clique sur le lien
        AllerALaPageInscription();

        username = (EditText) findViewById(R.id.inputUsernameL);
        mdp = (EditText) findViewById(R.id.inputPswL);
        Tp1bd = new DBHelper(this);
        connecterUser();
    }

    protected void connecterUser() {
        bouttonLogin = (Button)  findViewById(R.id.buttonLogin);
        bouttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String userEntrer = username.getText().toString();
             String mdpEntrer = mdp.getText().toString();

             if(userEntrer.equals("") || mdpEntrer.equals("")){
                 Toast.makeText(page_de_connexion.this,"un ou plusieurs champs sont vides. Veuillez les remplir", Toast.LENGTH_SHORT).show();
             }
             else {
                 //valide les infos entrer
                 Boolean validerUser = Tp1bd.validationUserPsw(userEntrer,mdpEntrer);
                 if(validerUser == true){
                     Toast.makeText(page_de_connexion.this,"connexion reussi",Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(getApplicationContext(),HomePage.class);
                     startActivity(intent);
                 }
                 else{
                     Toast.makeText(page_de_connexion.this,"mot de passe invalide",Toast.LENGTH_SHORT).show();
                 }
             }
            }
        });
    }
//permet de lancer l'activité d'invité
    protected void AllerALaPageInscription(){
        TextView button = findViewById(R.id.RegisterLink);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(page_de_connexion.this, page_dinscription.class));
            }
        });
    }
}