package com.example.examenfinal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    private TextView txtll;
    private EditText mtxtnombre, mtxtdescripcion, mtxtPeriocidad, mtxttiempo, mtxtCantidad;
    private Button btnGuardar, btnlista, btnfoto;

    private ImageView foto;
    private StorageReference storageReference;
    private ProgressDialog cargando;
    Bitmap thumb_bitmap = null;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private LocationManager ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        foto = findViewById(R.id.img_foto);
        mtxtnombre = findViewById(R.id.txtNombre);
        mtxtdescripcion = findViewById(R.id.txtDescripcion);
        mtxtPeriocidad = findViewById(R.id.txtPeriocidad);
        mtxttiempo = findViewById(R.id.txtTiempo);
        mtxtCantidad = findViewById(R.id.txtCantidad);


        btnlista = findViewById(R.id.btnLista);
        txtll = findViewById(R.id.ll);

        btnfoto = findViewById(R.id.btnFoto);
        btnGuardar = findViewById(R.id.btnGuardar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("img_comprimidas");
        cargando = new ProgressDialog(this);


        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, userlist.class));
            }
        });


        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(MainActivity.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri imgaeuri = CropImage.getPickImageResultUri(this, data);

            //Recortar la IMG

            CropImage.activity(imgaeuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(640, 480)
                    .setAspectRatio(2, 1).start(MainActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                File url = new File(resultUri.getPath());

                Picasso.with(this).load(url).into(foto);

                //comprimiendo imagen

                try {
                    thumb_bitmap = new Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(90).compressToBitmap(url);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                //fin del compresor


                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!mtxtnombre.getText().toString().isEmpty() && !mtxtCantidad.getText().toString().isEmpty() && !mtxttiempo.getText().toString().isEmpty() && !mtxtdescripcion.getText().toString().isEmpty() && !mtxtPeriocidad.getText().toString().isEmpty()) {

                            cargando.setTitle("Subiendo foto...");
                            cargando.setTitle("Espere por favor...");
                            cargando.show();

                            String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                            StorageReference ref = storageReference.child(fecha);
                            UploadTask uploadTask = ref.putBytes(thumb_byte);

                            //subir imagen en storage....

                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw Objects.requireNonNull(task.getException());
                                    }

                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {


                                    Uri dowloaduri = task.getResult();

                                    String id = mDatabase.push().getKey();
                                    String nombre = mtxtnombre.getText().toString();
                                    String descripcion = mtxtdescripcion.getText().toString();
                                    String cantidad = mtxtCantidad.getText().toString();
                                    String tiempo = mtxttiempo.getText().toString();
                                    String periocidad = mtxtPeriocidad.getText().toString();
                                    String url = dowloaduri.toString();


                                    com.example.examenfinal.Medicamentos user = new com.example.examenfinal.Medicamentos(id, nombre, descripcion, cantidad, tiempo, periocidad, url);

                                    mDatabase.child("nuevos").child(id).setValue(user);

                                    Toast.makeText(MainActivity.this, "Informacion exitosa", Toast.LENGTH_LONG).show();


                                    cargando.dismiss();

                                    Toast.makeText(MainActivity.this, "Imagen cargada exitosamente", Toast.LENGTH_SHORT).show();


                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                        }


                    }
                });


            }


        }


    }
}

