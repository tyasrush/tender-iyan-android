package com.tender.iyan.service;

import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;

import com.tender.iyan.BuildConfig;
import com.tender.iyan.entity.Kategori;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.entity.User;
import com.tender.iyan.service.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Service yang mengatur dan memanage data ke server
 * kelas ini hanya untuk service yang berhubungan dengan tender
 */
public class TenderService {

    public static final int ALPHABET = 0;
    public static final int CHEAPEST = 1;
    public static final int MOST_EXPENSIVE = 2;

    private OkHttpClient client = new OkHttpClient();
    private RequestBody body;
    private Request request;

    private final String TAG = getClass().getName();

    public static TenderService getInstance() {
        return new TenderService();
    }

    //untuk mengambil semua tender yang ada pada server
    public void getAll(final ListHomeView view, User user) {
        String url = user != null ? BuildConfig.BASE_URL + Api.TENDER_LIST + "?id=" + user.getId() : BuildConfig.BASE_URL + Api.TENDER_LIST;
        request = new Request.Builder()
                .url(url)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        view.onLoadFailed(e.getMessage());
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
                                    List<Tender> results = new ArrayList<>();
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject arrayValue = jsonArray.getJSONObject(i);
                                            Tender tender = new Tender();
                                            tender.setId(Integer.parseInt(arrayValue.getString("id")));
                                            tender.setIduser(arrayValue.getInt("id_user"));
                                            tender.setName(arrayValue.getString("nama"));
                                            tender.setFoto(arrayValue.getString("foto"));
                                            tender.setDeskripsi(arrayValue.getString("deskripsi"));
                                            tender.setAnggaran(Integer.parseInt(arrayValue.getString("anggaran")));
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simpleDateFormat.parse(arrayValue.getString("waktu"));
                                            String tanggal = DateFormat.format("dd", date) + "-" + DateFormat.format("MM", date) + "-" + DateFormat.format("yyyy", date);
                                            tender.setWaktu(tanggal);
                                            Kategori kategori = new Kategori();
                                            kategori.setId(arrayValue.getInt("id_kategori"));
                                            kategori.setNama(arrayValue.getString("nama_kategori"));
                                            tender.setKategori(kategori);
                                            results.add(tender);
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

    //untuk mengambil semua kategori dari server
    public void getKategoris(final ListKategoriView view) {
        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.KATEGORI_LIST)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        view.onLoadFailed(e.getMessage());
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
                                    List<Kategori> kategoris = new ArrayList<>();
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject arrayValue = array.getJSONObject(i);
                                        Kategori kategori = new Kategori();
                                        kategori.setId(Integer.parseInt(arrayValue.getString("id")));
                                        kategori.setNama(arrayValue.getString("nama"));
                                        kategoris.add(kategori);
                                    }

