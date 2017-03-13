package com.tender.iyan.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by tyas on 8/14/16.
 */
public class DialogUtil {

    private static ProgressDialog progressDialog;

    public static DialogUtil getInstance(Context context) {
        progressDialog = new ProgressDialog(context);
        return new DialogUtil();
    }

    public void showProgressDialog(String title, String message, boolean cancelable) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }
}
