package com.tender.iyan.ui.penawaran;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.tender.iyan.R;
import com.tender.iyan.entity.Deal;
import com.tender.iyan.entity.User;
import com.tender.iyan.service.DealsService;
import com.tender.iyan.ui.adapter.DealsAdapter;
import com.tender.iyan.ui.deal.UploadActivity;
import com.tender.iyan.util.UserUtil;
import java.util.ArrayList;
import java.util.List;

public class DealsActivity extends AppCompatActivity
    implements DealsService.ListDealView, DealsAdapter.OnDealsClickListener {

  private TextView tvIndicator;
  private RecyclerView recyclerView;
  private DealsAdapter adapter;
  private List<Deal> deals;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deals);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    deals = new ArrayList<>();
    adapter = new DealsAdapter();
    adapter.setDeals(deals);
    adapter.setOnDealsClickListener(this);
    recyclerView = (RecyclerView) findViewById(R.id.recycler);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(adapter);
    tvIndicator = (TextView) findViewById(R.id.tv_indicator);
    User user = new User(UserUtil.getInstance(this).getId());
    DealsService.getInstance().getAll(this, user);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) finish();
    return super.onOptionsItemSelected(item);
  }

  @Override public void onLoadedDataSuccess(List<Deal> deals) {
    if (deals != null && deals.size() > 0) {
      this.deals.addAll(deals);
      adapter.notifyDataSetChanged();
      tvIndicator.setVisibility(View.GONE);
    }
  }

  @Override public void onLoadFailed(String message) {
    Toast.makeText(this, "errorr - " + message, Toast.LENGTH_SHORT).show();
  }

  @Override public void onDealsClick(View view, int position) {
    startActivity(
        new Intent(this, DetailActivity.class).putExtra("id", deals.get(position).getId()));
  }
}