                                    view.onLoadedDataSuccess(kategoris);
                                } else {
                                    view.onLoadFailed("Data kategori tidak ditemukan");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                view.onLoadFailed("Data kategori tidak ditemukan");
                            }

                        }
                    }
                });
            }
        });
    }

    //untuk mengambil semua tender yang ada pada server dengan filter : alphabet, termurah, termahal
    public void getAllbyFilter(final ListHomeView view, int state) {
        if (state == ALPHABET) {
            //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("alphabet", String.valueOf(true))
                    .build();
        }

        if (state == CHEAPEST) {
            //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("termurah", String.valueOf(true))
                    .build();
        }

        if (state == MOST_EXPENSIVE) {
            //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("termahal", String.valueOf(true))
                    .build();
        }

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.TENDER_LIST)
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        view.onLoadFailed(e.getMessage());
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
                                    List<Tender> tenders = new ArrayList<>();
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    //perulangan untuk mengubah jsonobject ke dalam model tender
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject arrayValue = array.getJSONObject(i);
//                                        Log.d(TAG, "json object : " + jsonObject.toString());
                                        Tender tender = new Tender();
                                        tender.setId(Integer.parseInt(arrayValue.getString("id")));
                                        tender.setIduser(arrayValue.getInt("id_user"));
                                        tender.setName(arrayValue.getString("nama"));
                                        tender.setFoto(arrayValue.getString("foto"));
                                        tender.setDeskripsi(arrayValue.getString("deskripsi"));
                                        tender.setAnggaran(Integer.parseInt(arrayValue.getString("anggaran")));
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = simpleDateFormat.parse(arrayValue.getString("waktu"));
                                        String tanggal = DateFormat.format("dd", date) + "-" + DateFormat.format("MM", date) + "-" + DateFormat.format("yyyy", date);
                                        tender.setWaktu(tanggal);
                                        JSONObject kategoriObject = arrayValue.getJSONObject("kategori");
                                        Kategori kategori = new Kategori();
                                        kategori.setId(kategoriObject.getInt("id"));
                                        kategori.setNama(kategoriObject.getString("nama"));
                                        tender.setKategori(kategori);
                                        tenders.add(tender);
                                    }

                                    view.onLoadedDataSuccess(tenders);
                                } else {
                                    view.onLoadFailed("Akun invalid");
                                }
                            } catch (JSONException | IOException | ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }

    //untuk menambahkan tender baru ke dalam server
    public void upload(final AddView view, Tender tender) {
        File file = new File(tender.getFoto());
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .addFormDataPart("id_user", String.valueOf(tender.getIduser()))
                .addFormDataPart("name", tender.getName())
                .addFormDataPart("deskripsi", tender.getDeskripsi())
                .addFormDataPart("anggaran", String.valueOf(tender.getAnggaran()))
                .addFormDataPart("waktu", tender.getWaktu())
                .addFormDataPart("id_kategori", String.valueOf(tender.getIdKategori()))
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.TENDER_SAVE)
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        view.onAddedFailed(e.getMessage());
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
                    }
                });
            }
        });
    }

    //untuk menambahkan penawaran baru kedalam tender tertentu berdasarkan id tender ke dalam server
    public void uploadPenawaran(final UploadPenawaranView view, Penawaran penawaran) {
        File file = new File(penawaran.getFoto());
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .addFormDataPart("id_user", String.valueOf(penawaran.getIdUser()))
                .addFormDataPart("id_request", String.valueOf(penawaran.getIdTender()))
                .addFormDataPart("name", penawaran.getNama())
                .addFormDataPart("deskripsi", penawaran.getDeskripsi())
                .addFormDataPart("harga", String.valueOf(penawaran.getHarga()))
                .addFormDataPart("lat", String.valueOf(penawaran.getLat()))
                .addFormDataPart("lng", String.valueOf(penawaran.getLng()))
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.PENAWARAN_SAVE)
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        view.onAddedFailed(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
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

    //untuk mengubah status tender dan penawaran
    public void uploadDeal(final SaveDealView view, int idRequest, int idPenawaran) {
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_request", String.valueOf(idRequest))
                .addFormDataPart("id_penawaran", String.valueOf(idPenawaran))
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.DEAL_SAVE)
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null)
                            view.onSaveFailed(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    if (jsonObject.getString("status").equals("success")) {
                                        view.onSaveSuccess();
                                    } else {
                                        view.onSaveFailed("Terjadi kesalahan saat menyimpan");
                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                    view.onSaveFailed("Terjadi kesalahan saat menyimpan");
                                }
                            } else {
                                view.onSaveFailed("Terjadi kesalahan saat menyimpan");
                            }
                        } else {
                            view.onSaveFailed("Terjadi kesalahan saat menyimpan");
                        }
                    }
                });
            }
        });
    }

    //untuk mengambil semua penawaran yang ada pada tender sesuai dengan id tender
    public void getPenawaranByTender(final GetPenawaranView getPenawaranView, Tender tender) {
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_request", String.valueOf(tender.getId()))
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.TENDER_GET)
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        getPenawaranView.onLoadFailed(e.getMessage());
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
                                    List<Penawaran> penawaranList = new ArrayList<>();
                                    JSONArray array = jsonObject.getJSONArray("data");
