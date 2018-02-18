package com.tender.iyan.service;

import android.os.Handler;
import android.os.Looper;
import com.tender.iyan.entity.Deal;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.entity.User;
import com.tender.iyan.service.config.Api;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tyas on 2/17/18.
 */

public class DealsService {
  private static DealsService instance;
  private OkHttpClient client = new OkHttpClient();
  private RequestBody body;
  private Request request;

  private final String TAG = getClass().getName();

  public static DealsService getInstance() {
    if (instance == null) {
      instance = new DealsService();
      //baris ini -> statement untuk inisiasi HttpLoggingInterceptor untuk keperluan log data yang didapat dari server
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      //baris ini -> statement untuk set level dari log ini
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      //baris ini -> statement untuk inisiasi variabel client dengan interceptor
      instance.client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    return instance;
  }

  //untuk mengambil semua tender yang ada pada server
  public void getAll(final ListDealView view, User user) {
    String url = Api.BASE_URL + Api.DEAL_LIST + "?id=" + user.getId();
    request = new Request.Builder().url(url).build();

    //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, final IOException e) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            view.onLoadFailed(e.getMessage());
          }
        });
      }

      @Override public void onResponse(Call call, final Response response) throws IOException {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            if (response.isSuccessful()) {
              try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getString("status").equals("success")) {
                  List<Deal> results = new ArrayList<>();
                  JSONArray jsonArray = jsonObject.getJSONArray("data");
                  if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                      JSONObject arrayValue = jsonArray.getJSONObject(i);
                      Deal deal = new Deal();
                      deal.setId(Integer.parseInt(arrayValue.getString("id")));
                      deal.setNamaPenawaran(arrayValue.getString("nama_penawaran"));
                      deal.setHarga(arrayValue.getString("harga"));
                      results.add(deal);
                    }
                  }

                  //untuk melakukan parsing json object kedalam model dengan perulangan
                  view.onLoadedDataSuccess(results);
                } else {
                  view.onLoadFailed("Data tidak ditemukan");
                }
              } catch (Exception e) {
                e.printStackTrace();
                view.onLoadFailed("Data tender tidak ditemukan");
              }
            }
          }
        });
      }
    });
  }

  //untuk mengambil semua tender yang ada pada server
  public void get(final ListDealView view, Deal param) {
    String url = Api.BASE_URL + Api.DEAL_GET + "?id=" + param.getId();
    request = new Request.Builder().url(url).build();

    //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, final IOException e) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            view.onLoadFailed(e.getMessage());
          }
        });
      }

      @Override public void onResponse(Call call, final Response response) throws IOException {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            if (response.isSuccessful()) {
              try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (jsonObject.getString("status").equals("success")) {
                  List<Deal> results = new ArrayList<>();
                  JSONArray jsonArray = jsonObject.getJSONArray("data");
                  if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                      JSONObject arrayValue = jsonArray.getJSONObject(i);
                      Deal deal = new Deal();
                      deal.setId(Integer.parseInt(arrayValue.getString("id")));
                      deal.setNamaPenawaran(arrayValue.getString("nama_penawaran"));
                      deal.setHarga(arrayValue.getString("harga"));
                      deal.setFoto(arrayValue.getString("foto"));
                      results.add(deal);
                    }
                  }

                  //untuk melakukan parsing json object kedalam model dengan perulangan
                  view.onLoadedDataSuccess(results);
                } else {
                  view.onLoadFailed("Data tidak ditemukan");
                }
              } catch (Exception e) {
                e.printStackTrace();
                view.onLoadFailed("Data tender tidak ditemukan");
              }
            }
          }
        });
      }
    });
  }

  //untuk menambahkan penawaran baru kedalam tender tertentu berdasarkan id tender ke dalam server
  public void upload(final UploadView view, Deal param) {
    File file = new File(param.getFoto());
    //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
    body = new MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("image", file.getName(),
            RequestBody.create(MediaType.parse("image/*"), file))
        .addFormDataPart("id", String.valueOf(param.getId()))
        .build();

    //request ini untuk menjalankan servis http ke server
    request = new Request.Builder().url(Api.BASE_URL + Api.DEAL_UPLOAD).post(body).build();

    //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, final IOException e) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            view.onAddedFailed(e.getMessage());
          }
        });
      }

      @Override public void onResponse(Call call, final Response response) throws IOException {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            if (response.isSuccessful()) {
              if (response.isSuccessful()) {
                try {
                  JSONObject jsonObject = new JSONObject(response.body().string());
                  if (jsonObject.getString("status").equals("success")) {
                    view.onAddedSuccess();
                  } else {
                    view.onAddedFailed("Terjadi kesalahan saat menyimpan");
                  }
                } catch (JSONException | IOException e) {
                  e.printStackTrace();
                  view.onAddedFailed("Terjadi kesalahan saat menyimpan");
                }
              } else {
                view.onAddedFailed("Terjadi kesalahan saat menyimpan");
              }
            } else {
              view.onAddedFailed("Terjadi kesalahan saat menyimpan");
            }
          }
        });
      }
    });
  }

  public interface ListDealView {
    void onLoadedDataSuccess(List<Deal> deals);

    void onLoadFailed(String message);
  }

  public interface UploadView {
    void onAddedSuccess();

    void onAddedFailed(String message);
  }
}
