package com.souzs.appmotoristatcc.acticity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.ConfiguracaoFireBase;
import com.souzs.appmotoristatcc.helper.MotoristaFireBase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivityMain extends AppCompatActivity {
    private FirebaseAuth auth;
    private GoogleSignInClient signInClient;
    private int RC_SIGN_IN = 1;
    private Button buttonLogin;
    private SweetAlertDialog pDialog;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getSupportActionBar().hide();

        buttonLogin = findViewById(R.id.buttonLogin);

        btnL();
    }
    private void btnL(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pDialog = new SweetAlertDialog(ActivityMain.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                signIn();
            }
        });
    }
    private void signIn(){
        Intent i = signInClient.getSignInIntent();

        startActivityForResult(i, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                request(account);


            }catch (ApiException e){
                request(null);
            }
        }
    }
    private void  request(GoogleSignInAccount acc){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                    boolean t = MotoristaFireBase.atualizarNomeLinha("");
                    if(t){
                        updateUsu(user);
                    }
                }
            }
        });
    }
    private void  updateUsu(FirebaseUser u){
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (signInAccount != null){
            if (u.getEmail().equals("caioeumdeus@gmail.com")){
                finish();
                abrirTelaLinha();

            }else {
                boolean b = MotoristaFireBase.atualizarNomeLinha("");
                if (b){
                    pDialog.dismissWithAnimation();
                    finish();
                    telaSl();
                }
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        auth = ConfiguracaoFireBase.getAutenticacao();
        verificarLogin();
    }
    private void verificarLogin(){
        if (auth.getCurrentUser() != null){
            if (auth.getCurrentUser().getEmail().equals("caioeumdeus@gmail.com")){
                finish();
                abrirTelaLinha();
            }else {
                startActivity(new Intent(this, TelaMapaActivity.class));
                finish();
            }
        }
    }

    private void abrirTelaLinha(){
        startActivity(new Intent(this, LinhaActivity.class));
    }
    private void telaSl(){
        startActivity(new Intent(this, SelectLinhaActivity.class));
    }
}
