package com.souzs.appmotoristatcc.acticity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;
import com.souzs.appmotoristatcc.model.Linha;
import com.souzs.appmotoristatcc.model.MotoristaLogado;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ConfgActivity extends AppCompatActivity {
    private TextView textViewT, textViewNUp, textViewPUo, textViewLNS, textViewTi;
    private RadioButton radioButtonV, radioButtonA, radioButtonVer;
    private SweetAlertDialog sweetAlertDialog, sweetAlertDialogLNI, sweetAlertDialogTL, sweetAlertDialogI;
    private Linha l;
    private FirebaseAuth auth;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confg);
        getSupportActionBar().hide();
        textViewT = findViewById(R.id.textViewTeste);
        textViewNUp = findViewById(R.id.textViewUpN);
        textViewPUo = findViewById(R.id.textViewUpP);
        textViewLNS = findViewById(R.id.textViewLNS);
        textViewTi = findViewById(R.id.textViewTI);

        button = findViewById(R.id.btnAtu);

        radioButtonV = findViewById(R.id.radioButtonV);
        radioButtonA = findViewById(R.id.radioButtonA);
        radioButtonVer = findViewById(R.id.radioButtonVer);

        auth = ConfiguracaoFireBase.getAutenticacao();

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialogLNI = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialogTL = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialogI = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);

        textViewLNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfgActivity.this, SelectLinhaActivity.class));
                finish();
            }
        });

        if (MotoristaFireBase.getNomeLinhaAtual().equals("")
        || MotoristaFireBase.getNomeLinhaAtual() == null){
            confgTextView();
        }else {
            textLinhaAtual();
        }
    }
    private void confgTextView(){
        textViewTi.setText("Linha não selecionada!");
        textViewLNS.setVisibility(View.VISIBLE);
        textViewPUo.setVisibility(View.INVISIBLE);
        textViewNUp.setVisibility(View.INVISIBLE);
        radioButtonVer.setVisibility(View.INVISIBLE);
        radioButtonV.setVisibility(View.INVISIBLE);
        radioButtonA.setVisibility(View.INVISIBLE);
        textViewT.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
    }
    public void radioButton(View view){
        String sttLinha = "";
        if (radioButtonV.isChecked()){
            sttLinha = "tranquilo";
        } else if (radioButtonA.isChecked()){
            sttLinha = "lento";
        }else if (radioButtonVer.isChecked()){
            sttLinha = "travado";
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String valueP = bundle.getString("keyS");
            if (valueP.equals("iniciada")){
                if (sttLinha != null && !sttLinha.equals("")){
                    sweetAlertDialog.setTitle("Deseja atualizar o status da linha?");
                    sweetAlertDialog.setContentText("Deseja atualizar o status para " + sttLinha + " ? ");
                    sweetAlertDialog.setConfirmText("Atualizar");
                    final String finalSttLinha = sttLinha;
                    final String finalSttLinha1 = sttLinha;
                    final String finalSttLinha2 = sttLinha;
                    final String finalSttLinha3 = sttLinha;
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            MotoristaLogado motoristaLogado = new MotoristaLogado();
                            motoristaLogado.atualizarStt(finalSttLinha2);
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                }
            }else {
                sweetAlertDialogLNI.setTitle("Linha não iniciada!");
                sweetAlertDialogLNI.setContentText("Para atualizar o status é necessário iniciar uma linha!");
                sweetAlertDialogLNI.setConfirmText("Entendi");
                sweetAlertDialogLNI.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialogLNI.show();
            }
        }

    }
    private void textLinhaAtual(){
        DatabaseReference ref = ConfiguracaoFireBase.getReference();
        DatabaseReference c = ref.child("linha");
        Query q = c.orderByChild("nomeLinha").equalTo(MotoristaFireBase.getNomeLinhaAtual());

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Linha linha = d.getValue(Linha.class);

                    textViewNUp.setText(linha.getNomeLinha());
                    textViewPUo.setText(linha.getPrecoLinha());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void trocarL(View view){
        sweetAlertDialogTL.setTitle("Atenção!");
        sweetAlertDialogTL.setContentText("Ao trocar de linha a seção atual será finalizada!");
        sweetAlertDialogTL.setConfirmText("Entendi");
        sweetAlertDialogTL.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                DatabaseReference ref = ConfiguracaoFireBase.getReference();
                final DatabaseReference consulta = ref.child("linha");
                Query q = consulta.orderByChild("nomeLinha").equalTo(
                        MotoristaFireBase.getNomeLinhaAtual()
                );

                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            l = ds.getValue(Linha.class);
                            l.setId(ds.getKey());
                            l.salvarLinhaDisponivel(l);
                            MotoristaLogado motoristaLogado = new MotoristaLogado();
                            motoristaLogado.setSttLinha(motoristaLogado.STATUS_OFF);
                            motoristaLogado.deletarMotoristaLogado();
                           boolean t = MotoristaFireBase.atualizarNomeLinha("");
                            if (t){
                                finish();
                                startActivity(new Intent(ConfgActivity.this, SelectLinhaActivity.class));
                            }
                            sweetAlertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        sweetAlertDialogTL.show();
    }
    public void sair(View view){

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String valueP = bundle.getString("keyS");
            if (valueP.equals("iniciada")){
                sweetAlertDialogI.setTitle("Linha iniciada!");
                sweetAlertDialogI.setContentText("Finalize a linha atual para efetuar o LogOut!");
                sweetAlertDialogI.setConfirmText("Entendi");
                sweetAlertDialogI.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialogI.show();
            } else {
                boolean p = MotoristaFireBase.atualizarNomeLinha("");
                if (p){
                    auth.signOut();
                    finish();
                    startActivity(new Intent(ConfgActivity.this, ActivityMain.class));
                }
            }
        }
    }
}
