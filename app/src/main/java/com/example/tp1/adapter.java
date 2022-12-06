package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {

    private Context context;


    private ArrayList<OffreStageListModel> offreStageListModels;
    private int checkedPosition = 0;//

    public adapter(Context context, ArrayList<OffreStageListModel> offreStageListModels) {
        this.context = context;

     this.offreStageListModels = offreStageListModels;
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

       OffreStageListModel model = offreStageListModels.get(position);
       holder.companyname_id.setText(model.getNomCompanie());
       holder.poste_id.setText(model.getPoste());

    }

    @Override
    public int getItemCount() {
      //  return companyName_id.size();
        return offreStageListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView companyname_id, email_id, contact_id,poste_id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyname_id = itemView.findViewById(R.id.companyName);
            poste_id = itemView.findViewById(R.id.nomPoste);
            //lorsqu'on clique sur un item du recycler view. Je prend sa position et lance un activité pour montrer infos sur celle ci
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        OffreStageListModel clickedItem = offreStageListModels.get(pos);
                        Toast.makeText(v.getContext(), "tu a cliquer sur " + clickedItem.getNomCompanie(), Toast.LENGTH_SHORT).show();
                        Intent afficherInfoOffre = new Intent(context, afficherOffreSelectionner.class);
                        afficherInfoOffre.putExtra("companyName",clickedItem.getNomCompanie());
                        v.getContext().startActivity(afficherInfoOffre);
                    }
                }
            });
        }
    }

}
