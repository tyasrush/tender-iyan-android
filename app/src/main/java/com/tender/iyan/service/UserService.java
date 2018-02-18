package com.tender.iyan.service;

import android.os.Handler;
import android.os.Looper;
import com.tender.iyan.entity.User;
import com.tender.iyan.service.config.Api;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserService {

    private static UserService instance;
    private OkHttpClient client = new OkHttpClient();
    private RequestBody body;
    private Request request;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
            //baris ini -> statement untuk inisiasi HttpLoggingInterceptor untuk keperluan log data yang didapat dari server
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            //baris ini -> statement untuk set level dari log ini
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //baris ini -> statement untuk inisiasi variabel client dengan interceptor
            instance.client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        }
        return instance;
    }

    //untuk melakukan pengecekan user ke server
    public void login(final LoginView view, final User user) {
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new FormBody.Builder()
                .add("email", user.getEmail())
                .add("password", user.getPassword())
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(Api.BASE_URL + Api.USER_LOGIN)
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
                        view.onLoginFailed(e.getMessage());
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    JSONObject dataObject = jsonArray.getJSONObject(0);
                                    User result = new User();
                                    result.setId(dataObject.getInt("id"));
                                    view.onLoginSuccess(result);
                                } else {
                                    view.onLoginFailed("Akun tidak valid");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                view.onLoginFailed("Akun tidak valid");
                            }

                        }
                    }
                });
            }
        });
    }

    //untuk melakukan pendaftaran user baru
    public void signUp(final SignUpView view, User user) {
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
                .url(Api.BASE_URL + Api.USER_SIGN_UP)
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
                        view.onSignUpFailed(e.getMessage());
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
                                    view.onSignUpSuccess();
                                } else {
                                    view.onSignUpFailed("Daftar gagal");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                view.onSignUpFailed("Daftar gagal");
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
                .url(Api.BASE_URL + Api.USER_GET)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    JSONObject dataObject = jsonArray.getJSONObject(0);
                                    User result = new User();
                                    result.setName(dataObject.getString("nama"));
                                    result.setEmail(dataObject.getString("email"));
                                    result.setContact(dataObject.getString("contact"));
                                    result.setAlamat(dataObject.getString("alamat"));
                                    showView.onLoadSuccess(result);
                                } else {
                                    showView.onLoadFailed("Data tidak ditemukan");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                showView.onLoadFailed("Data tidak ditemukan");
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
