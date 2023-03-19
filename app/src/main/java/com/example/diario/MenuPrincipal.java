package com.example.diario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        Button bEscribir, bLista, bAjustes;

        bEscribir = (Button) findViewById(R.id.b_escrbir);
        bLista = (Button) findViewById(R.id.b_lista);
        bAjustes = (Button) findViewById(R.id.b_ajustes);

        bEscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEscrituraDiario = new Intent(getApplicationContext(),EscrituraDiario.class);
                startActivity(intentEscrituraDiario);
            }
        });

        bLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentListaDiarios = new Intent(getApplicationContext(),ListaDiarios.class);
                startActivity(intentListaDiarios);
            }
        });

        bAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAjustes = new Intent(getApplicationContext(),Ajustes.class);
                startActivity(intentAjustes);
            }
        });
    }

}