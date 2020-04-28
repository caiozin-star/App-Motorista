package com.souzs.appmotoristatcc.model;

import com.google.firebase.database.DatabaseReference;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;

import java.util.HashMap;
import java.util.Map;

public class MotoristaLogado {
    private String idMLogado;
    private String nomeLinha;
    private String sttLinha;

    private String lat;
    private String lon;


    public static final String STATUS_ONLINE = "online";
    public static final String STATUS_OFF = "offline";

    public MotoristaLogado() {
    }
    public void salvarMotoristaLogado(){
        DatabaseReference reference = ConfiguracaoFireBase.getReference();
        DatabaseReference logado = reference.child("motorista_logado");


        logado.child(idMLogado).setValue(this);

    }
    public void deletarMotoristaLogado(){
        String idL = MotoristaFireBase.getIdMl();

        DatabaseReference reference = ConfiguracaoFireBase.getReference();
        reference.child("motorista_logado").child(idL).removeValue();
    }
    public void atualizaLocalizacao(){
        Motorista mL = MotoristaFireBase.getDadosMotoristalLogado();
        DatabaseReference reference = ConfiguracaoFireBase.getReference();
        DatabaseReference upLoca = reference.child("motorista_logado");

        DatabaseReference atualizaL = upLoca.child(mL.getId());

        Map obj = new HashMap();
        obj.put("lat", getLat());
        obj.put("lon", getLon());

        atualizaL.updateChildren(obj);
    }

    public String getSttLinha() {
        return sttLinha;
    }

    public void setSttLinha(String sttLinha) {
        this.sttLinha = sttLinha;
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
