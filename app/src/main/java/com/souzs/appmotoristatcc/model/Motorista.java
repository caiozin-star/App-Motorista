package com.souzs.appmotoristatcc.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;

public class Motorista {
    private String id;
    private String nomeIdentificacao;
    private String email;
    private String senha;

    private String lat;
    private String log;

    public Motorista() {
    }
    public  void salvarMotorista(){
        DatabaseReference reference = ConfiguracaoFireBase.getReference();
        DatabaseReference motorista = reference.child("motorista")
                                        .child(getId());
        motorista.setValue(this);
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeIdentificacao() {
        return nomeIdentificacao;
    }

    public void setNomeIdentificacao(String nomeIdentificacao) {
        this.nomeIdentificacao = nomeIdentificacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
