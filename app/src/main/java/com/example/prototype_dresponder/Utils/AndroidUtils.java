package com.example.prototype_dresponder.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.prototype_dresponder.Login;

public class AndroidUtils {
    public static void showToast(Context context, String Txt){
        Toast.makeText(context,Txt,Toast.LENGTH_LONG).show();
    }
    public static ProgressDialog progressDialog(Context context){
        return new ProgressDialog(context);
    }
    public static void ProgressDialogMsg(Context context, String message){
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setMessage(message);
        progressdialog.show();
    }
    public static void closeDialog(Context context){
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.dismiss();

    }
}
