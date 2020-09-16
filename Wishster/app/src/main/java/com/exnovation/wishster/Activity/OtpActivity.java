package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exnovation.wishster.Models.LoginModel;
import com.exnovation.wishster.Models.SaveQuestion;
import com.exnovation.wishster.Models.ServerStatus;
import com.exnovation.wishster.Models.ValueSet;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Loader;
import com.exnovation.wishster.Utilities.Prefs;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {
    static Loader loader;
    RequestBody bodyRequest;
    Call<ServerStatus> coll=null;
    ApiManager apiManager=new ApiManager();
    Button send_otp;
    EditText phnOrMail;
    String phn = "";
    Prefs prefs;
    JSONObject emailJson=new JSONObject();
    String type = "";
    RelativeLayout main_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        loader=new Loader();
        init();
        prefs = new Prefs(OtpActivity.this);
        action();

    }

    private void action() {
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phn = phnOrMail.getText().toString();
                if (isValidMail(phn)) {
                    try {
                        App.isEmail=true;
                        type = "email";
                        sighUpWithEmail();

                        prefs.saveData("phn", phn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (!isValidMobile(phn)){
                    Toast.makeText(OtpActivity.this, "Please enter a valid phone number...", Toast.LENGTH_SHORT).show();
                }else{

                    type = "mobile";

                    prefs.saveData("phn", phn);
                    try {
                        sighUpWithEmail();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    App.isEmail=false;
                    //startActivity(new Intent(OtpActivity.this, SubmitOTPActivity.class));
                }

            }
        });
        phnOrMail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    send_otp.performClick();
                    return true;
                }
                return false;
            }
        });
       /* main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ValueSet(OtpActivity.this);
            }
        });*/
    }

    private void sighUpWithEmail() throws JSONException {
        emailJson.put("email_mobile", phn);
        emailJson.put("type", type);
        loader.showDialog(this);
       
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), emailJson.toString());
        coll=apiManager.service.Register_email(bodyRequest);
        coll.enqueue(new Callback<ServerStatus>()
        {
            @Override
            public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response)
            {


                loader.hideDialog();
                if (response.body().isStatus())
                {
                    startActivity(new Intent(OtpActivity.this, SubmitOTPActivity.class));
                    finish();
                }
                else
                    Toast.makeText(OtpActivity.this, ""+response.body().getMessage(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<ServerStatus> call, Throwable t)
            {
                Log.d("errMsg",""+t.getMessage());
            }
        });
    }


    private void init() {
        send_otp=(Button)findViewById(R.id.send_otp);
        phnOrMail = (EditText) findViewById(R.id.et_phn_or_email);
        main_container=(RelativeLayout)findViewById(R.id.main_container);
    }
    private boolean isValidMail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}