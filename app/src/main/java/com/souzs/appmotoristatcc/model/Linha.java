package com.souzs.appmotoristatcc.model;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;

public class Linha {
    private String id;

    private String nomeLinha;
    private String precoLinha;
    private DatabaseReference reference;

    public Linha() {
    }

    public  void salvarLinha(){
        reference = ConfiguracaoFireBase.getReference().push();
        DatabaseReference reference = ConfiguracaoFireBase.getReference();
        reference.child("linha").push().setValue(this);

    }
    public  void salvarLinhaDisponivel(Linha linha){
        DatabaseReference reference = ConfiguracaoFireBase.getReference();
        reference.child("linha_dis").child(linha.getId()).setValue(this);

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }

    public String getPrecoLinha() {
        return precoLinha;
    }

    public void setPrecoLinha(String precoLinha) {
        this.precoLinha = precoLinha;
    }
}
