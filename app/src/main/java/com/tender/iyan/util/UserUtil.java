package com.tender.iyan.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tyasrus on 13/07/16.
 */
public class UserUtil {

    public static final String ID_USER = "id_user";
    public static final String IS_LOGIN = "is_login";

    private static SharedPreferences sharedPreferences;

    public static UserUtil getInstance(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new UserUtil();
    }

    public void setLoginState(int id, boolean isLogin) {
        sharedPreferences.edit().putInt(ID_USER, id).apply();
        sharedPreferences.edit().putBoolean(IS_LOGIN, isLogin).apply();
    }

    public int getId() {
        return sharedPreferences.getInt(ID_USER, 0);
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        sharedPreferences.edit().putString(ID_USER, null).apply();
        sharedPreferences.edit().putBoolean(IS_LOGIN, false).apply();
    }
}
