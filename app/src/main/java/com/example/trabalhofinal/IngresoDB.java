package com.example.trabalhofinal;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class IngresoDB {
    private Banco banco;
    public IngresoDB(Banco b) {
        this.banco = b;
    }
    public long save(Ingreso ingreso) {
        long id = ingreso.getId();
        SQLiteDatabase db = banco.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("fecha", ingreso.getFecha());
            values.put("hora", ingreso.getHora());
            values.put("monto", ingreso.getMonto());
            values.put("moneda", ingreso.getMoneda());
            values.put("tipo", ingreso.getTipo());
            if (id==0) {
                //novo registro
                id = db.insert("movimientos", "", values);
            }
            else {
                //atualizar registro
                String _id = String.valueOf(ingreso.getId());
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
    private List<Ingreso> paraLista(Cursor c) {
        List<Ingreso> ingresos = new ArrayList<>();
        if (c.moveToFirst()) {
            do{
                Ingreso i = new Ingreso();
                i.setId(c.getInt(c.getColumnIndex("id")));
                i.setFecha(c.getString(c.getColumnIndex("fecha")));
                i.setHora(c.getString(c.getColumnIndex("hora")));
                i.setMonto(c.getInt(c.getColumnIndex("monto")));
                i.setMoneda(c.getString(c.getColumnIndex("moneda")));
                i.setTipo(c.getString(c.getColumnIndex("tipo")));
                ingresos.add(i);

            } while (c.moveToNext());
        }
        return ingresos;
    }
    public List<Ingreso> buscarTodos(){
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor c = null;
        try {
            //c = db.query("aluno", null, null, null, null, null, null);
            c = db.rawQuery("SELECT * FROM movimientos WHERE tipo='Ingreso'", null);

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
