package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exnovation.wishster.Models.LoginModel;
import com.exnovation.wishster.Models.ReviewQuestion;
import com.exnovation.wishster.Models.SaveQuestion;
import com.exnovation.wishster.Models.ValueSet;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Loader;
import com.exnovation.wishster.Utilities.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.exnovation.wishster.Utilities.App;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Arrays;


public class Login extends AppCompatActivity {
    CheckBox checkBox1;
    static Loader loader;
    RequestBody bodyRequest;
    Call<LoginModel> coll=null;
    ApiManager apiManager=new ApiManager();
    Button btnSignin;
    TextView email_or_phone_no,tv_forgot,skip;
    float density=0f;
    JSONObject userLoginData=new JSONObject();
    Prefs prefs;
    RelativeLayout main_container;
    LinearLayout fbBtn;
    boolean doubleBackToExitPressedOnce = false;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    int RC_SIGN_IN=0;
    int FB_SIGN_IN=1;
    String TAG="loginActivity_test";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loader=new Loader();
        prefs=new Prefs(this);
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());*/

      //  callbackManager = CallbackManager.Factory.create();


       // loginButton.setReadPermissions(Arrays.asList(EMAIL));



        App.bool=false;

        init();

        fbBtn = findViewById(R.id.facebook_btn);

        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLogin();


            }
        });





        Action();
        GoogleLogin();
    }

    private void init()
    {
        main_container=findViewById(R.id.main_container);
        checkBox1=findViewById(R.id.checkBox1);
        tv_forgot= findViewById(R.id.tv_forgot);
        skip= findViewById(R.id.tv_skip);
        btnSignin = findViewById(R.id.btn_signin);

        email_or_phone_no = findViewById(R.id.email_or_phone_no);
    }

    private void Action() {



        btnSignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Validate())
                {
                    try
                    {
                        prefs.saveData("social_types","0");
                        if (prefs.getData("social_types").equals("0")){
                            App.social_types=false;
                        }
                        userLoginData.put("email",(((TextView)findViewById(R.id.et_email)).getText()));
                        userLoginData.put("password",(((TextView)findViewById(R.id.et_psw)).getText()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LoginServerCall(false);
                }

            }
        });
        skip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this, HomeActivity.class));
            }
        });
        email_or_phone_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkBox1.isChecked())
                startActivity(new Intent(Login.this, OtpActivity.class));
                else
                    Toast.makeText(Login.this, "Checked terms and privacy policy", Toast.LENGTH_SHORT).show();
            }
        });


        tv_forgot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });

        ((TextView)findViewById(R.id.et_psw)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (Validate())
                    {
                        try
                        {
                            userLoginData.put("email",(((TextView)findViewById(R.id.et_email)).getText()));
                            userLoginData.put("password",(((TextView)findViewById(R.id.et_psw)).getText()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LoginServerCall(false);
                    }
                    return true;
                }
                return false;
            }
        });
        /*main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ValueSet(Login.this);
            }
        });*/

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private void fbLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        //App.social_types=true;
                                        prefs.saveData("social_types","1");
                                        prefs.saveData("social_login","FB");

                                        if (prefs.getData("social_types").equals("1")){
                                            App.social_types=true;
                                        }

                                        try {
                                            String profileName = object.get("name").toString();
                                            final String id = object.get("id").toString();
                                            userLoginData.put( "social_type", "facebook");
                                            userLoginData.put( "social_id", object.get("id").toString());
                                            userLoginData.put( "name", object.get("name"));
                                            userLoginData.put( "email", object.get("email"));
                                            userLoginData.put( "mobile", "");
                                            userLoginData.put( "birthday",object.get(""));
                                            userLoginData.put( "profile_img_url", object.get(""));
                                            Toast.makeText(Login.this, ""+profileName, Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        LoginServerCall(true);
                                        Log.v("MainData", response.toString());
                                        Log.d("AAAA", object.toString());
                                        // Toast.makeText(Login.this, response.toString(), Toast.LENGTH_SHORT).show();
                                        // setProfileToView(object);
                                        //setProfileToView(object);
                                        //   LoginManager.getInstance().logOut();

                                    }
                                });
                        //Log.d("ABC",callbackManager.toString());
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                       // Toast.makeText(Login.this, "Cancle login with Facebook", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("AAAAAA", exception.toString());
                        Toast.makeText(Login.this, "Facebook Login Cancel.Email Id Not Found.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean Validate()
    {
        if ((((TextView)findViewById(R.id.et_email)).getText().toString().length() == 0))
        {
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((((TextView)findViewById(R.id.et_psw)).getText().toString().length() == 0))
        {
            Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void LoginServerCall(boolean status)
    {

        loader.showDialog(this);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), userLoginData.toString());
        if (status)
        coll=apiManager.service.Social_login(bodyRequest);
        else
        coll=apiManager.service.login(bodyRequest);
        coll.enqueue(new Callback<LoginModel>()
        {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response)
            {

                App.token = response.headers().get("token");
               // Toast.makeText(Login.this, ""+App.token, Toast.LENGTH_SHORT).show();
                loader.hideDialog();
                if (response.body().isStatus())
                {
                    App.UserId=response.body().getUserDetails().getId()+"";

                    if (response.body().getUserDetails().getStatus()==0) {
                        App.bool=true;

                        prefs.saveData(getString(R.string.TOKEN),App.token);
                        prefs.saveData("User_name", response.body().getUserDetails().getName());
                        prefs.saveData("prf_pic", response.body().getUserDetails().getProfile_img());
                        startActivity(new Intent(Login.this, HomeActivity.class));
                        finish();
                    }
                    else {
                        prefs.saveData(getString(R.string.TOKEN),App.token);
                        prefs.saveData("User_name", response.body().getUserDetails().getName());
                        prefs.saveData("prf_pic", response.body().getUserDetails().getProfile_img());
                        startActivity(new Intent(Login.this, ReviewActivity.class));
                        finish();
                    }
                }
                else {
                    Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                if (response.body().getUserDetails().getStatus()==1) {
                    startActivity(new Intent(Login.this, ReviewActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t)
            {
                Log.d("errMsg",""+t.getMessage());
                loader.hideDialog();
                Toast.makeText(Login.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void GoogleLogin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        App.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        LinearLayout signInButton = findViewById(R.id.google_signup);
        findViewById(R.id.google_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = App.mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (result.isSuccess())
            handleSignInResult(task);
        }else if (callbackManager.onActivityResult(requestCode, resultCode, data)) {

            // mCallbackManager.onActivityResult(requestCode, resultCode, data);
            return ;
        }

    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            loader.showDialog(Login.this);
            prefs.saveData("social_login","Google");
            prefs.saveData("social_types","1");
            if (prefs.getData("social_types").equals("1")){
                App.social_types=true;
            }
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            userLoginData.put( "social_type", "google");
            userLoginData.put( "social_id", account.getId());
            userLoginData.put( "name", account.getDisplayName());
            userLoginData.put( "email", account.getEmail());
            userLoginData.put( "mobile", "");
            userLoginData.put( "birthday","");
            userLoginData.put( "profile_img_url", account.getPhotoUrl());

            LoginServerCall(true);


            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException | JSONException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
          //  Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
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
}
