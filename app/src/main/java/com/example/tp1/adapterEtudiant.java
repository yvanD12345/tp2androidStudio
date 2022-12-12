package com.example.tp1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp1.data.ComptePOJO;

import java.util.ArrayList;

public class adapterEtudiant extends RecyclerView.Adapter<adapterEtudiant.MyViewHolder>{


    private Context context;
    @NonNull ViewGroup parent;


    private ArrayList<ComptePOJO> listeEtudiants;
    private int checkedPosition = 0;//

    public adapterEtudiant(Context context, ArrayList<ComptePOJO> listeEtudiants) {
        this.context = context;

        this.listeEtudiants = listeEtudiants;
    }

    @NonNull
    @Override
    public adapterEtudiant.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.designe_recyclervew,parent,false);
        return new adapterEtudiant.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ComptePOJO model = listeEtudiants.get(position);

        if(model.getStageTrouver() == null){
            holder.nom.setText(model.getNom());
            holder.prenom.setText(model.getPrenom());
            return;
        }

        if(model.getStageTrouver()){
            holder.nom.setTextColor(Color.RED);
            holder.nom.setText(model.getNom());
            holder.prenom.setText(model.getPrenom());
            return;
        }

    }



    @Override
    public int getItemCount() {
        //  return companyName_id.size();
        return listeEtudiants.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom, email_id, contact_id, prenom;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom);
            prenom = itemView.findViewById(R.id.emailentreprise);

            //lorsqu'on clique sur un item du recycler view. Je prend sa position et lance un activité pour montrer infos sur celle ci

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();


                }
            });
        }
    }

}
