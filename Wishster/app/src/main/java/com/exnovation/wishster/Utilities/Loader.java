package com.exnovation.wishster.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.exnovation.wishster.R;

public class Loader extends AppCompatActivity {
    public static boolean isActive = false;
    //  private AlertDialog.Builder alertDialog;
    // private AlertDialog ad;
    Dialog dialog;

    @Override
    protected void onResume() {
        super.onResume();

        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showDialog(Context context) {

      /*  dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_gif_layout, false)
               // .content("Loading...")
                //.progress(true, 0)
                .cancelable(false)
                .build();

        dialog.show();
      *//* alertDialog=new AlertDialog.Builder(context) ;
       alertDialog.setView(R.layout.dialog_gif_layout);
       alertDialog.setMessage("Loading...");
       alertDialog.setCancelable(false);
       //ad=new AlertDialog();
       ad = alertDialog.show();*/
        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loader_layout);
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.show();

    }

    public void hideDialog() {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        // if (alertDialog != null && alertDialog.is)
        //alertDialog.setOnCancelListener(new )
        //ad.dismiss();
    }


}
