package com.example.trabalhofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.trabalhofinal.MenuFragment;

public class ActivityListaEgresos extends AppCompatActivity {

    private ListView lvwEgresos;
    private Banco banco;
    List<Egreso> dadosEgresos = new ArrayList<>();
    public ActivityListaEgresos(Banco banco) {
        this.banco = banco;
    }

    public ActivityListaEgresos() {
        // Constructor sin argumentos
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_egresos);

        lvwEgresos = findViewById(R.id.lvwEgresos);
        banco = new Banco(this);
        EgresoDB egresodb = egresodb = new EgresoDB(this.banco);
        List<Egreso> egresos = egresodb.buscarTodos();
        for(int i=0; i< egresos.size(); i++) {
            System.out.println(egresos.get(i).getId());
            System.out.println(egresos.get(i).getFecha());
            System.out.println(egresos.get(i).getHora());
            System.out.println(egresos.get(i).getMonto());
            System.out.println(egresos.get(i).getMoneda());
            System.out.println(egresos.get(i).getTipo());
        }
        preencheListView();

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MenuFragment()).commit();
    }

    public void preencheListView(){
        EgresoDB egresoDB = new EgresoDB(banco);
        dadosEgresos = egresoDB.buscarTodos();
        String[] arrayEgresos = new String[dadosEgresos.size()];
        for (int i=0; i<dadosEgresos.size(); i++) {
            Egreso egreso = dadosEgresos.get(i);
            arrayEgresos[i] = egreso.getFecha()+ "          " + egreso.getHora()+ "          " + egreso.getMonto()+ "          " + egreso.getMoneda();
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayEgresos);
        lvwEgresos.setAdapter(adaptador);
    }
}