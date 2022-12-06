package com.example.tp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

     public static final String nom_bd = "tp.db";

    public DBHelper( Context context) {
        super(context, "tp.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase Tp1bd) {
        Tp1bd.execSQL("create Table users(id_user INTEGER primary key AUTOINCREMENT,utilisateur TEXT, mdp TEXT)");
        //Tp1bd.execSQL("CREATE TABLE IF NOT EXISTS offreEmploi(companyText TEXT primary key,Contact TEXT,Telephone NUMERIC,Courriel VARCHAR(255),ville TEXT,codePostal TEXT)");
        Tp1bd.execSQL("create Table companie(id_company INTEGER primary key AUTOINCREMENT,nomCompany Text, email TEXT,contact TEXT,poste TEXT, ville TEXT, postalCode TEXT, adresse TEXT, telephone TEXT,url TEXT,date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase Tp1bd, int oldVersion, int newVersion) {
        Tp1bd.execSQL("drop table if exists users");
        Tp1bd.execSQL("drop table if exists companie");
    }
    //permet d'inserer un user à entrer dans la bd et retourne un boolean si oui ou non l'insertion à pu se faire
    public Boolean insertionUserDansLaBd(String username, String psw){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        //données qui seront insérer dans bd
        ContentValues dataInsert = new ContentValues();
        dataInsert.put("utilisateur",username);
        dataInsert.put("mdp",psw);
        long resultInsertion = Tp1bd.insert("users",null,dataInsert);

        if(resultInsertion ==-1){
            return false;
        }
        else return true;

    }
    //permet d'inserer une offre dans la bd et retourne un boolean si oui ou non l'iinsertiion a été un succes
    public boolean insererUneOffre(String nomCompany , String email, String contact, String poste, String ville, String postalCode, String adresse, String telephone, String url){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        ContentValues offre = new ContentValues();
        offre.put("nomCompany", nomCompany);
        offre.put("email",email);
        offre.put("contact",contact);
        offre.put("poste",poste);
        offre.put("adresse",adresse);
        offre.put("postalCode",postalCode);
        offre.put("ville",ville);
        offre.put("telephone",telephone);
        offre.put("url",url);

        long resultat = Tp1bd.insert("companie",null,offre);
        if(resultat == -1){
            return false;
        }
        else return true;

    }

//met a jour les donnnées de la companie avec celle contenu dans le dictionnaire. La clée est l'attribut ou l'élément qui sera changer dans la bd
    //et retourne si oui l'insertion a été reussi
    public Boolean updateCompanie(Map<String,String> listeAttribut, String identifiant){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        ContentValues update = new ContentValues();
        for (String key : listeAttribut.keySet()) {
            switch(key){
                case "contact": if(!listeAttribut.get(key).equals("")){update.put("contact",listeAttribut.get(key));}
                    break;
                case "email": if(!listeAttribut.get(key).equals("")){update.put("email",listeAttribut.get(key));}
                    break;
                case "ville": if(!listeAttribut.get(key).equals("")){update.put("ville",listeAttribut.get(key));}
                    break;
                case "postalCode": if(!listeAttribut.get(key).equals("")){update.put("postalCode",listeAttribut.get(key));}
                    break;
                case "adresse": if(!listeAttribut.get(key).equals("")){update.put("adresse",listeAttribut.get(key));}
                    break;
                case "telephone": if(!listeAttribut.get(key).equals("")){update.put("telephone",listeAttribut.get(key));}
                    break;
                case "url": if(!listeAttribut.get(key).equals("")){update.put("url",listeAttribut.get(key));}
                    break;

            }
        }
        long resultat = Tp1bd.update("companie",update,"nomCompany=?",new String[]{identifiant});
        if(resultat == -1){
            return false;
        }
        else return true;
    }
//supprime la companie à parti du nom recu et retourne si oui ou non la suppression a àtà faite
    public Boolean supprimerUneCompanie(String nomCompanie){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        Cursor resultatQuery = Tp1bd.rawQuery("delete from companie where nomCompany = ?", new String[] {nomCompanie});
        if(resultatQuery.getCount() > 0){
            return true;
        }
        else return false;
    }
    //retourne une ou des adresses en fonction de l'élément recherchà
    public Cursor RechercherCompanie(String recherche){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        Cursor resultatRecherche;
        if(recherche.equals("all")) {
             resultatRecherche = Tp1bd.rawQuery("Select * from companie order by nomCompany asc", null);
        }
        else {
             resultatRecherche = Tp1bd.rawQuery("Select * from companie where nomCompany = ?", new String[]{recherche});

        }
        return resultatRecherche;
    }
//obtenir toutes les adresses dans la bd
    public Cursor obtenirAdresse(){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        Cursor resultat;
        resultat = Tp1bd.rawQuery("Select adresse from companie ",null);
        return resultat;
    }

    //fait une recherche dans la bd pour voir s'il y a au moin une occurence avec le userame entré retourne true si au moin une occurence est trouvé
    public Boolean validationUsername(String username){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        Cursor resultatQuery = Tp1bd.rawQuery("Select * from users where utilisateur = ?", new String[] {username});
        if(resultatQuery.getCount() > 0){
            return true;
        }
        else return false;
    }
    //fait une recherche dans la bd pour voir s'il y a au moin une occurence du psw de l'utilisateur entré retourne true si au moin une occurence est trouvé
    public Boolean validationUserPsw(String username, String psw){
        SQLiteDatabase Tp1bd = this.getWritableDatabase();
        Cursor resulatQuery = Tp1bd.rawQuery("select * from users where utilisateur = ? and mdp = ?",new String[] {username,psw});
        if(resulatQuery.getCount() > 0){
            return true;
        }
        else return false;
    }
}
