package com.exnovation.wishster.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.exnovation.wishster.Models.ServerStatus;
import com.exnovation.wishster.Models.ValueSet;
import com.exnovation.wishster.Models.VerifyOtpModel;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Loader;
import com.exnovation.wishster.Utilities.Prefs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;

public class SubmitOTPActivity extends AppCompatActivity {
    Button submit;
    String phn = "";
    String email = "";
    Prefs prefs;
    EditText etOtp;
    String otp = "";
    FirebaseAuth mAuth;
    TextView tvTimer, tvResend;
    private String mVerificationId;
   // Loader loader;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    RequestBody bodyRequest;
    JSONObject OTPsubmit=new JSONObject();
    ApiManager manager = new ApiManager();

    static Loader loader;
    Call<ForgotPasswordModel> coll=null;
    ApiManager apiManager=new ApiManager();
    JSONObject emailJson=new JSONObject();
    RelativeLayout main_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_o_t_p);
        loader=new Loader();
        submit=(Button)findViewById(R.id.submit);
        etOtp = (EditText) findViewById(R.id.et_otp);
        tvTimer = findViewById(R.id.tvTimer);
        tvResend =  findViewById(R.id.tv_resend);
        prefs = new Prefs(SubmitOTPActivity.this);
        phn = prefs.getData("phn");
        main_container=findViewById(R.id.main_container);
        //mAuth = FirebaseAuth.getInstance();
        //mCallback = new PhoneAuthProvider();
        if(isValidMobile(phn) == true){
            StartFirebaseLogin();
            sendVerificationCode(phn);
            App.isPhone = true;

        }

        tvResend.setEnabled(false);
        //tvResend.setTextColor(rgb(81,81,59));


        setTimer();
        //if (android.util.Patterns.PHONE.matcher(phn).matches())

        action();


    }

    private void setTimer() {
        //Timer T=new Timer();

       new CountDownTimer(180000, 1000) {


            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tvTimer.setText("00:00");
                tvResend.setEnabled(true);
                tvResend.setTextColor(rgb(51,153,255));

            }
        }.start();

    }

    private void StartFirebaseLogin() {
        mAuth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    etOtp.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(SubmitOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //storing the verification id that is sent to the user
                mVerificationId = s;
            }
        };

    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback);
    }

    private void action() {
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                otp = etOtp.getText().toString().trim();
                if (isValidMail(phn) == true){
                    try {
                        verifyOtp(otp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    if (otp.isEmpty() || otp.length() < 6) {
                        etOtp.setError("Enter valid code");
                        etOtp.requestFocus();
                        return;
                    }
                    if (!(App.isEmail))
                    verifyVerificationCode(otp);
                }

                //startActivity(new Intent(SubmitOTPActivity.this, ReviewActivity.class));
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResend.setEnabled(false);
                tvResend.setTextColor(rgb(187,189,193));
                setTimer();
                if (App.isPhone){
                    onResend(v);
                }else{
                    try {
                        sighUpWithEmail();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        etOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });
       /* main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ValueSet(SubmitOTPActivity.this);
            }
        });*/

    }


    private void sighUpWithEmail() throws JSONException {

        emailJson.put("email_mobile", phn);
        emailJson.put("type", "email");

        loader.showDialog(this);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), emailJson.toString());
        coll=apiManager.service.getOtpEmail(bodyRequest);
        coll.enqueue(new Callback<ForgotPasswordModel>()
        {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response)
            {


                loader.hideDialog();
                Toast.makeText(SubmitOTPActivity.this, response.body().message, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t)
            {
                Log.d("errMsg",""+t.getMessage());
            }
        });
    }

    private void verifyOtp(String otp) throws JSONException {
        OTPsubmit.put("email",prefs.getData("phn"));
        OTPsubmit.put("otp",otp);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), OTPsubmit.toString());
        manager.service.getVerifyOtp(bodyRequest).enqueue(new Callback<VerifyOtpModel>() {
            @Override
            public void onResponse(Call<VerifyOtpModel> call, Response<VerifyOtpModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().status) {
                        Toast.makeText(SubmitOTPActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        if (!(App.isEmail)) {
                            startActivity(new Intent(SubmitOTPActivity.this, ResetPassword.class));
                            finish();
                        } else {
                            startActivity(new Intent(SubmitOTPActivity.this, InfoActivity.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(SubmitOTPActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }


                }

            }

            @Override
            public void onFailure(Call<VerifyOtpModel> call, Throwable t) {

                Toast.makeText(SubmitOTPActivity.this, "Network Error! Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SubmitOTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //startActivity(intent);
                           // startActivity(new Intent(SubmitOTPActivity.this,ResetPassword.class));
                            if (App.isForgot != 1) {
                                Toast.makeText(SubmitOTPActivity.this, "Welcome to Whishster", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SubmitOTPActivity.this, InfoActivity.class);
                                // prefs.saveData("uid", uID);
                                startActivity(i);
                                finish();
                            }else{
                                Intent i = new Intent(SubmitOTPActivity.this, ResetPassword.class);
                                // prefs.saveData("uid", uID);
                                startActivity(i);
                                finish();
                            }


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            task.getException();
                            Toast.makeText(SubmitOTPActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void onResend(View view) {
        sendVerificationCode(phn);
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


}