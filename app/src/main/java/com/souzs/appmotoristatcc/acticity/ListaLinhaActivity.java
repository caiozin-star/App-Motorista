package com.souzs.appmotoristatcc.acticity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.adapter.AdapterLinhas;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.model.Linha;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListaLinhaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewLinhas;
    private AdapterLinhas adapterLinhas;
    private List<Linha> linhas = new ArrayList<>();
    private DatabaseReference ref = ConfiguracaoFireBase.getReference();

    private SweetAlertDialog alertDialogDelete;
    private Linha linhaD;
    private Linha linhaA;
    private TextView textViewInfo;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutFundo;
    private EditText editTextLinha, editTextPreco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_linha);
        getSupportActionBar().setTitle("Linhas");


        textViewInfo = findViewById(R.id.textViewInfo);
        recyclerViewLinhas = findViewById(R.id.recyclerLinhas);
        linearLayout = findViewById(R.id.linearLayoutUp);
        linearLayoutFundo = findViewById(R.id.linearLayoutFundo);
        linearLayoutFundo.setVisibility(View.INVISIBLE);
        editTextLinha = findViewById(R.id.editNomeLinhaCad);
        editTextPreco = findViewById(R.id.editPrecoLinha);

        linearLayoutFundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutFundo.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
            }
        });

        adapterLinhas = new AdapterLinhas(linhas, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewLinhas.setLayoutManager(layoutManager);
        recyclerViewLinhas.setHasFixedSize(true);
        recyclerViewLinhas.setAdapter(adapterLinhas);

    }
    public void recuLinhas(){
        final DatabaseReference consulta = ref.child("linha");

        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0){
                    textViewInfo.setVisibility(View.GONE);
                }else {
                    textViewInfo.setVisibility(View.VISIBLE);
                }
                linhas.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Linha l = ds.getValue(Linha.class);
                    l.setId(ds.getKey());
                    linhas.add(l);
                }
                adapterLinhas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void swipe(){
        ItemTouchHelper.Callback iTpuch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int flags = ItemTouchHelper.ACTION_STATE_IDLE;
                int sipe = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(flags,sipe);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            if (direction == 16){
                excluirLinha(viewHolder);
            }else if (direction == 32){
                updateLinha(viewHolder);
            }
            }
        };
        new ItemTouchHelper(iTpuch).attachToRecyclerView(recyclerViewLinhas);
    }
    private void updateLinha(RecyclerView.ViewHolder viewHolder){
        linearLayoutFundo.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

        int p = viewHolder.getAdapterPosition();

        linhaA = linhas.get(p);

        editTextLinha.setText(linhaA.getNomeLinha());
        editTextPreco.setText(linhaA.getPrecoLinha());
        adapterLinhas.notifyDataSetChanged();
    }
    public void bUpdateLinha(View view){
        String campoLinhaUp = editTextLinha.getText().toString();
        String campoPrecoUp = editTextPreco.getText().toString();

        linhaA.setNomeLinha(campoLinhaUp);
        linhaA.setPrecoLinha(campoPrecoUp);

        DatabaseReference ref = ConfiguracaoFireBase.getReference();

        ref.child("linha").child(linhaA.getId()).setValue(linhaA);

        ref.child("linha_dis").child(linhaA.getId()).setValue(linhaA);

        final SweetAlertDialog alertDialogSucesso = new SweetAlertDialog(ListaLinhaActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        alertDialogSucesso.setTitleText("Sucesso!");
        alertDialogSucesso.setContentText("Linha atualizada com sucesso com sucesso");
        alertDialogSucesso.show();
        alertDialogSucesso.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialogSucesso.dismissWithAnimation();
                linearLayout.setVisibility(View.INVISIBLE);
                linearLayoutFundo.setVisibility(View.INVISIBLE);
            }
        });


    }
    private void excluirLinha(final RecyclerView.ViewHolder viewHolder){
        alertDialogDelete = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialogDelete.setTitleText("Deseja apagar essa linha?");
        alertDialogDelete.setContentText("Vocês está preste a deletar esta linha ");
        alertDialogDelete.setConfirmText("Sim");
        alertDialogDelete.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                    int p = viewHolder.getAdapterPosition();

                    linhaD = linhas.get(p);

                        final DatabaseReference consulta = ref.child("linha");
                        final DatabaseReference consultaD = ref.child("linha_dis");

                        consulta.child(linhaD.getId()).removeValue();
                        consultaD.child(linhaD.getId()).removeValue();
                        adapterLinhas.notifyItemRemoved(p);

                        final SweetAlertDialog alertDialogSucesso = new SweetAlertDialog(ListaLinhaActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        alertDialogSucesso.setTitleText("Sucesso!");
                        alertDialogSucesso.setContentText("Linha apadaga com sucesso");
                        alertDialogSucesso.show();
                        alertDialogSucesso.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                alertDialogDelete.dismissWithAnimation();
                                alertDialogSucesso.dismissWithAnimation();
                            }
                        });



                    }
                });
        alertDialogDelete.setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        adapterLinhas.notifyDataSetChanged();
                        alertDialogDelete.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuLinhas();
        swipe();
    }
}