//                                    Log.d(TAG, "josn raw : " + jsonObject.getJSONArray("data"));
                                    //perulangan untuk mengubah jsonobject ke dalam model penawaran
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject arrayValue = array.getJSONObject(i);
                                        Penawaran penawaran = new Penawaran();
                                        penawaran.setId(Integer.parseInt(arrayValue.getString("id")));
                                        penawaran.setIdUser(arrayValue.getInt("id_user"));
                                        penawaran.setFoto(arrayValue.getString("foto"));
                                        penawaran.setNama(arrayValue.getString("nama"));
                                        penawaran.setDeskripsi(arrayValue.getString("deskripsi"));
                                        penawaran.setHarga(arrayValue.getInt("harga"));
                                        penawaran.setLat(arrayValue.getDouble("lat"));
                                        penawaran.setLng(arrayValue.getDouble("lng"));
                                        penawaranList.add(penawaran);
                                    }

                                    getPenawaranView.onLoadDataSuccess(penawaranList);
                                } else {
                                    getPenawaranView.onLoadFailed("Data tidak ditemukan");
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

    //untuk mengurutkan atau filter penawaran yang ada pada tender tertentu
    public void filterPenawaranByTender(final GetPenawaranView view, Tender tender, int state) {
        //FormBody.Builder ini bertujuan untuk mengirim data dengan beberapa parameter ke server
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_request", String.valueOf(tender.getId()));

        if (state == ALPHABET) {
            builder.setType(MultipartBody.FORM)
                    .addFormDataPart("alphabet", String.valueOf(true));
        }

        if (state == CHEAPEST) {
            builder.setType(MultipartBody.FORM)
                    .addFormDataPart("termurah", String.valueOf(true));
        }

        if (state == MOST_EXPENSIVE) {
            builder.setType(MultipartBody.FORM)
                    .addFormDataPart("termahal", String.valueOf(true));
        }

        body = builder.build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.PENAWARAN_LIST)
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        view.onLoadFailed(e.getMessage());
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
                                    List<Penawaran> penawaranList = new ArrayList<>();
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject arrayValue = array.getJSONObject(i);
                                        Penawaran penawaran = new Penawaran();
                                        penawaran.setId(Integer.parseInt(arrayValue.getString("id")));
                                        penawaran.setIdUser(arrayValue.getInt("id_user"));
                                        penawaran.setFoto(arrayValue.getString("foto"));
                                        penawaran.setNama(arrayValue.getString("nama"));
                                        penawaran.setDeskripsi(arrayValue.getString("deskripsi"));
                                        penawaran.setHarga(arrayValue.getInt("harga"));
                                        penawaranList.add(penawaran);
                                    }

                                    view.onLoadDataSuccess(penawaranList);
                                } else {
                                    view.onLoadFailed("Daftar penawaran tidak ditemukan");
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

    //untuk melakukan pencarian tender berdasarkan nama tender
    public void searchTender(final ListHomeView listHomeView, String param) {
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("q", param)
                .build();

        //request ini untuk menjalankan servis http ke server
        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + Api.TENDER_SEARCH)
                .post(body)
                .build();

        //untuk melakukan asynchronous call, agar pengambilan data tidak dieksekusi di foreground android atau antar muka android
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listHomeView.onLoadFailed(e.getMessage());
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
                                    List<Tender> tenders = new ArrayList<>();
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    if (array != null) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject arrayValue = array.getJSONObject(i);
                                            Log.d(TAG, "json object : " + jsonObject.toString());
                                            Tender tender = new Tender();
                                            tender.setId(Integer.parseInt(arrayValue.getString("id")));
                                            tender.setIduser(arrayValue.getInt("id_user"));
                                            tender.setName(arrayValue.getString("nama"));
                                            tender.setFoto(arrayValue.getString("foto"));
                                            tender.setDeskripsi(arrayValue.getString("deskripsi"));
                                            tender.setAnggaran(Integer.parseInt(arrayValue.getString("anggaran")));
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = simpleDateFormat.parse(arrayValue.getString("waktu"));
                                            String tanggal = DateFormat.format("dd", date) + "-" + DateFormat.format("MM", date) + "-" + DateFormat.format("yyyy", date);
                                            tender.setWaktu(tanggal);
                                            JSONObject kategoriObject = arrayValue.getJSONObject("kategori");
                                            Kategori kategori = new Kategori();
                                            kategori.setId(kategoriObject.getInt("id"));
                                            kategori.setNama(kategoriObject.getString("nama"));
                                            tender.setKategori(kategori);
                                            tenders.add(tender);
                                        }
                                    }

                                    listHomeView.onLoadedDataSuccess(tenders);
                                } else {
                                    listHomeView.onLoadFailed("Data tidak ditemukan");
                                }
                            } catch (JSONException | IOException | ParseException e) {
                                e.printStackTrace();
                                listHomeView.onLoadFailed("Data tidak ditemukan");
                            }
                        }
                    }
                });
            }
        });
    }

    public interface ListKategoriView {
        void onLoadedDataSuccess(List<Kategori> kategoris);

        void onLoadFailed(String message);
    }

    public interface ListHomeView {
        void onLoadedDataSuccess(List<Tender> tenders);

        void onLoadFailed(String message);
    }

    public interface AddView {
        void onAddedSuccess();

        void onAddedFailed(String message);
    }

    public interface GetPenawaranView {
        void onLoadDataSuccess(List<Penawaran> penawaranList);

        void onLoadFailed(String message);
    }

    public interface UploadPenawaranView {
        void onAddedSuccess();

        void onAddedFailed(String message);
    }

    public interface SaveDealView {
        void onSaveSuccess();

        void onSaveFailed(String message);
    }
}
