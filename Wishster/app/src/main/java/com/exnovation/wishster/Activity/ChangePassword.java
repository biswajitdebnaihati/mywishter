package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.exnovation.wishster.Models.ChangePasswordModel;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    EditText etCurPwd, etNewPwd, etConfirmPwd;
    Button btnSubmit;
    ImageView ivBack;
    String current, newPwd, confirmPwd;
    ApiManager manager = new ApiManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etCurPwd = findViewById(R.id.et_current_pwd);
        etNewPwd = findViewById(R.id.et_new_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_pwd);
        btnSubmit = findViewById(R.id.btn_submit);
        ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = etCurPwd.getText().toString();
                newPwd = etNewPwd.getText().toString();
                confirmPwd = etConfirmPwd.getText().toString();
                if (newPwd.equals(confirmPwd)){
                    resetPassword(current, newPwd);
                }
            }
        });

    }

    private void resetPassword(String current, String newPwd) {
        manager.service.setNewPassword(current, newPwd).enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                if (response.isSuccessful()){
                    if (response.body().status.equals("true")){
                        Toast.makeText(ChangePassword.this, response.body().message, Toast.LENGTH_SHORT).show();
                        finish();
                        //startActivity(new Intent(ResetPassword.this, SubmitOTPActivity.class));
                    }else{
                        Toast.makeText(ChangePassword.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangePassword.this, "No response from the server! please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                Toast.makeText(ChangePassword.this, "Network Error! Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
