package com.tender.iyan.ui.deal;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.tender.iyan.R;
import com.tender.iyan.entity.Deal;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.service.DealsService;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.ui.penawaran.AddActivity;
import com.tender.iyan.util.UserUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadActivity extends AppCompatActivity
    implements View.OnClickListener, DealsService.UploadView {

  private final int CAMERA = 0;
  private final int WRITE_EXTERNAL = 1;
  private EditText fotoText;
  private Button fotoButton;
  private FloatingActionButton fab;
  private String imagePath;
  private Uri fileImage;

  private File getOutputMediaFile() {
    File mediaStorageDir =
        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "TenderCamera");

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        return null;
      }
    }

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
  }

  private void showFileChooser() {
    new AlertDialog.Builder(this).setTitle("Unggah Gambar dari...")
        .setItems(new String[] { "Galeri", "Kamera" }, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            if (i == 0) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                  && ContextCompat.checkSelfPermission(UploadActivity.this,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadActivity.this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, WRITE_EXTERNAL);
              } else {
                Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), WRITE_EXTERNAL);
              }
            }

            if (i == 1) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                  && ContextCompat.checkSelfPermission(UploadActivity.this,
                  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadActivity.this,
                    new String[] { Manifest.permission.CAMERA }, CAMERA);
              } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                fileImage = Uri.fromFile(getOutputMediaFile());
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileImage);
                startActivityForResult(cameraIntent, CAMERA);
              }
            }
          }
        })
        .show();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bukti_transfer);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(this);
    fotoButton = (Button) findViewById(R.id.btn_gambar);
    fotoButton.setOnClickListener(this);
    fotoText = (EditText) findViewById(R.id.et_gambar);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) finish();
    return super.onOptionsItemSelected(item);
  }

  @Override public void onClick(View v) {
    if (v.getId() == fotoButton.getId()) {
      showFileChooser();
    }

    if (v.getId() == fab.getId()) {
      if (fotoText.getText().toString().equals("")) {
        fotoText.setError("belum memilih foto/belum ada foto yang didapatkan");
        fotoButton.requestFocus();
      } else {
        Deal deal = new Deal();
        deal.setId(getIntent().getIntExtra("id", 0));
        deal.setFoto(imagePath);
        DealsService.getInstance().upload(this, deal);
      }
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == WRITE_EXTERNAL
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null) {
      Uri selectedImageUri = data.getData();
      String[] projection = { MediaStore.MediaColumns.DATA };
      Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      cursor.moveToFirst();

      imagePath = cursor.getString(column_index);
      fotoText.setText(imagePath);
    }

    if (requestCode == CAMERA && resultCode == RESULT_OK) {
      imagePath = fileImage.getPath();
      fotoText.setText(imagePath);
    }
  }

  @Override public void onAddedSuccess() {
    finish();
  }

  @Override public void onAddedFailed(String message) {
    Toast.makeText(this, "upload error : " + message, Toast.LENGTH_SHORT).show();
  }
}
