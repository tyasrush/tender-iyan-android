package com.tender.iyan.ui.tender;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.Kategori;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.ui.adapter.KategoriAdapter;
import com.tender.iyan.util.UserUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, TenderService.AddView, DatePickerDialog.OnDateSetListener, TenderService.ListKategoriView {

    private final int CAMERA = 0;
    private final int WRITE_EXTERNAL = 1;

    private EditText nameText;
    private EditText descEditText;
    private EditText anggaranText;
    private EditText waktuText;
    private EditText imageText;
    private Spinner spinner;
    private Button fotoButton;
    private Button saveButton;
    private Button waktuButton;

    private String imagePath;
    private String waktu;

    private Uri fileImage;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Tender");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TenderService.getInstance().getKategoris(this);

        nameText = (EditText) findViewById(R.id.et_name);
        descEditText = (EditText) findViewById(R.id.et_deskripsi);
        anggaranText = (EditText) findViewById(R.id.et_anggaran);
        waktuText = (EditText) findViewById(R.id.et_waktu);
        imageText = (EditText) findViewById(R.id.et_gambar);
        spinner = (Spinner) findViewById(R.id.spinner_kategori);

        fotoButton = (Button) findViewById(R.id.btn_gambar);
        if (fotoButton != null)
            fotoButton.setOnClickListener(this);

        saveButton = (Button) findViewById(R.id.btn_save);
        if (saveButton != null)
            saveButton.setOnClickListener(this);

        waktuButton = (Button) findViewById(R.id.btn_waktu);
        if (waktuButton != null)
            waktuButton.setOnClickListener(this);

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
            } else if (waktuText.getText().toString().equals("")) {
                waktuText.setError("Waktu masih kosong");
                waktuText.requestFocus();
            } else if (imageText.getText().toString().equals("")) {
                imageText.setError("Foto masih kosong");
                imageText.requestFocus();
            } else {
                Tender tender = new Tender();
                tender.setIduser(UserUtil.getInstance(this).getId());
                tender.setName(nameText.getText().toString());
                tender.setDeskripsi(descEditText.getText().toString());
                tender.setAnggaran(Integer.parseInt(anggaranText.getText().toString()));
                tender.setWaktu(waktu);
                tender.setIdKategori((int) spinner.getAdapter().getItemId(spinner.getSelectedItemPosition()));
                tender.setFoto(imagePath);

                dialog.show();
                TenderService.getInstance().upload(this, tender);
            }
        }

        if (view.getId() == fotoButton.getId()) {
            showFileChooser();
        }

        if (view.getId() == waktuButton.getId()) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            new DatePickerDialog(this, this, calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                                    ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                                    ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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

    @Override public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        waktu = i2 + "-" + (i1 + 1) + "-" + i;
        waktuText.setText(i2 + "/" + (i1 + 1) + "/" + i);
    }

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TenderCamera");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override public void onLoadedDataSuccess(List<Kategori> kategoris) {
        spinner.setAdapter(new KategoriAdapter(kategoris));
    }

    @Override public void onLoadFailed(String message) {
        Toast.makeText(this, "Terjadi kesalahan untuk load data kategori", Toast.LENGTH_SHORT).show();
    }
}
