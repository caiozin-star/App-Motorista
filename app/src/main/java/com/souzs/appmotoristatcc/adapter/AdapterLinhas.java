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

public class AdapterLinhas extends RecyclerView.Adapter<AdapterLinhas.MyViewHolder> {
    List<Linha> linhasList;
    Context c;

    public AdapterLinhas(List<Linha> linhasList, Context c) {
        this.linhasList = linhasList;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Linha l = linhasList.get(position);

        holder.nomeLinha.setText(l.getNomeLinha());
        holder.preco.setText(l.getPrecoLinha());
    }

    @Override
    public int getItemCount() {
        return linhasList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView nomeLinha;
        TextView preco;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeLinha = itemView.findViewById(R.id.textAdapterNome);
            preco = itemView.findViewById(R.id.textAdapterValor);
        }
    }
}
