package com.souzs.appmotoristatcc.slider_personalizados;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.souzs.appmotoristatcc.R;

import io.github.dreierf.materialintroscreen.SlideFragment;

public class SliderCad extends SlideFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.intro_4_login, container, false);
        return v;
    }

    @Override
    public boolean canMoveFurther() {
        return false;
    }

    @Override
    public int backgroundColor() {
        return R.color.color_background__slider;
    }

    @Override
    public int buttonsColor() {
        return R.color.color_button_slider;
    }
}
