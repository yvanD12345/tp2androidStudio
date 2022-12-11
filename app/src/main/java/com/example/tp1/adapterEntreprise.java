package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tp1.data.Entreprise;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterEntreprise extends RecyclerView.Adapter<adapterEntreprise.MyViewHolder> {

    private Context context;


    private ArrayList<Entreprise> entreprises;
    private int checkedPosition = 0;

    public adapterEntreprise(Context context, ArrayList<Entreprise> entreprises) {
        this.context = context;

     this.entreprises = entreprises;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.designe_recyclervew,parent,false);
        return new MyViewHolder(view);
    }
//inittialiser les donner pour des textview et image view dans le card view
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       Entreprise model = entreprises.get(position);
       holder.nomCompanie.setText(model.getNom());
       holder.emailCompanie.setText(model.getEmail());

    }

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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomCompanie = itemView.findViewById(R.id.companyName);
            emailCompanie = itemView.findViewById(R.id.nomPoste);
            //lorsqu'on clique sur un item du recycler view. Je prend sa position et lance un activité pour montrer infos sur celle ci
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
                        Log.d("TAG","VOICI INFO ENTREPRISE"+" CONTACT"+clickedItem.getContact()+" ville "+clickedItem.getVille());
                        v.getContext().startActivity(infoEntreprise);
                    }
                }
            });
        }
    }

}
