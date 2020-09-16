package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exnovation.wishster.Models.LoginModel;
import com.exnovation.wishster.Models.ReviewQuestion;
import com.exnovation.wishster.Models.SignUpModel;
import com.exnovation.wishster.Models.ValueSet;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Loader;
import com.exnovation.wishster.Utilities.Prefs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity {
    DatePickerDialog datepicker;
    static Loader loader;
    RequestBody bodyRequest;
    CheckBox cbAge;
    Call<SignUpModel> coll=null;
    ApiManager apiManager=new ApiManager();
    EditText etName, etDob, etPwd;
    Button btnSubmit;
    String type = "";
    RelativeLayout main_container;
    Prefs prefs;
    JSONObject userSignData=new JSONObject();
    int dd, mm, yy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        prefs=new Prefs(this);

        init();
        action();

    }
    private void init() {
        etName = findViewById(R.id.et_name);
        etDob = findViewById(R.id.et_dob);
        main_container=findViewById(R.id.main_container);
        etPwd = findViewById(R.id.et_pwd);
        btnSubmit = findViewById(R.id.btn_submit);
        cbAge = findViewById(R.id.checkBox_age);
    }
    private void action() {

        cbAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!cbAge.isChecked()){
                    cbAge.setChecked(true);
                }else{
                    cbAge.setChecked(false);
                }*/
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                 int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                 int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(InfoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = String.format("%02d" , (monthOfYear + 1));
                                String day = String.format("%02d" , dayOfMonth);
                                etDob.setText( month +"-"+day+ "-" + year);
                                dd = dayOfMonth;
                                yy = year;
                                mm = monthOfYear+1;
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAge.isChecked()){
                    getAge(yy, mm, dd);

                    if (App.isAdult){
                        if (Validate())
                        {

                            try {
                                if(App.isPhone){
                                    type = "mobile";
                                }else{
                                    type = "email";
                                }
                                userSignData.put("type", type);
                                userSignData.put("email_mobile",prefs.getData("phn"));
                                userSignData.put("name",etName.getText().toString());
                                userSignData.put("birthday",etDob.getText().toString());
                                userSignData.put("password",etPwd.getText().toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SignupServerCall();
                        }
                    }else {
                        Toast.makeText(InfoActivity.this, "Sorry! You are under aged to create an account!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(InfoActivity.this, "Please tick the check box if you are atleast 18 yeas old.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        etPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    btnSubmit.performClick();
                    return true;
                }
                return false;
            }
        });
        /*main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ValueSet(InfoActivity.this);
            }
        });*/
    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        if (ageInt>=18){
            App.isAdult = true;
        }else{
            App.isAdult = false;
        }
        String ageS = ageInt.toString();

        return ageS;
    }

    private boolean Validate()
    {//.matches(emailPattern)
        if (etName.getText().toString().length()==0)
        {
            Toast.makeText(this, "Name field cannot be blank", Toast.LENGTH_SHORT).show();
            // etName.setError("Name field cannot be blank");
            return false;
        }
        if (!(etName.getText().toString().matches(App.fullname)))
        {
            //   etName.setError("Invalid user name");
            Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDob.getText().toString().length()==0)
        {
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
            // etName.setError("Name field cannot be blank");
            return false;
        }
        if (!(etDob.getText().toString().matches(App.date_pattern)))
        {
          //  etDob.setError("Invalid date format");
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPwd.getText().toString().length()==0)
        {
            //  etPwd.setError("Password field cannot be blank");
            Toast.makeText(this, "Password field cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(etPwd.getText().toString().length()>5 && etPwd.getText().toString().length()<21))
        {
            //  etPwd.setError("password length must be of 6-20");
            Toast.makeText(this, "Password length must be of 6-20", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    private void SignupServerCall()
    {
        loader=new Loader();
        loader.showDialog(this);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), userSignData.toString());
        coll=apiManager.service.signup(bodyRequest);
        coll.enqueue(new Callback<SignUpModel>()
        {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response)
            {
                App.token = response.headers().get("token");

                loader.hideDialog();
                if (response.body().isStatus())
                {
                    App.UserId=response.body().getUserData().getId()+"";
                    App.bool=true;
                    prefs.saveData(getString(R.string.TOKEN), App.token);
                    prefs.saveData("User_name", response.body().getUserData().getName());
                    prefs.saveData("prf_pic", response.body().getUserData().getProfile_img());
                    startActivity(new Intent(InfoActivity.this, ReviewActivity.class));
                    finish();
                }
                else
                    Toast.makeText(InfoActivity.this, ""+response.body().getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t)
            {
                loader.hideDialog();
                Toast.makeText(InfoActivity.this, "Network Error! Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                Log.d("errMsg",""+t.getMessage());
            }
        });

    }
}
