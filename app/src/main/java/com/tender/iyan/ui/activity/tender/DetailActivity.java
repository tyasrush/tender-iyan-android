package com.tender.iyan.ui.activity.tender;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tender.iyan.R;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.ui.adapter.DetailAdapter;
import com.tender.iyan.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TenderService.GetPenawaranView {

    private List<Penawaran> penawaranList = new ArrayList<>();
    private DetailAdapter detailAdapter;
    private Tender tender;

    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tender = getIntent().getParcelableExtra("tender");
        setTitle(tender.getName());
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = (ImageView) findViewById(R.id.img_header_detail);
        if (imageView != null) {
            Glide.with(this)
                    .load(tender.getFoto())
                    .into(imageView);
        }

        detailAdapter = new DetailAdapter(tender, penawaranList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(detailAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detail);
        if (tender.getIduser() == UserUtil.getInstance(this).getId()) {
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, com.tender.iyan.ui.activity.penawaran.AddActivity.class);
                intent.putExtra("id_request", tender.getId());
                startActivity(intent);
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        TenderService.getInstance().getPenawaranByTender(this, tender);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadDataSuccess(List<Penawaran> penawaranList) {
        this.penawaranList.addAll(penawaranList);
        detailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed(String message) {
        Toast.makeText(this, "Loading data gagal", Toast.LENGTH_SHORT).show();
    }
}
