package com.exnovation.wishster.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Prefs;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    ImageView nav;
    CircleImageView profile_link;
    DrawerLayout drawerLayout;
    NavigationView navigation;
    TextView scanner_id;

    LinearLayout friendsLay, wishLay, logoutLay,menu_profile,login_lay;
    LinearLayout changePwdLay;
    Prefs prefs;
    Button btnAction;
    CircleImageView uImg;
    TextView uName;
    //scanner
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    String intentData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prefs=new Prefs(this);
        //prefs.clearAllData();
        nav = findViewById(R.id.nav_iv);
        profile_link = findViewById(R.id.iv_scan);
        uImg = findViewById(R.id.user_img);
        uName = findViewById(R.id.u_name);
        logoutLay = findViewById(R.id.logout_lay);
        drawerLayout = findViewById(R.id.drawer_layout);
        btnAction= findViewById(R.id.btnAction);
        navigation = findViewById(R.id.navigation);
        friendsLay = findViewById(R.id.my_friends_lay);
        wishLay = findViewById(R.id.wish_lay);
       // blockLay = findViewById(R.id.block_lay);
        changePwdLay = findViewById(R.id.change_pwd_lay);
        menu_profile=findViewById(R.id.menu_profile);
        login_lay=findViewById(R.id.login_lay);
        scanner_id=(TextView)findViewById(R.id.scanner_id);
        Log.d("ABCD", prefs.getData(getString(R.string.TOKEN)));
        if (prefs.getData(getString(R.string.TOKEN)).equals(""))
            App.loginStatus=true;
        else
            App.loginStatus=false;
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigation);
            }
        });

        menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Profile.class));
                drawerLayout.closeDrawer(navigation);
            }
        });
        wishLay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, WishListActivity.class));
                drawerLayout.closeDrawer(navigation);
            }
        });
        login_lay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Login.class));
                finish();
            }
        });

        profile_link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Profile.class));
                drawerLayout.closeDrawer(navigation);

            }
        });

        friendsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FriendCircleActivity.class));
                drawerLayout.closeDrawer(navigation);
            }
        });

       /* blockLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BlockedUsers.class));
            }
        });*/

        changePwdLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ChangePassword.class));
                drawerLayout.closeDrawer(navigation);
            }
        });
        logoutLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Login.class));
                signOutGoogle();
                LoginManager.getInstance().logOut();
                prefs.clearAllData();
                finish();
            }
        });

        uName.setText(prefs.getData("User_name"));
        if (prefs.getData("prf_pic") == null || prefs.getData("prf_pic").equals("")){
            Glide.with(HomeActivity.this).load(R.drawable.cover_ic)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(uImg);
            Glide.with(HomeActivity.this).load(R.drawable.cover_ic)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(profile_link);
        }else{
            Glide.with(HomeActivity.this).load(prefs.getData("prf_pic"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(uImg);
            Glide.with(HomeActivity.this).load(prefs.getData("prf_pic"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(profile_link);
        }
        if (!(App.loginStatus)) {
            uName.setText(prefs.getData("User_name"));
            if (prefs.getData("prf_pic") == null || prefs.getData("prf_pic").equals("")) {
                Glide.with(HomeActivity.this).load(R.drawable.cover_ic).into(uImg);
            } else {
                Glide.with(HomeActivity.this).load(prefs.getData("prf_pic")).into(uImg);
            }
            login_lay.setVisibility(View.GONE);
            showHrLineStatus(View.VISIBLE);
            if (!(App.social_types)){
                changePwdLay.setVisibility(View.VISIBLE);
                ((View)findViewById(R.id.v5)).setVisibility(View.VISIBLE);
            }
            else {
                changePwdLay.setVisibility(View.GONE);
                ((View)findViewById(R.id.v5)).setVisibility(View.GONE);
            }
        }
        else {
            login_lay.setVisibility(View.VISIBLE);
            showHrLineStatus(View.GONE);
        }
        //Scanner
        initViews();
        initialiseDetectorsAndSources();

    }

    private void showHrLineStatus(int visible) {

        menu_profile.setVisibility(visible);
        wishLay.setVisibility(visible);
        profile_link.setVisibility(visible);
        friendsLay.setVisibility(visible);
        //blockLay.setVisibility(visible);
        changePwdLay.setVisibility(visible);
        logoutLay.setVisibility(visible);
        //((View)findViewById(R.id.v1)).setVisibility(visible);
        ((View)findViewById(R.id.v2)).setVisibility(visible);
        ((View)findViewById(R.id.v3)).setVisibility(visible);
        ((View)findViewById(R.id.v4)).setVisibility(visible);
        ((View)findViewById(R.id.v5)).setVisibility(visible);
        //((View)findViewById(R.id.v6)).setVisibility(visible);


    }

    @Override
    protected void onResume() {
        super.onResume();
        uName.setText(prefs.getData("User_name"));
        if (prefs.getData("prf_pic") == null || prefs.getData("prf_pic").equals("")){
            Glide.with(HomeActivity.this).load(R.drawable.cover_ic)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(uImg);
            Glide.with(HomeActivity.this).load(R.drawable.cover_ic)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(profile_link);
        }else{
            Glide.with(HomeActivity.this).load(prefs.getData("prf_pic"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(uImg);
            Glide.with(HomeActivity.this).load(prefs.getData("prf_pic"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(profile_link);
        }


    }

    //scanner
    private void initViews() {
        txtBarcodeValue = (TextView) findViewById(R.id.txtBarcodeValue);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        btnAction = (Button) findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentData.length() > 0) {
                    btnAction.setText("Scan");
                   // if (!(App.loginStatus))
                        startActivity(new Intent(HomeActivity.this, ProductListActivity.class).putExtra("barcode", txtBarcodeValue.getText().toString()));
                   /* else
                        Toast.makeText(HomeActivity.this, "You will be able to scan after login", Toast.LENGTH_SHORT).show();*/
                    
                }

            }
        });
    }
    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(HomeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(HomeActivity.this, "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {

                            btnAction.setText("Search");
                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);

                        }
                    });

                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));

                }
                return;

            }
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private void signOutGoogle() {
        App.mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Sign out
                    }
                });
    }
}
