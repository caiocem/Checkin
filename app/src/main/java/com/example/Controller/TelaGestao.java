package com.example.Controller;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checkin.R;

import Util.BD;

public class TelaGestao extends AppCompatActivity {
    public double qtdvst = 0;
    public Integer visits = 0;
    public String ListaNome = null;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.telagestao);

        //configura actionbar
        Util.ActionBar.configureActionBar(this, (float) 20, 0);
        getSupportActionBar().setTitle("Relatório");

        //Custom layout
        LinearLayout layoutNome = (LinearLayout) findViewById(R.id.layoutConteudo);
        LinearLayout layoutDel = (LinearLayout) findViewById(R.id.layoutDeletar);
        Cursor cLayouts = BD.getInstance().buscar("Checkin chkn",
                new String[]{"chkn.Local Local"},
                "",
                "");
        if (cLayouts.getCount()>0) {
            int coluna = 0;
            while (cLayouts.moveToNext()) {
                coluna = cLayouts.getColumnIndex("Local");
                ListaNome = cLayouts.getString(coluna);
                TextView text = new TextView(this);
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setText(ListaNome);
                text.setTextColor(Color.parseColor("#949494"));
                text.setTextSize(22);
                layoutNome.addView(text);

                ImageButton text2 = new ImageButton(this);
                text2.setLayoutParams(new LinearLayout.LayoutParams(77,77));
                text2.setImageResource(R.drawable.iconedelete);
                text2.setForegroundGravity(Gravity.RIGHT);
                text2.setBackgroundColor(Color.TRANSPARENT);
                text2.setTag(ListaNome);
                text2.setOnClickListener(OnClickListener);
                layoutDel.addView(text2);

            }
        }
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ImageButton imgBtn = (ImageButton) v;
            String tag = (String) imgBtn.getTag();
            AlertDialog.Builder alerta = new AlertDialog.Builder(TelaGestao.this);
            alerta.setTitle("Exclusão");
            alerta.setMessage("Tem certeza que deseja excluir "+tag+"?");
// Configura Método executado se escolher Sim
            alerta.setPositiveButton
                    ("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BD.getInstance().deletar("Checkin","Local = '"+tag+"'");
                            recreate();
                        }
                    });
// Configura Método executado se escolher Não
            alerta.setNegativeButton
                    ("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alerta.show();
        }
    };



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

