package com.example.trabalhofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.example.trabalhofinal.MenuFragment;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private EditText edtFecha;
    private EditText edtValor;
    private Spinner spMoneda;
    private Button btnGuardar;
    private Button btnSalidas;
    private Banco banco;
    private String Hora;
    List<Egreso> dadosEgresos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtFecha = findViewById(R.id.edtFecha);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Montevideo"));
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);
        int segundos = calendar.get(Calendar.SECOND);
        String horaActual = hora + ":" + minutos + ":" + segundos;
        Hora = horaActual;
        edtValor = findViewById(R.id.edtValor);
        spMoneda = findViewById(R.id.spMoneda);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSalidas = findViewById(R.id.btnSalidas);
        btnGuardar.setOnClickListener(this);
        btnSalidas.setOnClickListener(this);
        banco = new Banco(getApplicationContext());


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedDate = String.format(Locale.getDefault(), "%d/%d/%04d", day, month, year);
        edtFecha.setText(formattedDate);
        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        String[] datos = new String[] {"Pesos", "Reales", "Dólares"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);
        spMoneda.setAdapter(adapter);


        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MenuFragment()).commit();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String fechaSeleccionada = String.format(Locale.getDefault(), "%d/%d/%04d", dayOfMonth, monthOfYear + 1, year);
                        edtFecha.setText(fechaSeleccionada);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnGuardar) {
            //salvar as informacoes BD
            Egreso e= new Egreso();
            EgresoDB egresodb = egresodb = new EgresoDB(this.banco);
            e.setFecha(edtFecha.getText().toString());
            e.setHora(Hora);
            e.setMonto(Integer.parseInt(edtValor.getText().toString()));
            e.setMoneda((String) spMoneda.getSelectedItem());
            e.setTipo("Egreso");
            egresodb.save(e);
            Toast.makeText(getApplicationContext(),"Egreso registrado con éxito", Toast.LENGTH_LONG).show();
        }
        else if (view.getId()==R.id.btnSalidas) {
            //salvar as informacoes no BD
            Intent i = new Intent(getApplicationContext(), ActivityListaEgresos.class);
            startActivity(i);
        }
    }

}