package com.souzs.appmotoristatcc.acticity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;
import com.souzs.appmotoristatcc.helper.Permissoes;
import com.souzs.appmotoristatcc.model.Motorista;
import com.souzs.appmotoristatcc.model.MotoristaLogado;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TelaMapaActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng local;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private MotoristaLogado motoristaLogado;

    private Button buttonLinha;
    private Boolean finalizar_secao = false;
    private String[] p = new String[]{
      Manifest.permission.ACCESS_FINE_LOCATION
    };
    private SweetAlertDialog alertDialog, alertDialogSL, alertDialogLNS, alertDialogIS;

    private String atuN = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mapa);

        alertDialogSL = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialogLNS = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialogIS = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);

        buttonLinha = findViewById(R.id.buttonIniciarLinha);
        reference = ConfiguracaoFireBase.getReference();


        verificaP();


    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = ConfiguracaoFireBase.getAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (MotoristaFireBase.getNomeLinhaAtual().equals("") ||
        MotoristaFireBase.getNomeLinhaAtual() == null){

            toolbar.setTitle("Carregando...");
            setSupportActionBar(toolbar);

        }else {
            toolbar.setTitle(auth.getCurrentUser().getDisplayName());
            setSupportActionBar(toolbar);
        }

    }

    private void  verificaP(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            alertaP();

        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            verificaSttLinha();
        }
    }


    private void alertaP(){
        alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);

        alertDialog.setTitleText("Oops..");
        alertDialog.setContentText("Para prosseguir é necessário aceitar as permissões!");
        alertDialog.setConfirmButtonBackgroundColor(R.color.colorAccent);
        alertDialog.setConfirmText("Aceita-las!");
        alertDialog.setCancelable(false);
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Permissoes.validarPermissoes(p, TelaMapaActivity.this, 1);

            }
        });
        alertDialog.show();
    }
    private void  verificaSttLinha(){
        Motorista mL = MotoristaFireBase.getDadosMotoristalLogado();

        final DatabaseReference motoristaL = reference.child("motorista_logado");
        Query q = motoristaL.orderByChild("idMLogado").equalTo(mL.getId());

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    motoristaLogado = ds.getValue(MotoristaLogado.class);

                    Log.d("resultado", "onDataChange: " + motoristaLogado.getIdMLogado());
                    finalizar_secao = false;
                    switch (motoristaLogado.getSttLinha()){
                        case MotoristaLogado.STATUS_TRANQUILO:
                            confg();
                            break;
                        case MotoristaLogado.STATUS_LENTO:
                            confg();
                            break;
                        case MotoristaLogado.STATUS_TRAVADO:
                            confg();
                            break;
                        case MotoristaLogado.STATUS_OFF:
                            buttonLinha.setText("Iniciar Seção");
                            atuN = "finalizada";
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void confg(){
        buttonLinha.setText("Finalizar Linha");
        finalizar_secao = true;
        atuN = "iniciada";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int pR: grantResults){
            if (pR == PackageManager.PERMISSION_GRANTED){

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                verificaSttLinha();
                alertDialog.dismiss();
            } else if (pR == PackageManager.PERMISSION_DENIED){

            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        recuLocalizacao();

    }
    public void iniciarMonitoramento(View view){
        if (finalizar_secao){
            alertDialogSL.setTitleText("Deseja finalizar essa seção?");
            alertDialogSL.setContentText("Finalizar seção referente a linha: " +MotoristaFireBase.getNomeLinhaAtual() );
            alertDialogSL.setConfirmText("Finalizar");
            alertDialogSL.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    motoristaLogado.setSttLinha(MotoristaLogado.STATUS_OFF);
                    motoristaLogado.deletarMotoristaLogado();
                    finalizar_secao = false;
                    atuN = "finalizada";
                    buttonLinha.setText("Iniciar seção");
                    alertDialogSL.dismiss();
                }
            });
            alertDialogSL.show();

        }else {
            if (MotoristaFireBase.getNomeLinhaAtual().equals("") ||
            MotoristaFireBase.getNomeLinhaAtual() == null){
                alertDialogLNS.setTitleText("Linha não selecionada!");
                alertDialogLNS.setContentText("Para iniciar uma linha é necessário escolher tal!");
                alertDialogLNS.setConfirmText("Escolher Linha");
                alertDialogLNS.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(TelaMapaActivity.this, SelectLinhaActivity.class));
                        finish();
                    }
                });
                alertDialogLNS.show();

            }else {
                alertDialogIS.setTitleText("Iniciar seção?");
                alertDialogIS.setContentText("Inciar seção referente a linha: " +MotoristaFireBase.getNomeLinhaAtual() );
                alertDialogIS.setConfirmText("Inciar");
                alertDialogIS.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        finalizar_secao = true;
                        atuN = "iniciada";
                        buttonLinha.setText("Finalizar seção");

                        MotoristaLogado motoristaLogado = new MotoristaLogado();

                        Motorista motoristaL = MotoristaFireBase.getDadosMotoristalLogado();
                        motoristaLogado.setLat(String.valueOf(local.latitude));
                        motoristaLogado.setLon(String.valueOf(local.longitude));
                        motoristaLogado.setIdMLogado(motoristaL.getId());
                        motoristaLogado.setNomeLinha(motoristaL.getNomeIdentificacao());
                        motoristaLogado.setSttLinha(motoristaLogado.STATUS_TRANQUILO);
                        motoristaLogado.salvarMotoristaLogado();
                        alertDialogIS.dismiss();
                    }
                });
                alertDialogIS.show();
            }
        }

    }

    public void recuLocalizacao() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double lat = location.getLatitude();
                Double lon = location.getLongitude();
                local = new LatLng(lat, lon);

                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(local)
                        .title("Menu Local")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario)));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 18));

               if (finalizar_secao){
                       motoristaLogado.setLat(String.valueOf(lat));
                       motoristaLogado.setLon(String.valueOf(lon));

                       motoristaLogado.atualizaLocalizacao();

               }

            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    10,
                    locationListener
            );
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_confg){
            Intent i = new Intent(TelaMapaActivity.this, ConfgActivity.class);
            i.putExtra("keyS", atuN);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
