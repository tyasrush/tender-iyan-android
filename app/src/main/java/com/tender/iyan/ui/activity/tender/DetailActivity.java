package com.tender.iyan.ui.activity.tender;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tender.iyan.R;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.ui.adapter.DetailAdapter;
import com.tender.iyan.util.UserUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity
    implements TenderService.GetPenawaranView, DetailAdapter.OnOpenMapClickListener,
    DetailAdapter.OnDealClickListener, TenderService.SaveDealView {

  private List<Penawaran> penawaranList = new ArrayList<>();
  private DetailAdapter detailAdapter;
  private Tender tender;

  private final String TAG = getClass().getName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    tender = getIntent().getParcelableExtra("tender");
    setTitle(tender.getName());
    if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    ImageView imageView = (ImageView) findViewById(R.id.img_header_detail);
    if (imageView != null) {
      Glide.with(this)
          .load(tender.getFoto())
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(imageView);
    }

    detailAdapter = new DetailAdapter(this, tender, penawaranList);
    detailAdapter.setOnOpenMapClickListener(this);
    detailAdapter.setOnDealClickListener(this);

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_detail);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(detailAdapter);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detail);
    if (tender.getIduser() == UserUtil.getInstance(this).getId()) {
      fab.setVisibility(View.GONE);
    }

    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(DetailActivity.this,
            com.tender.iyan.ui.activity.penawaran.AddActivity.class);
        intent.putExtra("id_request", tender.getId());
        startActivity(intent);
      }
    });

    final TextView textView = (TextView) findViewById(R.id.time_count_detail);
    Calendar start_calendar = Calendar.getInstance();
    Calendar end_calendar = Calendar.getInstance();

    start_calendar.setTime(new Date());
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    Date date;
    try {
      date = df.parse(tender.getWaktu());
      end_calendar.setTime(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    if (end_calendar.before(start_calendar)) {
      fab.setVisibility(View.GONE);
    }

    long start_millis = start_calendar.getTimeInMillis(); //get the start time in milliseconds
    long end_millis = end_calendar.getTimeInMillis(); //get the end time in milliseconds
    long total_millis = (end_millis - start_millis); //total time in milliseconds

    //1000 = 1 second interval
    CountDownTimer cdt = new CountDownTimer(total_millis, 1000) {
      @Override public void onTick(long millisUntilFinished) {
        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
        millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

        textView.setText(days
            + " Hari | "
            + hours
            + " Jam | "
            + minutes
            + " Menit | "
            + seconds
            + " Detik"); //You can compute the millisUntilFinished on hours/minutes/seconds
      }

      @Override public void onFinish() {
        textView.setText("Waktu Sudah Habis!");
      }
    };
    cdt.start();
  }

  @Override protected void onStart() {
    super.onStart();
    TenderService.getInstance().getPenawaranByTender(this, tender);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_detail, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) finish();

    if (item.getItemId() == R.id.option_alphabet) {
      if (!penawaranList.isEmpty()) {

        penawaranList.clear();
        detailAdapter.notifyDataSetChanged();
      }

      TenderService.getInstance().filterPenawaranByTender(this, tender, TenderService.ALPHABET);
    }

    if (item.getItemId() == R.id.option_cheapest) {
      if (!penawaranList.isEmpty()) {

        penawaranList.clear();
        detailAdapter.notifyDataSetChanged();
      }

      TenderService.getInstance().filterPenawaranByTender(this, tender, TenderService.CHEAPEST);
    }

    if (item.getItemId() == R.id.option_most_expensive) {
      if (!penawaranList.isEmpty()) {

        penawaranList.clear();
        detailAdapter.notifyDataSetChanged();
      }

      TenderService.getInstance()
          .filterPenawaranByTender(this, tender, TenderService.MOST_EXPENSIVE);
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onLoadDataSuccess(List<Penawaran> penawaranList) {
    Log.d(TAG, "load penawaran: " + penawaranList.toString());
    if (!this.penawaranList.isEmpty()) {
      this.penawaranList.clear();
      detailAdapter.notifyDataSetChanged();
    }
    this.penawaranList.addAll(penawaranList);
    detailAdapter.notifyDataSetChanged();
  }

  @Override public void onLoadFailed(String message) {
    Toast.makeText(this, "Loading data gagal", Toast.LENGTH_SHORT).show();
  }

  @Override public void onOpenMapClick(int position) {
    if (penawaranList != null && !penawaranList.isEmpty()) {
      Penawaran penawaran = penawaranList.get(position);
      Log.d(TAG, "onOpenMapClick: " + penawaran.toString());
      String uri = "http://maps.google.com/maps?q=loc:"
          + penawaran.getLat()
          + ","
          + penawaran.getLng()
          + " ("
          + penawaran.getNama()
          + ")";
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
      startActivity(intent);
    }
  }

  @Override public void onDealClick(int position) {
    if (penawaranList != null && !penawaranList.isEmpty()) {
      TenderService.getInstance()
          .uploadDeal(this, tender.getId(), penawaranList.get(position).getId());
    }
  }

  @Override public void onSaveSuccess() {
    finish();
  }

  @Override public void onSaveFailed(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
