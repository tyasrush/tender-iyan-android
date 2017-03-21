package com.tender.iyan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.service.TenderService;
import com.tender.iyan.ui.activity.tender.DetailActivity;
import com.tender.iyan.ui.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TenderService.ListHomeView, HomeAdapter.OnItemTenderClickListener {

    private EditText editText;
    private List<Tender> tenders = new ArrayList<>();
    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        adapter = new HomeAdapter(tenders);
        adapter.setOnItemTenderClickListener(this);
        editText = (EditText) findViewById(R.id.et_search);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        ImageView imageView = (ImageView) findViewById(R.id.btn_search);
        imageView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
        TenderService.getInstance().searchTender(this, editText.getText().toString());
    }

    @Override public void onLoadedDataSuccess(List<Tender> tenders) {
        if (tenders != null) {
            if (!this.tenders.isEmpty()) {
                this.tenders.clear();
                adapter.notifyDataSetChanged();
            }

            this.tenders.addAll(tenders);
            adapter.notifyDataSetChanged();
        }
    }

    @Override public void onLoadFailed(String message) {
        Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
    }

    @Override public void onItemTenderClick(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("tender", tenders.get(position));
        startActivity(intent);
    }
}
