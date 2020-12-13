package com.example.Controller;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.checkin.R;


import Util.BD;

public class TelaVisitas extends AppCompatActivity {

    public double qtdvst = 0;
    public Integer visits = 0;
    public String ListaNome = null;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.telavisitas);

        //configura actionbar
        Util.ActionBar.configureActionBar(this, (float) 20, 0);
        getSupportActionBar().setTitle("Relatório");

        //Custom layout
        LinearLayout layoutNome = (LinearLayout) findViewById(R.id.layoutNomes);
        LinearLayout layoutVisita = (LinearLayout) findViewById(R.id.layoutVisitas);
        Cursor cLayouts = BD.getInstance().buscar("Checkin chkn",
                new String[]{"chkn.Local Local", "chkn.qtdVisitas qtdVisitas"},
                "",
                "");
        if (cLayouts.getCount()>0) {
            int coluna = 0;
            while (cLayouts.moveToNext()) {
                coluna = cLayouts.getColumnIndex("Local");
                ListaNome = cLayouts.getString(coluna);
                coluna = cLayouts.getColumnIndex("qtdVisitas");
                qtdvst = cLayouts.getDouble(coluna);
                visits = (int) qtdvst;
                TextView text = new TextView(this);
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setText(ListaNome);
                text.setTextColor(Color.parseColor("#949494"));
                text.setTextSize(22);
                layoutNome.addView(text);
                TextView text2 = new TextView(this);
                text2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                text2.setGravity(Gravity.RIGHT);
                text2.setText(visits.toString());
                text2.setTextColor(Color.parseColor("#949494"));
                text2.setTextSize(22);
                Log.d("TAG", text2.getText().toString());
                layoutVisita.addView(text2);
            }
        }
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            MenuItem item = menu.add(0, 1, 1, "Voltar");
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case 1: {
                    finish();
                    break;
                }
            }
            return false;
        }
}
