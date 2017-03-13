package com.tender.iyan.ui.activity.tender;

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
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.entity.User;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.ui.adapter.HomeAdapter;
import com.tender.iyan.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class MineActivity extends AppCompatActivity implements
        HomeAdapter.OnItemTenderClickListener,
        TenderService.ListHomeView {

    private HomeAdapter homeAdapter;
    private List<Tender> tenders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tender Saya");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_mine);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        homeAdapter = new HomeAdapter(tenders);
        homeAdapter.setOnItemTenderClickListener(this);

        recyclerView.setAdapter(homeAdapter);

        TenderService.getInstance().getAll(this, new User(UserUtil.getInstance(this).getId()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_mine);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemTenderClick(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("tender", tenders.get(position));
        startActivity(intent);
    }

    @Override
    public void onLoadedDataSuccess(List<Tender> tenders) {
        this.tenders.addAll(tenders);
        homeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed(String message) {
        Toast.makeText(this, "error loading : " + message, Toast.LENGTH_SHORT).show();
    }
}
