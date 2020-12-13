package com.example.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import Util.ActionBar;
import Util.BD;

import com.example.checkin.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener {
    //strings para spinner e autocomplete respectivamente
    private static final List<String> Categorias = new ArrayList<String>();
    private static final List<String> Locais = new ArrayList<String>();
    //dados cadastrais
    String CadastroLocal = null;
    String CadastroCategoria = null;
    double Latitude = 0;
    double Longitude = 0;
    //pegar local
    public LocationManager lm;
    public Criteria criteria;
    public String provider;
    private final int LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BD.getInstance().abrir();
        //configura actionbar
        Util.ActionBar.configureActionBar(this, (float)20, 0);
        getSupportActionBar().setTitle("Checkin Locais");

        //conigura autocomplete
        ConfiguraAtuoComplete();
        //configura spinner
        Cursor cCatg = BD.getInstance().buscar("Categoria ctg",
                new String[]{"ctg.nome nome"},
                "",
                "");
        while (cCatg.moveToNext()) {
            int coluna = cCatg.getColumnIndex("nome");
            Categorias.add(cCatg.getString(coluna));
        }
        cCatg.close();

        Spinner combo = (Spinner) findViewById(R.id.SpinnerCategoria);
        combo.setOnItemSelectedListener(this); //configura método de seleção

        ArrayAdapter<String> adaptador2 =
                new ArrayAdapter<String>(this, R.layout.spinner_item, Categorias);
        adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        combo.setAdapter(adaptador2);

        //posicao
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        PackageManager packageManager = getPackageManager();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION);

        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if (hasGPS) {
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            Log.d("GPS", "TEM");
        } else {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            Log.d("GPS", "NAO TEM");
        }
        Localiza();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ConfiguraAtuoComplete();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        CadastroCategoria = Categorias.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permissão concedida", 5000);
                    Localiza();
                } else {
                    Toast.makeText(this, "Permissão Necessária para saber seu local", Toast.LENGTH_LONG).show();
                    Log.d("PERMISSAO", "NAO DEIXOUUUUUUU");
                }
            }
        }
    }

    public void Localiza() {
        provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            Log.d("Provedor", "Nenhum provedor encontrado");
        } else {
            lm.requestLocationUpdates(provider, 100, 1, this);
            lm.removeUpdates(listener);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        Log.i("tag", "onLocationChanged: "+Latitude);
        TextView v = (TextView) findViewById(R.id.textViewLatitude);
        TextView v2 = (TextView) findViewById(R.id.textViewLongitude);
        v.setText("Latitude: " + String.valueOf(Latitude));
        v2.setText("Longitude: "+String.valueOf(Longitude));
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(0, 1, 1, "Mapa de Check-in");
        item = menu.add(0, 2, 2, "Gestão de Check-in");
        item = menu.add(0, 3, 3, "Lugares mais visitados");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                Intent it = new Intent(this, TelaMapa.class);
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(it);
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
        }
        return false;
    }

    public void Checkin(View view) {
        AutoCompleteTextView loco = (AutoCompleteTextView) findViewById(R.id.locais);
        CharSequence locol = loco.getText();
        CadastroLocal = locol.toString();

        if (!CadastroLocal.equals(null)&& !CadastroCategoria.equals(null)&& Latitude != 0 && Longitude != 0){
            Cursor cLocal = BD.getInstance().buscar("Checkin chkn",
                    new String[]{"chkn.Local Local"},
                    "chkn.Local = '" + locol + "'",
                    "");
            if (cLocal.getCount()==0){ //cadastro novo
                ContentValues valores = new ContentValues();
                valores.put("Local",CadastroLocal);
                valores.put("qtdVisitas",1);
                valores.put("cat",CadastroCategoria);
                valores.put("latitude",Latitude);
                valores.put("longitude",Longitude);
                BD.getInstance().inserir("Checkin",valores);
                ConfiguraAtuoComplete();
            } else {//atualiza qtd de visitas no banco
                int NewQtd = 0;
                Cursor cLocalqtd = BD.getInstance().buscar("Checkin chkn",
                        new String[]{"chkn.Local Local", "chkn.qtdVisitas qtdVisitas"},
                        "chkn.Local = '" + CadastroLocal + "'",
                        "");
                while (cLocalqtd.moveToNext()) {
                    int coluna = cLocalqtd.getColumnIndex("qtdVisitas");
                    NewQtd = cLocalqtd.getInt(coluna);
                }
                cLocalqtd.close();
                ContentValues valores = new ContentValues();
                valores.put("qtdVisitas",NewQtd+1);
                BD.getInstance().atualizar("Checkin",valores,"Local = '"+CadastroLocal+"'");
            }
            loco.setText("");
            cLocal.close();
        }else Toast.makeText(this, "Dados incompletos", Toast.LENGTH_SHORT).show();
    }

    public void ConfiguraAtuoComplete (){
        Locais.clear();
        Cursor cLocals = BD.getInstance().buscar("Checkin chkin",
                new String[]{"chkin.Local Local"},
                "",
                "");
        if (cLocals.getCount()>0) {
            while (cLocals.moveToNext()) {
                int coluna = cLocals.getColumnIndex("Local");
                Locais.add(cLocals.getString(coluna));
            }
        }
        cLocals.close();

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, Locais);
        AutoCompleteTextView locals = (AutoCompleteTextView) findViewById(R.id.locais);
        locals.setAdapter(adaptador);
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.d("Estado atual de ", getClass().getName() + "= .onDestroy");
        BD.getInstance().fechar();
    }

}