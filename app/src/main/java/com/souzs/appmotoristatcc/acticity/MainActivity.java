package com.souzs.appmotoristatcc.acticity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.FloatRange;

import com.souzs.appmotoristatcc.R;
import com.souzs.appmotoristatcc.helper.SliderCad;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;
import io.github.dreierf.materialintroscreen.animations.IViewTranslation;

public class MainActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        exibirSlider();
    }
    public void abrirTelaCad(View view){
        Toast.makeText(this, "Clicou botão cad", Toast.LENGTH_SHORT).show();
    }
    private void exibirSlider(){
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorTeste)
                .buttonsColor(R.color.colorTeste2)
                .image(R.drawable.slider_motorista_icone)
                .title("Olá, bem vindo(a) a versão motorista do nosso App \n")
                .description("Nesse app você irá fornecer a localização do ônibus para vários passageiros," +
                        " contribindo e facilitando a vida deles ;)")
                .build());
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorTeste)
                        .buttonsColor(R.color.colorTeste2)
                        .image(R.drawable.slide_icone_location)
                        .possiblePermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
                        .neededPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION})
                        .title("Aceite a permissão para prosseguir! \n")
                        .description("Fique tranquilo, a permissão é de suma importancia para o app, iremos \n usa-lá com " +
                                "consciência ;)")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Á permissão já foi concedida, prossiga para a proxima tela");
                    }
                }, "Permissão concedida ;)"));

        addSlide(new SliderCad());
    }

}

