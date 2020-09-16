package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.exnovation.wishster.Models.AllFriendsModel;
import com.exnovation.wishster.Models.Questions;
import com.exnovation.wishster.Models.ReviewQuestion;
import com.exnovation.wishster.Models.SaveQuestion;
import com.exnovation.wishster.Network.ApiInterface;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.Loader;
import com.squareup.okhttp.internal.spdy.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    public static List<Questions> AllQuestion;
    Button btn_q_submit_n;
    RequestBody bodyRequest;
    Call<ReviewQuestion> coll=null;
    Call<SaveQuestion> coll2=null;
    TextView total_count,question,skip;
    JSONObject Question=new JSONObject();
    static int count=-1;
    RadioButton p1,p2,p3,p4;
    static Loader loader;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ApiManager apiManager=new ApiManager();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        loader=new Loader();

        try {
            init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        action();

    }

    private void init() throws JSONException
    {
        radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        btn_q_submit_n=(Button)findViewById(R.id.btn_q_submit_n);
        skip=(TextView) findViewById(R.id.skip);
        total_count=(TextView) findViewById(R.id.total_count);
        question=(TextView) findViewById(R.id.question);
        p1=(RadioButton) findViewById(R.id.p1);
        p2=(RadioButton) findViewById(R.id.p2);
        p3=(RadioButton) findViewById(R.id.p3);
        p4=(RadioButton) findViewById(R.id.p4);
        Question.put("user_id",""+App.UserId);
    }
    private void action() {
        btn_q_submit_n.setClickable(false);
        btn_q_submit_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuestion(1);
                if (!(btn_q_submit_n.getText().toString().trim().equals("Submit")))
                    btn_q_submit_n.setClickable(false);
                else if (count==AllQuestion.size())
                    saveQuestionToServer();
                skip.setVisibility(View.GONE);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ReviewActivity.this, HomeActivity.class));
                finish();
            }
        });


        loader.showDialog(this);
        serverCall();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                if (AllQuestion!=null && radioButton!=null)
                {
                    try {
                        if (question!=null) {
                            Question.put("user_id",""+App.UserId);
                            Question.put("security_qtn" + (count + 1), question.getText().toString());
                            Question.put("security_ans" + (count + 1), radioButton.getText().toString());
                        }
                        if (btn_q_submit_n!=null) {
                            btn_q_submit_n.setClickable(true);
                            btn_q_submit_n.setEnabled(true);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private void serverCall()
    {
        coll=apiManager.service.ReviewQuestion();
        coll.enqueue(new Callback<ReviewQuestion>()
        {
            @Override
            public void onResponse(Call<ReviewQuestion> call, Response<ReviewQuestion> response) {

                loader.hideDialog();
                if (response.body().isStatus())
                {
                    if (AllQuestion!=null)
                        AllQuestion.clear();
                    AllQuestion =response.body().getQuestions;
                    if (!(AllQuestion.size()>0 ))
                        Toast.makeText(ReviewActivity.this, "Security Question Not Added", Toast.LENGTH_SHORT).show();
                    else
                        setQuestion(1);
                }
                else
                    Toast.makeText(ReviewActivity.this, ""+response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ReviewQuestion> call, Throwable t)
            {
                Log.d("errMsg",""+t.getMessage());
            }
        });

    }
    private void setQuestion(int np)
    {

        if (AllQuestion!=null) {
            if (np==1)
                count++;
            else
                count --;

            if (count>AllQuestion.size()-2)
                btn_q_submit_n.setText("Submit");
            else
                btn_q_submit_n.setText("Next");
            if (count<0 ){
                count=0;
                return;
            }
            if (count>=AllQuestion.size()){
                count=AllQuestion.size();
                return;
            }
            radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
            radioGroup.clearCheck();

            question.postDelayed(new Runnable() {
                @Override
                public void run() {
                    question.setText(AllQuestion.get(count).getQuestion());
                }
            },100);
            p1.postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    if (AllQuestion.get(count).getOption1()!=null)
                        p1.setText(AllQuestion.get(count).getOption1());
                    else
                        p1.setText("");

                }
            },100);
            p2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (AllQuestion.get(count).getOption2()!=null)
                        p2.setText(AllQuestion.get(count).getOption2());
                    else
                        p2.setText("");
                }
            },100);
            p3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (AllQuestion.get(count).getOption3()!=null)
                        p3.setText(AllQuestion.get(count).getOption3());
                    else
                        p3.setText("");
                }
            },100);
            p4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (AllQuestion.get(count).getOption4()!=null)
                        p4.setText(AllQuestion.get(count).getOption4());
                    else
                        p4.setText("");
                }
            },100);
            setTotalCount((count+1), AllQuestion.size());
        }
    }
    void setTotalCount(final int count,final  int total_count_question){
        total_count.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                total_count.setText(count+"/"+total_count_question);
            }
        },100);
    }
    private void saveQuestionToServer()
    {
        Log.d("chkdata",""+Question.toString());
        loader.showDialog(this);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), Question.toString());
        coll2=apiManager.service.SaveQuestion(bodyRequest);
        coll2.enqueue(new Callback<SaveQuestion>()
        {
            @Override
            public void onResponse(Call<SaveQuestion> call, Response<SaveQuestion> response)
            {
                App.token = response.headers().get("token");
                loader.hideDialog();
                if (response.body().isStatus())
                {
                    startActivity(new Intent(ReviewActivity.this, HomeActivity.class));
                    finish();
                }
                else
                    Toast.makeText(ReviewActivity.this, ""+response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SaveQuestion> call, Throwable t)
            {
                Log.d("errMsg",""+t.getMessage());
            }


        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        count=-1;
    }
}