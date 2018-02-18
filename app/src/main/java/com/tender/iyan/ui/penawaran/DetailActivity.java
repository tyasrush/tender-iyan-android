package com.tender.iyan.ui.penawaran;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.tender.iyan.R;
import com.tender.iyan.entity.Deal;
import com.tender.iyan.service.DealsService;
import com.tender.iyan.ui.deal.UploadActivity;
import java.util.List;

public class DetailActivity extends AppCompatActivity
    implements View.OnClickListener, DealsService.ListDealView {

  private TextView title;
  private ImageView image;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail2);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle("Detail Penawaran");
    if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    title = (TextView) findViewById(R.id.title);
    image = (ImageView) findViewById(R.id.image);
    findViewById(R.id.btn).setOnClickListener(this);
    DealsService.getInstance().get(this, new Deal(getIntent().getIntExtra("id", 0)));
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) finish();
    return super.onOptionsItemSelected(item);
  }

  @Override public void onClick(View v) {
    startActivity(
        new Intent(this, UploadActivity.class).putExtra("id", getIntent().getIntExtra("id", 0)));
  }

  @Override public void onLoadedDataSuccess(List<Deal> deals) {
    if (deals != null && deals.size() > 0) {
      Toast.makeText(this, "deal - " + deals.get(0).toString(), Toast.LENGTH_LONG).show();
      Glide.with(this)
          .load(deals.get(0).getFoto())
          .into(image);
      title.setText("Gambar diatas adalah bukti transfer");
      findViewById(R.id.btn).setVisibility(View.GONE);
    } else {
      image.setVisibility(View.GONE);
      title.setText("Belum ada bukti transfer");
    }
  }

  @Override public void onLoadFailed(String message) {
    Toast.makeText(this, "error - " + message, Toast.LENGTH_SHORT).show();
  }
}
