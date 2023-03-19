package com.example.diario.db;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;



import java.util.ArrayList;

public class DbDiario extends DbHelper {

    Context context;

    public DbDiario(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarContacto(String usuario, String titulo, String cuerpo, byte[] foto) {
        
        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("fecha", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            values.put("usuario", usuario);
            values.put("titulo", titulo);
            values.put("foto", foto);
            values.put("cuerpo", cuerpo);

            id = db.insert(TABLE_DIARIO, null, values);
            db.close();
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }}