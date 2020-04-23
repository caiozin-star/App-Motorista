package com.souzs.appmotoristatcc.model;

import com.google.firebase.database.DatabaseReference;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;

public class MotoristaLogado {
    private String idMLogado;
    private String nomeLinha;

    private String lat;
    private String lon;

    public MotoristaLogado() {
    }
    public void salvarMotoristaLogado(){
        DatabaseReference reference = ConfiguracaoFireBase.getReference();
        DatabaseReference logado = reference.child("motorista_logado");
        

        logado.child(idMLogado).setValue(this);

    }

    public String getIdMLogado() {
        return idMLogado;
    }

    public void setIdMLogado(String idMLogado) {
        this.idMLogado = idMLogado;
    }

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
