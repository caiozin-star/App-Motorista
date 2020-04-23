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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;
import com.souzs.appmotoristatcc.model.Motorista;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoEmail, campoIdentidadeLinha, campoSenha;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().hide();

        campoEmail = findViewById(R.id.editEmailCad);
        campoIdentidadeLinha = findViewById(R.id.editIdentificacaoLinha);
        campoSenha = findViewById(R.id.editSenhaCad);
    }
    public void validarCampos(View view){

        String email = campoEmail.getText().toString();
        String identificacao = campoIdentidadeLinha.getText().toString();
        String senha = campoSenha.getText().toString();

        if (!email.isEmpty()){
            if (!identificacao.isEmpty()){
                if (!senha.isEmpty()){
                    Motorista motorista = new Motorista();

                    motorista.setEmail(email);
                    motorista.setNomeIdentificacao(identificacao);
                    motorista.setSenha(senha);

                    cadastrarMotorista(motorista);
                }else {
                    Toast.makeText(this, "Ops, Preencha o campo Senha para finalizar seu cadastro!", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "Ops, Preencha o campo de Identificação da linha para prosseguir com " +
                        "o cadastro!", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Ops, Preencha os campos para realizar seu cadastro!", Toast.LENGTH_SHORT).show();
        }
    }
    public void cadastrarMotorista(final Motorista m){
        auth = ConfiguracaoFireBase.getAutenticacao();

        auth.createUserWithEmailAndPassword(
                m.getEmail(), m.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    try {
                        String idMotorista = task.getResult().getUser().getUid();
                        m.setId(idMotorista);
                        m.salvarMotorista();

                        MotoristaFireBase.atualizarNomeLinha(m.getNomeIdentificacao());

                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    String erro = "";
                    try {
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Está conta já está cadastrada";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário! " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
