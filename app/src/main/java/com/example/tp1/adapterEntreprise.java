package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.tp1.data.Entreprise;
import com.example.tp1.network.ConnectUtils;
import com.example.tp1.network.MonAPIClient;
import com.example.tp1.network.MonApi;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class adapterEntreprise extends RecyclerView.Adapter<adapterEntreprise.MyViewHolder> {

    private Context context;


    private ArrayList<Entreprise> entreprises;
     MonApi client =  MonAPIClient.getRetrofit().create(MonApi.class);
    private int checkedPosition = 0;

    public adapterEntreprise(Context context, ArrayList<Entreprise> entreprises) {
        this.context = context;

     this.entreprises = entreprises;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.designe_recyclervew2,parent,false);
        return new MyViewHolder(view);
    }
//inittialiser les donner pour des textview et image view dans le card view
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       Entreprise model = entreprises.get(position);
       holder.nomCompanie.setText(model.getNom());
       holder.emailCompanie.setText(model.getEmail());

    }
/**
 * change la liste d'entreprise et on dit l'adaptateur que sa liste a changé
  * @param entreprise liste d'entreprises qui va remplacer celle dans l'adaptateur
 */
    public void changerEffectuer(ArrayList<Entreprise> entreprise){
        this.entreprises = entreprise;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
      //  return companyName_id.size();
        return entreprises.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomCompanie, email_id, contact_id, emailCompanie;
        ToggleButton bouttonFavori;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomCompanie = itemView.findViewById(R.id.nom);
            emailCompanie = itemView.findViewById(R.id.emailentreprise);
            bouttonFavori = itemView.findViewById(R.id.bouttonFavori);
            //lorsqu'on clique sur un item du recycler view. Je prend sa position et lance un activité pour montrer infos sur celle ci
            bouttonFavori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                  if(bouttonFavori.isChecked()){
                      Entreprise entreprise = entreprises.get(pos);
                      entreprise.setEstFavorite(true);
                      changerEntreprisePourFavori(entreprise);
                    }
                  else if(!bouttonFavori.isChecked()){
                      Entreprise entreprise = entreprises.get(pos);
                      entreprise.setEstFavorite(false);
                      changerEntreprisePourFavori(entreprise);
                  }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();


                    if(pos != RecyclerView.NO_POSITION){
                        Entreprise clickedItem = entreprises.get(pos);
                        Toast.makeText(v.getContext(), "tu a cliquer sur " + clickedItem.getNom(), Toast.LENGTH_SHORT).show();
                        Intent infoEntreprise = new Intent(context, afficherOffreSelectionner.class);
                        infoEntreprise.putExtra("nom",clickedItem.getNom());
                        infoEntreprise.putExtra("ville",clickedItem.getVille());
                        infoEntreprise.putExtra("adresse",clickedItem.getAdresse());
                        infoEntreprise.putExtra("telephone",clickedItem.getTelephone());
                        infoEntreprise.putExtra("postalCode",clickedItem.getCodePostal());
                        infoEntreprise.putExtra("url",clickedItem.getSiteWeb());
                        infoEntreprise.putExtra("Contact",clickedItem.getContact());
                        infoEntreprise.putExtra("courriel",clickedItem.getEmail());
                        infoEntreprise.putExtra("id",clickedItem.getId());

                        v.getContext().startActivity(infoEntreprise);
                    }
                }
            });
        }
    }
    /**
     * change l'entreprise avec le meme id avec celle en parametre dans la méthode afin de faire l'actualisation
     * @param entreprise est l'entreprise qui a été modifier pour etre mise ou nom en favori
     */
    public void changerEntreprisePourFavori(Entreprise entreprise){
        client.modifierEntreprise(ConnectUtils.authToken,entreprise.getId(),entreprise).enqueue(new Callback<Entreprise>() {
            @Override
            public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                if(response.isSuccessful()){

                }

            }

            @Override
            public void onFailure(Call<Entreprise> call, Throwable t) {

            }
        });
    }

}
