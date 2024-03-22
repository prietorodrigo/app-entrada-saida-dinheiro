package com.example.trabalhofinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Banco extends SQLiteOpenHelper {
    private static final String TAG = "";
    private static final String NOME_BANCO = "cadastro.sqlite";
    private static final int VERSAO = 1;
    public Banco (Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Criando as tabelas");
        sqLiteDatabase.execSQL("create table if not exists movimientos (id integer primary key autoincrement," +
                "fecha text, hora text, monto integer, moneda text, tipo text);");
        Log.d(TAG, "Tabelas criadas");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //nao usaremos
    }
}
