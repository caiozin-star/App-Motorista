package com.souzs.appmotoristatcc.acticity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.Permissoes;
import com.souzs.appmotoristatcc.model.Linha;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LinhaActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText editTextNome, editTextPreco;
    private SweetAlertDialog alertDialog;
    private FloatingActionButton fbaSair, fbaP, fbaVisu;
    private boolean controle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linha);
        getSupportActionBar().hide();

        auth = ConfiguracaoFireBase.getAutenticacao();

        fbaVisu = findViewById(R.id.fabVisuLinha);
        fbaP = findViewById(R.id.fbaP);
        fbaSair = findViewById(R.id.fabSair);
        editTextNome = findViewById(R.id.editNomeLinhaCad);
        editTextPreco = findViewById(R.id.editPrecoLinha);

        floatButtons();

    }
    private void floatButtons(){

        fbaP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controle){
                    fbaSair.setVisibility(View.VISIBLE);
                    fbaVisu.setVisibility(View.VISIBLE);
                    controle = false;
                }else {
                    controle = true;
                    fbaSair.setVisibility(View.GONE);
                    fbaVisu.setVisibility(View.GONE);
                }
            }
        });

        fbaSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
                startActivity(new Intent(LinhaActivity.this, ActivityMain.class));
            }
        });
        fbaVisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LinhaActivity.this, ListaLinhaActivity.class));
            }
        });
    }
    public void validarCampos(View view){
        String campo_Nome = editTextNome.getText().toString();
        String campo_Preco = editTextPreco.getText().toString();

        if (!campo_Nome.isEmpty()){
            if (!campo_Preco.isEmpty()){

                Linha l = new Linha();
                l.setNomeLinha(campo_Nome);
                l.setPrecoLinha(campo_Preco);

                l.salvarLinha();
                salvaLinhaD();

                editTextNome.setText("");
                editTextPreco.setText("");

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sucesso ao cadastrar linha!")
                        .setContentText("Linha cadastrada com sucesso.!")
                        .show();


            }else {
                String aviso2 ="Preencha o preço da linha para finalizar o cadastro!" ;
                alerta(aviso2);
            }

        }else {
            String aviso1 ="Preencha os dados para salvar uma nova linha!" ;
            alerta(aviso1);
        }
    }
    private void salvaLinhaD(){
        DatabaseReference ref = ConfiguracaoFireBase.getReference();
        final DatabaseReference consulta = ref.child("linha");

        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Linha l = ds.getValue(Linha.class);
                    l.setId(ds.getKey());


                    l.salvarLinhaDisponivel(l);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void alerta(String aviso){
        alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);

        alertDialog.setTitleText("Oops..");
        alertDialog.setContentText(aviso);
        alertDialog.setConfirmButtonBackgroundColor(R.color.colorAccent);
        alertDialog.show();
    }

}
