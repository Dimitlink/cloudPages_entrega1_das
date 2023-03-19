package com.example.diario;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.diario.db.DbDiario;

import java.io.ByteArrayOutputStream;

public class EscrituraDiario extends AppCompatActivity {
    private final int GALLERY_CODE = 1000;
    private int presionadoEscritura = 0;
    private int pulsadoAtras = 0;

    Uri imagen;
    ImageView iDiario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean tema = prefs.getBoolean("tema",false);
        if(tema) {
            setTheme(R.style.Theme_Azul);
        }
        else{
            setTheme(R.style.Theme_Amarillo);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escritura_diario);

        EditText txtTitulo, txtCuerpo;

        iDiario = (ImageView) findViewById(R.id.i_diario);
        Button bGuardar = (Button) findViewById(R.id.b_guardar);
        Button bSeleccFoto = (Button) findViewById(R.id.b_galeria);

        txtTitulo = (EditText) findViewById(R.id.t_titulo);
        txtCuerpo = (EditText) findViewById(R.id.t_cuerpoDiario);

        if(savedInstanceState != null){
            imagen = savedInstanceState.getParcelable("imageUri");
            iDiario.setImageURI(imagen);
            presionadoEscritura = savedInstanceState.getInt("pulsado");
            pulsadoAtras = savedInstanceState.getInt("pulsadoAtras");

            if(pulsadoAtras == 1){
                crearDialogo();
            }
        }

        bGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            if(!txtTitulo.getText().toString().equals("") && !txtCuerpo.getText().toString().equals("")) {
                byte[] imageInByte = getBytes(iDiario);
                DbDiario dbDiario = new DbDiario(EscrituraDiario.this);
                long id = dbDiario.insertarContacto("Julia", txtTitulo.getText().toString(), txtCuerpo.getText().toString(), imageInByte);

                if (id > 0) {
                    Toast.makeText(EscrituraDiario.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                    txtTitulo.setText("Introduce un titlo");
                    txtCuerpo.setText("Empieza a escribir tu diario aqui...");
                    presionadoEscritura = 1;

                    // Pido permisos, se ejecuta solo cuando la version de API es 33
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) !=
                                PackageManager.PERMISSION_GRANTED) {
                            //PEDIR EL PERMISO
                            ActivityCompat.requestPermissions(EscrituraDiario.this, new
                                    String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
                        }
                    }
                    NotificationManager elManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(getApplicationContext(), "IdCanal");

                    // Necesario para API mayor o igual que la de la version Oreo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        NotificationChannel channel = new NotificationChannel("IdCanal", "canal", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.setDescription("esElCanal");
                        // Register the channel with the system. You can't change the importance
                        // or other notification behaviors after this.
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);

                        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                                .setContentTitle("Recordatorio")
                                .setContentText("'Recuerda votar! Tu opinión nos es útil.")
                                .setVibrate(new long[]{0, 1000, 500, 1000})
                                .setAutoCancel(true);


                        elManager.notify(1, elBuilder.build());
                    }
                } else {
                    Toast.makeText(EscrituraDiario.this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(EscrituraDiario.this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show();
            }
        }
    });

    bSeleccFoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent iGaleria = new Intent(Intent.ACTION_PICK);
            iGaleria.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGaleria,GALLERY_CODE);
        }
    });

}

    private byte[] getBytes(ImageView imageView) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytesData = stream.toByteArray();
            stream.close();
            return bytesData;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("imageUri", imagen);
        outState.putInt("pulsado", presionadoEscritura);
        outState.putInt("pulsadoAtras", pulsadoAtras);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){

            if (requestCode==GALLERY_CODE){

                iDiario.setImageURI(data.getData());
                imagen = data.getData();


            }
        }
    }

    public void crearDialogo(){
        pulsadoAtras = 1;
        new AlertDialog.Builder(this)
                .setTitle("Aviso")
                .setMessage("¿Seguro que deseas salir sin guardar?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                        startActivity(intent);
                        presionadoEscritura = 0;
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        if (presionadoEscritura == 0) {
            crearDialogo();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
            startActivity(intent);
            presionadoEscritura = 0;
            finish();

        }
    }
}