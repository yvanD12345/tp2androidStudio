package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class page_de_connexion extends AppCompatActivity {

    EditText username, mdp;
    Button bouttonLogin;
    DBHelper Tp1bd;
    private MonApi client;

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


                client = MonAPIClient.getRetrofit().create(MonApi.class);

                ConnexionUtilisateur user = new ConnexionUtilisateur();
                user.setId_compte(ConnectUtils.authId);
                client.testerConnexion(ConnectUtils.authToken, user.getId_compte()).enqueue(
                        new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (!response.isSuccessful()) {

                                    connecter();
                                }
                                else {



                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                connecter();
                            }
                        }
                );
              /*
             if(userEntrer.equals("") || mdpEntrer.equals("")){
                 Toast.makeText(page_de_connexion.this,"un ou plusieurs champs sont vides. Veuillez les remplir", Toast.LENGTH_SHORT).show();
             }
             else {

             }
             */
            }
        });


    }

    private void connecter() {

        String userEntrer = username.getText().toString();
        String mdpEntrer = mdp.getText().toString();

        LoginData loginData = new LoginData(userEntrer, mdpEntrer);

        client.connecter(loginData).enqueue(new Callback<CompteResult>() {
            @Override
            public void onResponse(Call<CompteResult> call, Response<CompteResult> response) {
                if (response.isSuccessful()) {
                    CompteResult json = response.body();
                    ConnectUtils.authToken = json.getAccessToken();
                    ConnectUtils.authId = json.getId();
                    ConnectUtils.authTypeUtilisateur = json.getTypeCompte();

                    Intent connexion = new Intent(page_de_connexion.this,HomePage.class);
                    String[] myStrings = new String[] {userEntrer,mdpEntrer};
                    connexion.putExtra("strings", myStrings);
                    startActivity(connexion);
                    Log.d("tag", "connexion par l'id"+json.getId());
                }
            }

            @Override
            public void onFailure(Call<CompteResult> call, Throwable t) {

            }
        });

    }


//permet de lancer l'activité d'invité
    public void AllerALaPageInscription(){
        TextView button = findViewById(R.id.RegisterLink);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(page_de_connexion.this, page_dinscription.class));
            }
        });
    }
}