package com.example.trabalhofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import com.example.trabalhofinal.MenuFragment;

public class MainActivity3 extends AppCompatActivity {

    private EditText edtFecha;
    private Button btnCerrar;
    private TextView txtSaldo;
    private Banco banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        edtFecha = findViewById(R.id.edtFecha);
        btnCerrar = findViewById(R.id.btnCerrar);
        txtSaldo = findViewById(R.id.txtSaldo);
        banco = new Banco(this);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obterSaldo();
            }
        });


        Calendar calendar = Calendar.getInstance();
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

    @SuppressLint("Range")
    public String obterSaldo(){
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor c = null;
        try {
            String fechaIngresada = edtFecha.getText().toString();
            c = db.rawQuery("SELECT moneda, " +
                    "SUM(CASE WHEN tipo = 'Ingreso' AND moneda = 'Pesos' THEN monto ELSE 0 END) AS total_ingresos_pesos, " +
                    "SUM(CASE WHEN tipo = 'Egreso' AND moneda = 'Pesos' THEN monto ELSE 0 END) AS total_egresos_pesos, " +
                    "SUM(CASE WHEN tipo = 'Ingreso' AND moneda = 'Reales' THEN monto ELSE 0 END) AS total_ingresos_reales, " +
                    "SUM(CASE WHEN tipo = 'Egreso' AND moneda = 'Reales' THEN monto ELSE 0 END) AS total_egresos_reales, " +
                    "SUM(CASE WHEN tipo = 'Ingreso' AND moneda = 'Dólares' THEN monto ELSE 0 END) AS total_ingresos_dolares, " +
                    "SUM(CASE WHEN tipo = 'Egreso' AND moneda = 'Dólares' THEN monto ELSE 0 END) AS total_egresos_dolares, " +
                    "(SUM(CASE WHEN tipo = 'Ingreso' AND moneda = 'Pesos' THEN monto ELSE 0 END) - SUM(CASE WHEN tipo = 'Egreso' AND moneda = 'Pesos' THEN monto ELSE 0 END)) AS saldo_pesos, " +
                    "(SUM(CASE WHEN tipo = 'Ingreso' AND moneda = 'Reales' THEN monto ELSE 0 END) - SUM(CASE WHEN tipo = 'Egreso' AND moneda = 'Reales' THEN monto ELSE 0 END)) AS saldo_reales, " +
                    "(SUM(CASE WHEN tipo = 'Ingreso' AND moneda = 'Dólares' THEN monto ELSE 0 END) - SUM(CASE WHEN tipo = 'Egreso' AND moneda = 'Dólares' THEN monto ELSE 0 END)) AS saldo_dolares " +
                    "FROM movimientos WHERE fecha = '" + fechaIngresada + "';", null);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //db.close();
        }
        if (c != null) {
            StringBuilder resultado = new StringBuilder();

            while (c.moveToNext()) {
                String moneda = c.getString(c.getColumnIndex("moneda"));
                double totalIngresosPesos = c.getDouble(c.getColumnIndex("total_ingresos_pesos"));
                double totalEgresosPesos = c.getDouble(c.getColumnIndex("total_egresos_pesos"));
                double totalIngresosReales = c.getDouble(c.getColumnIndex("total_ingresos_reales"));
                double totalEgresosReales = c.getDouble(c.getColumnIndex("total_egresos_reales"));
                double totalIngresosDolares = c.getDouble(c.getColumnIndex("total_ingresos_dolares"));
                double totalEgresosDolares = c.getDouble(c.getColumnIndex("total_egresos_dolares"));
                double saldoPesos = c.getDouble(c.getColumnIndex("saldo_pesos"));
                double saldoReales = c.getDouble(c.getColumnIndex("saldo_reales"));
                double saldoDolares = c.getDouble(c.getColumnIndex("saldo_dolares"));

                resultado.append("Total de ingresos del día: ").append("\n\n")
                        .append(totalIngresosPesos).append(" Pesos - ")
                        .append(totalIngresosReales).append(" Reales - ")
                        .append(totalIngresosDolares).append(" Dólares").append("\n\n\n");
                resultado.append("Total de egresos del día: ").append("\n\n")
                        .append(totalEgresosPesos).append(" Pesos - ")
                        .append(totalEgresosReales).append(" Reales - ")
                        .append(totalEgresosDolares).append(" Dólares").append("\n\n\n");
                resultado.append("Saldo del día: ").append("\n\n")
                        .append(saldoPesos).append(" Pesos - ")
                        .append(saldoReales).append(" Reales - ")
                        .append(saldoDolares).append(" Dólares").append("\n");
            }

            txtSaldo.setText(resultado.toString());
        }
        return null;
    }

}