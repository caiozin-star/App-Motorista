package com.souzs.appmotoristatcc.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFireBase {
    private static DatabaseReference reference;
    private static FirebaseAuth autenticacao;

    public static FirebaseAuth getAutenticacao(){
    if (autenticacao == null){
        autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static DatabaseReference getReference(){
        if (reference == null){
            reference = FirebaseDatabase.getInstance().getReference();
        }
        return reference;
    }
}
