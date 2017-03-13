package com.tender.iyan.ui.activity.penawaran;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.util.DialogUtil;
import com.tender.iyan.util.UserUtil;


public class AddActivity extends AppCompatActivity implements TenderService.UploadPenawaranView, View.OnClickListener {

    private final int WRITE_EXTERNAL = 1;

    private EditText nameText;
    private EditText descEditText;
    private EditText anggaranText;
    private EditText imageText;
    private Button fotoButton;
    private Button saveButton;
    private String imagePath;

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

        fotoButton = (Button) findViewById(R.id.btn_gambar);
        if (fotoButton != null)
            fotoButton.setOnClickListener(this);

        saveButton = (Button) findViewById(R.id.btn_save);
        if (saveButton != null)
            saveButton.setOnClickListener(this);
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
            } else {
                Penawaran penawaran = new Penawaran();
                penawaran.setIdTender(getIntent().getIntExtra("id_request", 0));
                penawaran.setIdUser(UserUtil.getInstance(this).getId());
                penawaran.setNama(nameText.getText().toString());
                penawaran.setDeskripsi(descEditText.getText().toString());
                penawaran.setHarga(Integer.parseInt(anggaranText.getText().toString()));
                penawaran.setFoto(imagePath);
                DialogUtil.getInstance(this).showProgressDialog("", "Uploading...", true);
                TenderService.getInstance().uploadPenawaran(this, penawaran);
            }
        }

        if (view.getId() == fotoButton.getId()) {
            showFileChooser();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            imagePath = cursor.getString(column_index);
            imageText.setText(imagePath);
        }
    }

    private void showFileChooser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
        }
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
    }

    @Override
    public void onAddedSuccess() {
        DialogUtil.getInstance(this).dismiss();
        finish();
    }

    @Override
    public void onAddedFailed(String message) {
        DialogUtil.getInstance(this).dismiss();
        Toast.makeText(this, "upload error : " + message, Toast.LENGTH_SHORT).show();
    }
}
