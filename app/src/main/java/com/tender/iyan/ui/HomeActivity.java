package com.tender.iyan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.ui.penawaran.DealsActivity;
import com.tender.iyan.ui.tender.AddActivity;
import com.tender.iyan.ui.tender.DetailActivity;
import com.tender.iyan.ui.tender.MineActivity;
import com.tender.iyan.ui.adapter.HomeAdapter;
import com.tender.iyan.ui.user.LoginActivity;
import com.tender.iyan.ui.user.ProfileActivity;
import com.tender.iyan.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity home ini adalah untuk menampilkan data list utama dan menu utama
 */
public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, TenderService.ListHomeView,
    View.OnClickListener, HomeAdapter.OnItemTenderClickListener {

  private HomeAdapter homeAdapter;
  private List<Tender> tenders = new ArrayList<>();
  private FloatingActionButton addButton;
  //    private ProgressDialog progressDialog;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(getString(R.string.app_name));

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    homeAdapter = new HomeAdapter(tenders);
    homeAdapter.setOnItemTenderClickListener(this);

    //untuk menampilkan list data yang ada pada halamn utama
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_home);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(homeAdapter);
    TenderService.getInstance().getAll(this, null);

    addButton = (FloatingActionButton) findViewById(R.id.fab_add);
    addButton.setOnClickListener(this);

    //untuk menampilkan dialog load data
    //        progressDialog = new ProgressDialog(this);
    //        progressDialog.setMessage("Mengambil data...");
    //        progressDialog.setCancelable(false);
  }

  @Override protected void onStart() {
    super.onStart();
    //        if (tenders.isEmpty() && progressDialog != null)
    //            progressDialog.show();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_refresh, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.option_refresh) {
      if (!tenders.isEmpty()) {
        //                if (progressDialog != null)
        //                    progressDialog.show();

        tenders.clear();
        homeAdapter.notifyDataSetChanged();
      }

      TenderService.getInstance().getAll(this, null);
    }

    if (item.getItemId() == R.id.option_search) {
      startActivity(new Intent(this, SearchActivity.class));
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody") @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.nav_profile) {
      startActivity(new Intent(this, ProfileActivity.class));
    } else if (id == R.id.nav_request) {
      startActivity(new Intent(this, MineActivity.class));
    } else if (id == R.id.nav_deal) {
      startActivity(new Intent(this, DealsActivity.class));
    } else if (id == R.id.nav_logout) {
      UserUtil.getInstance(getApplicationContext()).logout();
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override public void onLoadedDataSuccess(List<Tender> tenders) {
    this.tenders.addAll(tenders);
    homeAdapter.notifyDataSetChanged();
  }

  @Override public void onLoadFailed(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override public void onClick(View view) {
    if (view.getId() == addButton.getId()) {
      startActivity(new Intent(this, AddActivity.class));
    }
  }

  @Override public void onItemTenderClick(View view, int position) {
    Intent intent = new Intent(this, DetailActivity.class);
    intent.putExtra("tender", tenders.get(position));
    startActivity(intent);
  }
}
