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

import com.exnovation.wishster.Models.ForgotPasswordModel;
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

public class ForgotPassword extends AppCompatActivity {
    static Loader loader;
    Button send_otp;
    EditText userInput;
    String uInput = "";
    Prefs prefs = new Prefs(this);
    ApiManager manager = new ApiManager();
    RequestBody bodyRequest;
    JSONObject forgotPassword=new JSONObject();
    RelativeLayout main_container;
    static String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        loader=new Loader();
        init();
        Action();
    }

    private void init() {
        send_otp=findViewById(R.id.send_otp);
        userInput=findViewById(R.id.et_user_input);
        main_container=(RelativeLayout)findViewById(R.id.main_container);
    }

    private void Action() {
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.showDialog(ForgotPassword.this);
                uInput = userInput.getText().toString();
                if (isValidMail(uInput) == true){
                    try {
                        type="email";
                        sendOtpEmail(uInput);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    prefs.saveData("phn", uInput);
                }else if (isValidMobile(uInput) == true){
                    type="mobile";
                    prefs.saveData("phn", uInput);
                    App.isForgot = 1;
                   // Toast.makeText(ForgotPassword.this, "", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this, SubmitOTPActivity.class));
                    finish();
                }

            }
        });
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                new ValueSet(ForgotPassword.this);
            }
        });*/
    }

    private void sendOtpEmail(String uInput) throws JSONException {

        forgotPassword.put("email_mobile",uInput);
        forgotPassword.put("type",type);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), forgotPassword.toString());
        manager.service.getOtpEmail(bodyRequest).enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                Log.d("printdataABCD",""+response.body().message);
                loader.hideDialog();
                if (response.isSuccessful()){
                    if (response.body().status.equals("true")){
                        Toast.makeText(ForgotPassword.this, response.body().message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this, SubmitOTPActivity.class));
                        finish();
                    }else{
                        Toast.makeText(ForgotPassword.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgotPassword.this, "No response from the server! please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {

                Toast.makeText(ForgotPassword.this, "Network Error! Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean isValidMail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
