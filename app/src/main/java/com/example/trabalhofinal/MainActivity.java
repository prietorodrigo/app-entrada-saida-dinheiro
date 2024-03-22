package com.example.trabalhofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.example.trabalhofinal.MenuFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtFecha;
    private EditText edtValor;
    private Spinner spMoneda;
    private Button btnGuardar;
    private Button btnEntradas;
    private Banco banco;
    private String Hora;
    private static final String API_BASE_URL = "https://openexchangerates.org/api/";
    private static final String API_APP_ID = "9d6822af42414da9b2dfe351a3f878ce";
    private TextView txtPesoUruguayo;
    private TextView txtRealBrasileno;
    private TextView txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        btnEntradas = findViewById(R.id.btnEntradas);
        btnGuardar.setOnClickListener(this);
        btnEntradas.setOnClickListener(this);
        banco = new Banco(getApplicationContext());
        txtFecha = findViewById(R.id.txtFecha);
        txtPesoUruguayo = findViewById(R.id.txtPesoUruguayo);
        txtRealBrasileno = findViewById(R.id.txtRealBrasileno);


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


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaActual = new Date();
        String fechaFormateada = dateFormat.format(fechaActual);
        txtFecha.setText("Cotización de la moneda del día " + fechaFormateada + ": \n");


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ExchangeRatesAPI api = retrofit.create(ExchangeRatesAPI.class);

        Call<ExchangeRatesResponse> call = api.getLatestRates(API_APP_ID, "USD");
        call.enqueue(new Callback<ExchangeRatesResponse>() {
            @Override
            public void onResponse(Call<ExchangeRatesResponse> call, Response<ExchangeRatesResponse> response) {
                if (response.isSuccessful()) {
                    ExchangeRatesResponse exchangeRates = response.body();
                    if (exchangeRates != null) {
                        Double pesoUruguayo = exchangeRates.getRates().get("UYU");
                        Double realBrasileno = exchangeRates.getRates().get("BRL");

                        txtPesoUruguayo.setText("Dólares a Pesos: "+String.valueOf(pesoUruguayo));
                        txtRealBrasileno.setText("Dólares a Reales: "+String.valueOf(realBrasileno));
                    }
                }
            }

            @Override
            public void onFailure(Call<ExchangeRatesResponse> call, Throwable t) {
                txtRealBrasileno.setText(t.getMessage());
            }
        });
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
            Ingreso i= new Ingreso();
            IngresoDB ingresodb = ingresodb = new IngresoDB(this.banco);
            i.setFecha(edtFecha.getText().toString());
            i.setHora(Hora);
            i.setMonto(Integer.parseInt(edtValor.getText().toString()));
            i.setMoneda((String) spMoneda.getSelectedItem());
            i.setTipo("Ingreso");
            ingresodb.save(i);
            Toast.makeText(getApplicationContext(),"Ingreso registrado con éxito", Toast.LENGTH_LONG).show();
        }
        else if (view.getId()==R.id.btnEntradas) {
            //salvar as informacoes no BD
            Intent i = new Intent(getApplicationContext(), ActivityListaIngresos.class);
            startActivity(i);
        }
    }

}