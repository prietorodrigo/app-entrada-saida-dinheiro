package com.example.trabalhofinal;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EgresoDB {
    private Banco banco;
    public EgresoDB(Banco b) {
        this.banco = b;
    }
    public long save(Egreso egreso) {
        long id = egreso.getId();
        SQLiteDatabase db = banco.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("fecha", egreso.getFecha());
            values.put("hora", egreso.getHora());
            values.put("monto", egreso.getMonto());
            values.put("moneda", egreso.getMoneda());
            values.put("tipo", egreso.getTipo());
            if (id==0) {
                //novo registro
                id = db.insert("movimientos", "", values);
            }
            else {
                //atualizar registro
                String _id = String.valueOf(egreso.getId());
                String[] whereArgs = new String[]{_id};
                id = db.update("movimientos", values, "id=?", whereArgs);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            //db.close();
        }
        return id;
    }
    @SuppressLint("Range")
    private List<Egreso> paraLista(Cursor c) {
        List<Egreso> egresos = new ArrayList<>();
        if (c.moveToFirst()) {
            do{
                Egreso e = new Egreso();
                e.setId(c.getInt(c.getColumnIndex("id")));
                e.setFecha(c.getString(c.getColumnIndex("fecha")));
                e.setHora(c.getString(c.getColumnIndex("hora")));
                e.setMonto(c.getInt(c.getColumnIndex("monto")));
                e.setMoneda(c.getString(c.getColumnIndex("moneda")));
                e.setTipo(c.getString(c.getColumnIndex("tipo")));
                egresos.add(e);

            } while (c.moveToNext());
        }
        return egresos;
    }
    public List<Egreso> buscarTodos(){
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor c = null;
        try {
            //c = db.query("aluno", null, null, null, null, null, null);
            c = db.rawQuery("SELECT * FROM movimientos WHERE tipo='Egreso'", null);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.close();
        }
        if (c != null) {
            return paraLista(c);
        }
        return null;
    }
}
