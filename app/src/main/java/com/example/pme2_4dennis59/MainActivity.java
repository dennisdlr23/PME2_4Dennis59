package com.example.pme2_4dennis59;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pme2_4dennis59.Modulos.Operaciones;
import com.example.pme2_4dennis59.Modulos.SQLiteConexion;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private CaptureBitmapView captureBitmapView;
    EditText Descripcion;
    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Descripcion = ( EditText) findViewById(R.id.txtdescripcion);
        Button btnguardar = (Button) findViewById(R.id.btnguardar);
        Button btnmostrar = (Button) findViewById(R.id.btnmostrar);

        LinearLayout mContent = (LinearLayout) findViewById(R.id.signLayout);
        captureBitmapView = new CaptureBitmapView(this, null);
        mContent.addView(captureBitmapView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
            }
        });

        btnmostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListarFirma.class);
                startActivity(intent);

            }
        });
    }

    private void guardar() {

        SQLiteConexion conexion = new SQLiteConexion(this, Operaciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        Bitmap signature = captureBitmapView.getBitmap();
        ContentValues valores = new ContentValues();
        valores.put(Operaciones.Descripcion, Descripcion.getText().toString());
        valores.put(Operaciones.ImgFirma, signature.toString());

        if (Descripcion.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor no dejar campos vacios", Toast.LENGTH_LONG).show();
        } else {

            Long Registro = db.insert(Operaciones.TablaSignature, Operaciones.Descripcion, valores);
            Toast.makeText(getApplicationContext(), "Registro INGRESADO : Codigo :" + Registro.toString(), Toast.LENGTH_LONG).show();

            db.close();
        }
        LimpiarPantalla();
    }
    private void guardarDatos() {
        try {
            firmas(captureBitmapView.getBitmap());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName, "yourDescription");

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            LimpiarPantalla();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error a guardar Datos ", Toast.LENGTH_LONG).show();
        }
    }
        private void firmas( Bitmap bitmap) {

            SQLiteConexion conexion = new SQLiteConexion(this, Operaciones.NameDatabase,null,1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] ArrayFoto  = stream.toByteArray();

            ContentValues valores = new ContentValues();

            valores.put(Operaciones.Descripcion,Descripcion.getText().toString());
            valores.put(String.valueOf(Operaciones.ImgFirma),ArrayFoto);

            Long resultado = db.insert(Operaciones.TablaSignature, null, valores);

            Toast.makeText(getApplicationContext(), "Registro ingreso con exito: " + resultado.toString()
                    ,Toast.LENGTH_LONG).show();

            db.close();
        }


        private void LimpiarPantalla()
        {
            Descripcion.setText("");
            captureBitmapView.ClearCanvas();

        }

    }