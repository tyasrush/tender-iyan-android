package com.tender.iyan.ui.penawaran;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.util.LocationUtil;
import com.tender.iyan.util.UserUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddActivity extends AppCompatActivity implements TenderService.UploadPenawaranView, View.OnClickListener, LocationUtil.TrackingLocation {

    private final int CAMERA = 0;
    private final int WRITE_EXTERNAL = 1;

    private EditText nameText;
    private EditText descEditText;
    private EditText anggaranText;
    private EditText imageText;
    private EditText latEditText;
    private EditText lngEditText;
    private Button fotoButton;
    private Button saveButton;
    private Button locationButton;
    private String imagePath;
    private Uri fileImage;

    private LocationUtil locationUtil;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penawaran);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tambah Penawaran");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        nameText = (EditText) findViewById(R.id.et_name);
        descEditText = (EditText) findViewById(R.id.et_deskripsi);
        anggaranText = (EditText) findViewById(R.id.et_harga);
        imageText = (EditText) findViewById(R.id.et_gambar);
        latEditText = (EditText) findViewById(R.id.et_lat);
        lngEditText = (EditText) findViewById(R.id.et_lng);

        fotoButton = (Button) findViewById(R.id.btn_gambar);
        if (fotoButton != null)
            fotoButton.setOnClickListener(this);

        saveButton = (Button) findViewById(R.id.btn_save);
        if (saveButton != null)
            saveButton.setOnClickListener(this);

        locationButton = (Button) findViewById(R.id.btn_find_location);
        if (locationButton != null)
            locationButton.setOnClickListener(this);

        locationUtil = new LocationUtil(this);
        locationUtil.connect();
        locationUtil.setTrackingLocation(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Tunggu sebentar...");
        dialog.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == saveButton.getId()) {
            if (nameText.getText().toString().equals("")) {
                nameText.setError("Nama masih kosong");
                nameText.requestFocus();
            } else if (descEditText.getText().toString().equals("")) {
                descEditText.setError("Deskripsi masih kosong");
                descEditText.requestFocus();
            } else if (anggaranText.getText().toString().equals("")) {
                anggaranText.setError("Anggaran masih kosong");
                anggaranText.requestFocus();
            } else if (imageText.getText().toString().equals("")) {
                imageText.setError("Foto masih kosong");
                imageText.requestFocus();
            } else if (latEditText.getText().toString().equals("")) {
                latEditText.setError("latitude masih kosong");
                latEditText.requestFocus();
            } else if (lngEditText.getText().toString().equals("")) {
                lngEditText.setError("longitude masih kosong");
                lngEditText.requestFocus();
            } else {
                Penawaran penawaran = new Penawaran();
                penawaran.setIdTender(getIntent().getIntExtra("id_request", 0));
                penawaran.setIdUser(UserUtil.getInstance(this).getId());
                penawaran.setNama(nameText.getText().toString());
                penawaran.setDeskripsi(descEditText.getText().toString());
                penawaran.setHarga(Integer.parseInt(anggaranText.getText().toString()));
                penawaran.setLat(Double.parseDouble(latEditText.getText().toString()));
                penawaran.setLng(Double.parseDouble(lngEditText.getText().toString()));
                penawaran.setFoto(imagePath);
                dialog.show();
                TenderService.getInstance().uploadPenawaran(this, penawaran);
            }
        }

        if (view.getId() == fotoButton.getId()) {
            showFileChooser();
        }

        if (view.getId() == locationButton.getId()) {
            locationUtil.find();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_EXTERNAL && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            imagePath = cursor.getString(column_index);
            imageText.setText(imagePath);
        }

        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            imagePath = fileImage.getPath();
            imageText.setText(imagePath);
        }
    }

    private void showFileChooser() {
        new AlertDialog.Builder(this)
                .setTitle("Unggah Gambar dari...")
                .setItems(new String[]{"Galeri", "Kamera"}, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        WRITE_EXTERNAL);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select File"), WRITE_EXTERNAL);
                            }
                        }

                        if (i == 1) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        CAMERA);
                            } else {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                fileImage = Uri.fromFile(getOutputMediaFile());
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileImage);
                                startActivityForResult(cameraIntent, CAMERA);
                            }
                        }
                    }
                }).show();
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
        }

        if (requestCode == CAMERA
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            fileImage = Uri.fromFile(getOutputMediaFile());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileImage);
            startActivityForResult(cameraIntent, CAMERA);
        }

        if (requestCode == LocationUtil.LOCATION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            locationUtil.find();
        }
    }

    @Override
    public void onAddedSuccess() {
        if (dialog.isShowing())
            dialog.dismiss();

        finish();
    }

    @Override
    public void onAddedFailed(String message) {
        if (dialog.isShowing())
            dialog.dismiss();

        Toast.makeText(this, "upload error : " + message, Toast.LENGTH_SHORT).show();
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TenderCamera");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override public void onLocationFound(Location location) {
        latEditText.setText(String.valueOf(location.getLatitude()));
        lngEditText.setText(String.valueOf(location.getLongitude()));
    }

    @Override public void onLocationError(String message) {
        Toast.makeText(this, "Terjadi kesalahan saat mencari lokasi", Toast.LENGTH_SHORT).show();
    }
}
