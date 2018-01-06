package com.tender.iyan.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.tender.iyan.R;
import com.tender.iyan.ui.HomeActivity;
import com.tender.iyan.ui.user.fragment.LoginFragment;
import com.tender.iyan.util.UserUtil;

/**
 * Activity login untuk membentuk tampilan login dan sign up pada pertama kali pengguna membuka aplikasi
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout loginContainer = (FrameLayout) findViewById(R.id.container_login);

        //cek user login,jika sudah login maka akan dilanjutkan ke homeactivity
        if (UserUtil.getInstance(getApplicationContext()).isLogin()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            //menampilkan halaman login
            getSupportFragmentManager().beginTransaction()
                    .add(loginContainer.getId(), new LoginFragment())
                    .commit();
        }
    }
}
