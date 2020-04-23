package com.souzs.appmotoristatcc.acticity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;
import com.souzs.appmotoristatcc.model.Motorista;
import com.souzs.appmotoristatcc.model.MotoristaLogado;

public class TelaMapaActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng local;

    private FirebaseAuth auth;

    private Button buttonLinha;
    private Boolean stt_secao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mapa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(MotoristaFireBase.getNomeLinhaAtual());
        setSupportActionBar(toolbar);

        buttonLinha = findViewById(R.id.buttonIniciarLinha);
        stt_secao = false;

        auth = ConfiguracaoFireBase.getAutenticacao();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        MotoristaLogado motoristaLogado = new MotoristaLogado();

        Motorista motoristaL = MotoristaFireBase.getDadosMotoristalLogado();
        motoristaLogado.setLat(String.valueOf(local.latitude));
        motoristaLogado.setLon(String.valueOf(local.longitude));
        motoristaLogado.setIdMLogado(motoristaL.getId());
        motoristaLogado.setNomeLinha(motoristaL.getNomeIdentificacao());
        motoristaLogado.salvarMotoristaLogado();

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
            Toast.makeText(this, "Exibir tela para ediatdar dados", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.menu_sair){
                auth.signOut();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
