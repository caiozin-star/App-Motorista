package com.souzs.appmotoristatcc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.model.Linha;

import java.util.List;

public class AdapterSelectL extends RecyclerView.Adapter<AdapterSelectL.MyViewHolder> {
    List<Linha> linhasList;
    Context c;

    public AdapterSelectL(List<Linha> linhasList, Context c) {
        this.linhasList = linhasList;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_linhas_s, parent, false);
        return new AdapterSelectL.MyViewHolder(itemLista);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Linha l = linhasList.get(position);

            holder.nomeLinha.setText(l.getNomeLinha());
    }

    @Override
    public int getItemCount() {
        return linhasList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView nomeLinha;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeLinha = itemView.findViewById(R.id.textAdapterNomeS);
        }
    }
}
