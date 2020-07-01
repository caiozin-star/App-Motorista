package com.souzs.appmotoristatcc.acticity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.adapter.AdapterSelectL;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;
import com.souzs.appmotoristatcc.helper.RecyclerItemClickListener;
import com.souzs.appmotoristatcc.model.Linha;
import com.souzs.appmotoristatcc.model.Motorista;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectLinhaActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdapterSelectL adapterSelectL;
    private List<Linha> linhasS = new ArrayList<>();
    private SweetAlertDialog alertDialogL;
    private Motorista motorista= new Motorista();
    private DatabaseReference ref;
    private ProgressBar progressBar;
    private  Linha l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_linha);
        getSupportActionBar().hide();
        alertDialogL = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        progressBar = findViewById(R.id.progressBarC);
        confgRecylerV();

    }

    private void confgRecylerV(){

        recyclerView = findViewById(R.id.recyclerViewLinhasS);

        adapterSelectL = new AdapterSelectL(linhasS, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterSelectL);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                 l = linhasS.get(position);


                                alertDialogL.setTitleText("Deseja selecionar esta linha?");
                                alertDialogL.setContentText("Linha selecionada: " +l.getNomeLinha() );
                                alertDialogL.setConfirmText("Selecionar");
                                alertDialogL.setCancelable(false);
                                alertDialogL.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        motorista.setsL(l.getNomeLinha());
                                        boolean t = MotoristaFireBase.atualizarNomeLinha(l.getNomeLinha());
                                        if (t) {
                                            final DatabaseReference consultaD = ref.child("linha_dis");
                                            consultaD.child(l.getId()).removeValue();
                                            Intent i = new Intent(SelectLinhaActivity.this, TelaMapaActivity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                        alertDialogL.dismiss();

                                    }
                                });
                                alertDialogL.setCancelButton("Não", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        alertDialogL.dismissWithAnimation();
                                    }
                                });
                                alertDialogL.show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }


    private void recuLinha(){
        DatabaseReference consulta = ref.child("linha_dis");
        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0){
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                linhasS.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Linha l = ds.getValue(Linha.class);
                    linhasS.add(l);

                }
                adapterSelectL.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        ref = ConfiguracaoFireBase.getReference();
        recuLinha();

    }
}
