package Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.checkin.R;

import java.lang.reflect.Field;

public class BD {
    protected SQLiteDatabase db;
    private final String NOME_BANCO = "pokemon_go_bd";
    private static BD INSTANCE = new BD();

    private final String[] SCRIPT_DATABASE_CREATE = new String[]{
            "CREATE TABLE Checkin (" +
                    "Local TEXT PRIMARY KEY," +
                    "qtdVisitas INTEGER NOT NULL," +
                    "cat INTEGER NOT NULL," +
                    "latitude TEXT NOT NULL," +
                    "longitude TEXT NOT NULL," +
                    "CONSTRAINT fkey0 FOREIGN KEY (cat) REFERENCES Categoria (idCategoria)"+
            ");",
            "CREATE TABLE Categoria (" +
                    "idCategoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL);",
            "INSERT INTO Categoria (nome) VALUES" +
                    "('Restaurante')," +
                    "('Bar')," +
                    "('Cinema')," +
                    "('Universidade')," +
                    "('Estádio')," +
                    "('Parque')," +
                    "('Outros');"
    };

    private BD() {
        Context ctx = MyApp.getAppContext();
        // Abre o banco de dados já existente ou então cria um banco novo

        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

        //busca por tabelas existentes no banco = "show tables" do MySQL
        //SELECT * FROM sqlite_master WHERE type = "table"
        Cursor c = buscar("sqlite_master", null, "type = 'table'", "");

        //Cria tabelas do banco de dados caso o mesmo estiver vazio.
        //Sempre os bancos criados pelo método openOrCreateDatabase() possuem uma tabela padrão "android_metadata"
        if(c.getCount() == 1){
            for(int i = 0; i < SCRIPT_DATABASE_CREATE.length; i++){
                db.execSQL(SCRIPT_DATABASE_CREATE[i]);
            }
            Log.i("BANCO_DADOS", "Criou tabelas do banco e as populou.");
        }
        else{
            //Banco já criado
            //precisamos garantir que os hashes dos resources sejam os mesmos


        }
        c.close();
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    public static BD getInstance(){
        return INSTANCE;
    }

    // Insere um novo registro
    public long inserir(String tabela, ContentValues valores) {
        long id = db.insert(tabela, null, valores);

        Log.i("BANCO_DADOS", "Cadastrou registro com o id [" + id + "]");
        return id;
    }

    // Atualiza registros
    public int atualizar(String tabela, ContentValues valores, String where) {
        int count = db.update(tabela, valores, where, null);

        Log.i("BANCO_DADOS", "Atualizou [" + count + "] registros");
        return count;
    }

    // Deleta registros
    public int deletar(String tabela, String where) {
        int count = db.delete(tabela, where, null);

        Log.i("BANCO_DADOS", "Deletou [" + count + "] registros");
        return count;
    }

    // Busca registros
    public Cursor buscar(String tabela, String colunas[], String where, String orderBy) {
        Cursor c;
        if(!where.equals(""))
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        else
            c = db.query(tabela, colunas, null, null, null, null, orderBy);

        Log.i("BANCO_DADOS", "Realizou uma busca e retornou [" + c.getCount() + "] registros.");
        return c;
    }

    // Abre conexão com o banco
    public void abrir() {
        Context ctx = MyApp.getAppContext();
        // Abre o banco de dados já existente
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    // Fecha o banco
    public void fechar() {
        // fecha o banco de dados
        if (db != null) {
            db.close();
            Log.i("BANCO_DADOS", "Fechou conexão com o Banco.");
        }
    }
}
