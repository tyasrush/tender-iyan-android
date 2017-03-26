package com.tender.iyan.service;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.tender.iyan.BuildConfig;
import com.tender.iyan.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class UserService {

    private OkHttpClient client = new OkHttpClient();
    private RequestBody body;
    private Request request;

    public static UserService getInstance() {
        return new UserService();
    }

    //untuk melakukan pengecekan user ke server
    public void login(final LoginView loginView, final User user) {
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new FormBody.Builder()
                .add("email", user.getEmail())
                .add("password", user.getPassword())
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + BuildConfig.LOGIN_URL)
                //http request post
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.onLoginFailed(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.getString("login_status").equals("success")) {
                                    User userResult = new User();
                                    userResult.setId(jsonObject.getInt("id"));
                                    loginView.onLoginSuccess(userResult);
                                } else {
                                    loginView.onLoginFailed("Akun invalid");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }

    //untuk melakukan pendaftaran user baru
    public void signUp(final SignUpView signUpView, User user) {
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new FormBody.Builder()
                .add("name", user.getName())
                .add("email", user.getEmail())
                .add("password", user.getPassword())
                .add("kontak", user.getContact())
                .add("alamat", user.getAlamat())
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + BuildConfig.SIGN_UP_URL)
                //http request post
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        signUpView.onSignUpFailed(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.getString("signup_status").equals("success")) {
                                    signUpView.onSignUpSuccess();
                                } else {
                                    signUpView.onSignUpFailed("Sign up invalid");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }

    //untuk menampilkan identitas user pada aplikasi ini
    public void show(final ShowView showView, User user) {
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new FormBody.Builder()
                .add("id", String.valueOf(user.getId()))
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + BuildConfig.USER_URL)
                //http request post
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showView.onLoadFailed(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.getString("status").equals("success")) {
                                    User result = new User();
                                    result.setName(jsonObject.getString("nama"));
                                    result.setEmail(jsonObject.getString("email"));
                                    result.setContact(jsonObject.getString("contact"));
                                    result.setAlamat(jsonObject.getString("alamat"));
                                    showView.onLoadSuccess(result);
                                } else {
                                    showView.onLoadFailed("Show user failed");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }

    public interface LoginView {
        void onLoginSuccess(User user);
        void onLoginFailed(String message);
    }

    public interface SignUpView {
        void onSignUpSuccess();
        void onSignUpFailed(String message);
    }

    public interface ShowView {
        void onLoadSuccess(User user);
        void onLoadFailed(String message);
    }
}
