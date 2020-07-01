package com.souzs.appmotoristatcc.model;


import java.io.Serializable;

public class Motorista implements Serializable {
    private String id;
    private String nomeIdentificacao;
    private String email;
    private String sL;

    public Motorista() {
    }

    public String getsL() {
        return sL;
    }

    public void setsL(String sL) {
        this.sL = sL;
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
}
