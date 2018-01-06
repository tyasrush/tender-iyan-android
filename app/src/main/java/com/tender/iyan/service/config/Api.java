package com.tender.iyan.service.config;
/**
 * kelas ini berisi konstan string yang digunakan untuk tambahan url sesuai kebutuhan
 *
 * konstan adalah sebuah pernyataan yang tidak boleh diubah lagi
 */
public interface Api {
    //baris ini -> statement untuk menyimpan url sebagai url dasar dari semua service
    //String BASE_URL = "https://ryan-tender.000webhostapp.com/api/";
    String BASE_URL = "http://10.10.2.197/tender/api/";

    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk login pengguna
    String USER_LOGIN = "user/login.php";
    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk menyimpan data pengguna
    String USER_SIGN_UP = "user/save.php";
    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk mengambil data pengguna
    String USER_GET = "user/get.php";
    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk mengambil semua data pengguna
    String USER_LIST = "user/list.php";

    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk menyimpan data tender
    String TENDER_SAVE = "tender/save.php";
    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk mengambil data tender
    String TENDER_GET = "tender/get.php";
    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk melakukan pencarian data tender
    String TENDER_SEARCH = "tender/search.php";
    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk mengambil semua data tender
    String TENDER_LIST = "tender/list.php";

    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk menyimpan data penawaran
    String PENAWARAN_SAVE = "penawaran/save.php";
    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk mengambil semua data penawaran
    String PENAWARAN_LIST = "penawaran/list.php";

    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk mengambil semua data kategori
    String KATEGORI_LIST = "kategori/list.php";

    //baris ini -> statement untuk menyimpan url dari api yang diperlukan untuk menyimpan data deal
    String DEAL_SAVE = "deal/save.php";
}
