package com.example.trabalhofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.trabalhofinal.MenuFragment;

public class ActivityListaIngresos extends AppCompatActivity {

    private ListView lvwIngresos;
    private Banco banco;
    List<Ingreso> dadosIngresos = new ArrayList<>();

    public ActivityListaIngresos(Banco banco) {
        this.banco = banco;
    }

    public ActivityListaIngresos() {
        // Constructor sin argumentos
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ingresos);

        lvwIngresos = findViewById(R.id.lvwIngresos);
        banco = new Banco(this);
        IngresoDB ingresodb = ingresodb = new IngresoDB(this.banco);
        List<Ingreso> ingresos = ingresodb.buscarTodos();
        for(int i=0; i< ingresos.size(); i++) {
            System.out.println(ingresos.get(i).getId());
            System.out.println(ingresos.get(i).getFecha());
            System.out.println(ingresos.get(i).getHora());
            System.out.println(ingresos.get(i).getMonto());
            System.out.println(ingresos.get(i).getMoneda());
            System.out.println(ingresos.get(i).getTipo());
        }
        preencheListView();

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MenuFragment()).commit();
    }

    public void preencheListView(){
        IngresoDB ingresoDB = new IngresoDB(banco);
        dadosIngresos = ingresoDB.buscarTodos();
        String[] arrayIngresos = new String[dadosIngresos.size()];
        for (int i=0; i<dadosIngresos.size(); i++) {
            Ingreso ingreso = dadosIngresos.get(i);
            arrayIngresos[i] = ingreso.getFecha()+ "          " + ingreso.getHora()+ "          " + ingreso.getMonto()+ "          " + ingreso.getMoneda();
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayIngresos);
        lvwIngresos.setAdapter(adaptador);
    }
}