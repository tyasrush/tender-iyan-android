package com.tender.iyan.service.api;

/**
 * Created by tyas on 10/4/17.
 */

public interface Api {
    public static final String USER_LOGIN = "user/login.php";
    public static final String USER_SIGN_UP = "user/save.php";
//    public static final String USER_FORGOT = "user/forgot.php";
    public static final String USER_GET = "user/get.php";
//    public static final String USER_UPDATE = "user/update.php";
    public static final String USER_LIST = "user/list.php";

    public static final String TENDER_SAVE = "tender/save.php";
    public static final String TENDER_GET = "tender/get.php";
    public static final String TENDER_SEARCH = "tender/search.php";
    public static final String TENDER_LIST = "tender/list.php";

    public static final String PENAWARAN_SAVE = "penawaran/save.php";
    public static final String PENAWARAN_LIST = "penawaran/list.php";

    public static final String KATEGORI_LIST = "kategori/list.php";

    public static final String DEAL_SAVE = "deal/save.php";
}
