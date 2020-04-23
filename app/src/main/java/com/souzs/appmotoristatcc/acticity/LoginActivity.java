package com.souzs.appmotoristatcc.acticity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.model.Motorista;

public class LoginActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        campoEmail = findViewById(R.id.editEmailLogin);
        campoSenha = findViewById(R.id.editSenhaLogin);
    }

    public void validarCampos(View view){
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if (!email.isEmpty()){
            if (!senha.isEmpty()){
                Motorista m = new Motorista();
                m.setEmail(email);
                m.setSenha(senha);

                logarMotorista( m );

            }else {
                Toast.makeText(this, "Ops, Preencha o campo Senha para validar seu Login!", Toast.LENGTH_SHORT).show();
            }
        }else {
                Toast.makeText(this, "Ops, Preencha os dados para validar seu Login!", Toast.LENGTH_SHORT).show();
        }
    }
    public void logarMotorista(Motorista motorista){
        auth = ConfiguracaoFireBase.getAutenticacao();

        auth.signInWithEmailAndPassword(
                motorista.getEmail(), motorista.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
                }else {
                    String erro = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        erro = "Digite um e-mail cadastrado!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail e senha não correspondem a um usuário cadastrado!";
                    }
                    catch (Exception e){
                        erro = "Erro ao cadastrar uauário! " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
