package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exnovation.wishster.Models.ResetPasswordModel;
import com.exnovation.wishster.Models.ValueSet;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {
    EditText etConfirmPwd, etNewPwd;
    Button btnSubmit;
    String pwd, confirmPwd, email;
    Prefs prefs = new Prefs(ResetPassword.this);
    ApiManager manager = new ApiManager();
    RequestBody bodyRequest;
    JSONObject resetpassword=new JSONObject();
    RelativeLayout main_container;
    static  String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPwd = findViewById(R.id.et_new_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_pwd);
        btnSubmit = findViewById(R.id.btn_submit);
        main_container=(RelativeLayout)findViewById(R.id.main_container);
        email = prefs.getData("phn");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = etNewPwd.getText().toString();
                confirmPwd = etConfirmPwd.getText().toString();
                if (pwd.equals(confirmPwd)){

                    if (isValidMail(pwd))
                        type="email";
                    else
                        type="mobile";

                    try {
                        setNewPassword(email, pwd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(ResetPassword.this, "Password didn't match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        etConfirmPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    btnSubmit.performClick();
                    return true;
                }
                return false;
            }
        });
       /* main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ValueSet(ResetPassword.this);
            }
        });*/
    }

    private void setNewPassword(String email, String pwd) throws JSONException {


        resetpassword.put("type",type);
        resetpassword.put("email_mobile",email);
        resetpassword.put("new_password",pwd);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), resetpassword.toString());
        manager.service.resetPassword(bodyRequest).enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(Call<ResetPasswordModel> call, Response<ResetPasswordModel> response) {
                if (response.isSuccessful()){

                        Toast.makeText(ResetPassword.this, response.body().message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPassword.this, Login.class));
                        finish();
                    }else{
                    Toast.makeText(ResetPassword.this, "No Response from the sever! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordModel> call, Throwable t) {

                Toast.makeText(ResetPassword.this, "Network error! Please check internet connection and try again!", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private boolean isValidMail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
