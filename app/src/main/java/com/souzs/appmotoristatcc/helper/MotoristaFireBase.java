package com.souzs.appmotoristatcc.helper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.souzs.appmotoristatcc.model.Motorista;

public class MotoristaFireBase {

    public static FirebaseUser getMotoristaAtual(){
        FirebaseAuth motorista = ConfiguracaoFireBase.getAutenticacao();

        return  motorista.getCurrentUser();
    }

    public static String getNomeLinhaAtual(){
        FirebaseAuth motorista = ConfiguracaoFireBase.getAutenticacao();

        return  motorista.getCurrentUser().getDisplayName();
    }
    public static Motorista getDadosMotoristalLogado(){
        FirebaseUser user = getMotoristaAtual();

        Motorista motorista = new Motorista();

        motorista.setId(user.getUid());
        motorista.setNomeIdentificacao(user.getDisplayName());

        return  motorista;
    }

    public static Boolean atualizarNomeLinha(String nomeLinha){
        try {
            FirebaseUser m = getMotoristaAtual();
            UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nomeLinha)
                    .build();
            m.updateProfile(perfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
