package Util;

import androidx.appcompat.app.AppCompatActivity;

public class ActionBar {
    public static void configureActionBar(AppCompatActivity ctx, int iconHome, float elevation){
        ctx.getSupportActionBar().setHomeAsUpIndicator(iconHome); //Ex: botão close
        ctx.getSupportActionBar().setDisplayHomeAsUpEnabled(true); //ativa botão. Ex: close
        ctx.getSupportActionBar().setElevation(elevation);
    }
    public static void configureActionBar(AppCompatActivity ctx, float elevation, int voltar){
        if(voltar>0)
            ctx.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // botão voltar
        else
            ctx.getSupportActionBar().setDisplayHomeAsUpEnabled(false); // botão voltar
        ctx.getSupportActionBar().setElevation(elevation);
    }
    public static void configureActionBar(AppCompatActivity ctx, int iconHome){
        ctx.getSupportActionBar().setHomeAsUpIndicator(iconHome); //Ex: botão close
        ctx.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ativa botão. Ex: close
    }
    public static void configureActionBar(AppCompatActivity ctx){
        ctx.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // botão voltar
    }
}