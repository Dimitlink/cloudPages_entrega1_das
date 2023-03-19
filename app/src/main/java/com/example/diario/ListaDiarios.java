package com.example.diario;

import static com.example.diario.db.DbHelper.TABLE_DIARIO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;

import com.example.diario.db.DbHelper;
import com.example.diario.listaDiarios.DiarioData;
import com.example.diario.listaDiarios.DiariosAdapter;

import java.util.ArrayList;

public class ListaDiarios extends AppCompatActivity {
    RecyclerView recyclerView;
    DiariosAdapter adapter;
    ArrayList<DiarioData> listaDiariops;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_diarios);

        recyclerView = (RecyclerView) findViewById(R.id.rv_diarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DbHelper(getApplicationContext());

        consultarListaDiarios();

    }

    private void consultarListaDiarios() {
        listaDiariops = new ArrayList<DiarioData>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        DiarioData diario = new DiarioData();
        String[] CAMPOS = {"id", "fecha","titulo", "foto", "cuerpo"};
        /*Cursor cursor = db.query(TABLE_DIARIO, CAMPOS, null, null,
                null, null, null, null);*/
        Cursor cursor = db.rawQuery("SELECT id, fecha, titulo, foto, cuerpo FROM t_diario ORDER BY fecha DESC",null);

        while (cursor.moveToNext()){
            diario = new DiarioData(cursor.getInt(0),cursor.getString(2),cursor.getString(4),cursor.getString(1),cursor.getBlob(3));
            listaDiariops.add(diario);
        }
        cursor.close();
        adapter = new DiariosAdapter(listaDiariops, this);
        recyclerView.setAdapter(adapter);
    }
}