package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Prefs;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SplashActivity extends AppCompatActivity {
    Prefs prefs;
    private static int SPLASH_TIME_OUT = 1000;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs=new Prefs(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }
        else {
            navigationMethod();
        }
        }

    void navigationMethod(){
        new Thread(new Runnable() {
            public void run() {
                for (int progress = 0; progress < 100; progress += 10) {
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                       /* e.printStackTrace();
                        Timber.e(e.getMessage());*/
                    }
                }
                if (!(prefs.getData(getString(R.string.TOKEN)).equals("")))
                {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    App.mGoogleSignInClient = GoogleSignIn.getClient(SplashActivity.this, gso);

                    App.bool=true;
                    App.token=prefs.getData(getString(R.string.TOKEN));
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, Login.class));
                    finish();
                }
                }
        }).start();
    }
    //checkPermission
    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.CAMERA)+
                ContextCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
               + ContextCompat.checkSelfPermission(
                SplashActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage("Camera and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                SplashActivity.this,
                                new String[]
                                        {
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                        },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        SplashActivity.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {
            navigationMethod();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]+grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                //    Toast.makeText(SplashActivity.this,"Permissions granted.",Toast.LENGTH_SHORT).show();
                }else {
                    // Permissions are denied
                  //  Toast.makeText(SplashActivity.this,"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                if (!(prefs.getData(getString(R.string.TOKEN)).equals("")))
                {

                    App.bool=true;
                    App.token=prefs.getData(getString(R.string.TOKEN));
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, Login.class));
                    finish();
                }
                return;
            }
        }
    }
}


