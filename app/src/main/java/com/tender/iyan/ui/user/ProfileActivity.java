package com.tender.iyan.ui.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.User;
import com.tender.iyan.service.UserService;
import com.tender.iyan.util.UserUtil;

public class ProfileActivity extends AppCompatActivity implements UserService.ShowView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Profil Saya");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        User user = new User(UserUtil.getInstance(this).getId());
        UserService.getInstance().show(this, user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadSuccess(User user) {
        TextView nameTextView = (TextView) findViewById(R.id.tv_name);
        TextView emailTextView = (TextView) findViewById(R.id.tv_email);
        TextView contactTextView = (TextView) findViewById(R.id.tv_contact);
        TextView alamatTextView = (TextView) findViewById(R.id.tv_address);
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        contactTextView.setText(String.valueOf(user.getContact()));
        alamatTextView.setText(user.getAlamat());
    }

    @Override
    public void onLoadFailed(String message) {
        Toast.makeText(this, "user load error : " + message, Toast.LENGTH_SHORT).show();
    }
}
