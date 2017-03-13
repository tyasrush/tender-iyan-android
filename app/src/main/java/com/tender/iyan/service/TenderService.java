package com.tender.iyan.service;

import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;

import com.tender.iyan.BuildConfig;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.entity.User;

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

    //untuk mengambil semua tender yang ada pad server
    public void getAll(final ListHomeView listHomeView, User user) {
        if (user.getId() != 0) {
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id", String.valueOf(user.getId()))
                    .build();

            request = new Request.Builder()
                    .url(BuildConfig.BASE_URL + BuildConfig.LIST_TENDER_URL)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(BuildConfig.BASE_URL + BuildConfig.LIST_TENDER_URL)
                    .build();
        }


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
                                if (jsonObject.getString("list_status").equals("success")) {
                                    List<Tender> tenders = new ArrayList<>();
                                    JSONArray array = jsonObject.getJSONArray("data");
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
                                        tenders.add(tender);
                                    }

                                    listHomeView.onLoadedDataSuccess(tenders);
                                } else {
                                    listHomeView.onLoadFailed("Akun invalid");
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

    //untuk mengambil semua tender yang ada pada server dengan urutan alphabet
    public void getAllbyFilter(final ListHomeView listHomeView, int state) {
        if (state == ALPHABET) {
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("alphabet", String.valueOf(true))
                    .build();
        }

        if (state == CHEAPEST) {
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("termurah", String.valueOf(true))
                    .build();
        }

        if (state == MOST_EXPENSIVE) {
            body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("termahal", String.valueOf(true))
                    .build();
        }

        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + BuildConfig.LIST_FILTER_TENDER_URL)
                .post(body)
                .build();

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
                                if (jsonObject.getString("list_status").equals("success")) {
                                    List<Tender> tenders = new ArrayList<>();
                                    JSONArray array = jsonObject.getJSONArray("data");
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
                                        tenders.add(tender);
                                    }

                                    listHomeView.onLoadedDataSuccess(tenders);
                                } else {
                                    listHomeView.onLoadFailed("Akun invalid");
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

    public void upload(final AddView addView, Tender tender) {
        File file = new File(tender.getFoto());
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .addFormDataPart("id_user", String.valueOf(tender.getIduser()))
                .addFormDataPart("name", tender.getName())
                .addFormDataPart("deskripsi", tender.getDeskripsi())
                .addFormDataPart("anggaran", String.valueOf(tender.getAnggaran()))
                .addFormDataPart("waktu", tender.getWaktu())
                .build();

        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + BuildConfig.ADD_TENDER_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        addView.onAddedFailed(e.getMessage());
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
                                if (!jsonObject.getBoolean("error")) {
                                    addView.onAddedSuccess();
                                } else {
                                    addView.onAddedFailed("file not good");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            addView.onAddedFailed("Add Tender Invalid");
                        }
                    }
                });
            }
        });
    }

    public void uploadPenawaran(final UploadPenawaranView uploadPenawaranView, Penawaran penawaran) {
        File file = new File(penawaran.getFoto());
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .addFormDataPart("id_user", String.valueOf(penawaran.getIdUser()))
                .addFormDataPart("id_request", String.valueOf(penawaran.getIdTender()))
                .addFormDataPart("name", penawaran.getNama())
                .addFormDataPart("deskripsi", penawaran.getDeskripsi())
                .addFormDataPart("harga", String.valueOf(penawaran.getHarga()))
                .build();

        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + BuildConfig.ADD_PENAWARAN_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        uploadPenawaranView.onAddedFailed(e.getMessage());
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
                                if (!jsonObject.getBoolean("error")) {
                                    uploadPenawaranView.onAddedSuccess();
                                } else {
                                    uploadPenawaranView.onAddedFailed("file not good");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            uploadPenawaranView.onAddedFailed("Add Tender Invalid");
                        }
                    }
                });
            }
        });
    }

    public void getPenawaranByTender(final GetPenawaranView getPenawaranView, Tender tender) {
        body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_request", String.valueOf(tender.getId()))
                .build();

        request = new Request.Builder()
                .url(BuildConfig.BASE_URL + BuildConfig.DETAIL_TENDER_URL)
                .post(body)
                .build();

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
                                if (jsonObject.getString("list_status").equals("success")) {
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

                                    getPenawaranView.onLoadDataSuccess(penawaranList);
                                } else {
                                    getPenawaranView.onLoadFailed("Akun invalid");
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
}
