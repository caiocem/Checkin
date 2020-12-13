package com.example.Controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.checkin.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Util.BD;

public class TelaMapa extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    public Marker marcador = null;
    public String MarkNome = null;
    public String MarkCtg = null;
    public double qtdvst = 0;
    public double lat = 0;
    public double longi = 0;
    LatLng loco = new LatLng(0,0);

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.telamapa);
        Log.d("Estado atual de ", getClass().getName() + "= .onCreate");

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa)).getMapAsync(this);

        //configura actionbar
        Util.ActionBar.configureActionBar(this, (float)20, 0);
        getSupportActionBar().setTitle("Mapa Checkin");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(0, 1, 1, "Voltar");
        item = menu.add(0, 2, 2, "Gestão de Check-in");
        item = menu.add(0, 3, 3, "Lugares mais visitados");
        SubMenu subMenu = menu.addSubMenu(0,4,4,"Tipos de Mapa");
        item = subMenu.add(0, 5, 5, "Mapa Normal");
        item = subMenu.add(0, 6, 6, "Mapa Híbrido");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                finish();
                break;
            }
            case 2: {
                Intent it = new Intent(this, TelaGestao.class);
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(it);
                break;
            }
            case 3: {
                Intent it = new Intent(this, TelaVisitas.class);
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(it);
                break;
            }
            case 5: {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            }
            case 6: {
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            }
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Cursor cMarcas = BD.getInstance().buscar("Checkin chkn",
                new String[]{"chkn.Local Local", "chkn.qtdVisitas qtdVisitas", "chkn.cat cat", "chkn.latitude latitude", "chkn.longitude longitude", },
                "",
                "");
        if (cMarcas.getCount()>0) {
            int coluna = 0;
            while (cMarcas.moveToNext()) {
                coluna = cMarcas.getColumnIndex("Local");
                MarkNome = cMarcas.getString(coluna);
                coluna = cMarcas.getColumnIndex("qtdVisitas");
                qtdvst = cMarcas.getDouble(coluna);
                coluna = cMarcas.getColumnIndex("cat");
                MarkCtg = cMarcas.getString(coluna);
                coluna = cMarcas.getColumnIndex("latitude");
                lat = cMarcas.getDouble(coluna);
                coluna = cMarcas.getColumnIndex("longitude");
                longi = cMarcas.getDouble(coluna);
                loco = new LatLng(lat, longi);
                map.addMarker(new MarkerOptions()
                        .position(loco)
                        .title(MarkNome)
                        .snippet("Categoria: "+MarkCtg+" Visitas: "+(int)qtdvst)
                );
            }
        }
        cMarcas.close();
    }
}
