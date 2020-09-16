package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.internal.StatusCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivityFaceBook extends AppCompatActivity {
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_face_book);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions(Arrays.asList("user_status"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Tface", "E \n"+loginResult.getAccessToken());

                /*GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                App.social_types=true;
                                Log.v("MainData", response.toString());
                                Log.d("AAAA", object.toString());
                                 Toast.makeText(getApplicationContext(), object.toString()+"\n"+response.toString(), Toast.LENGTH_SHORT).show();
                                // setProfileToView(object);
                                //setProfileToView(object);
                                //   LoginManager.getInstance().logOut();

                            }
                        });
                //Log.d("ABC",callbackManager.toString());
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                */
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code

                                Log.d("Tface", "A \n"+response.toString());
                                Log.d("Tface", "B \n"+object.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",  "id, name, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                Log.d("Tface", "D \n"+ AccessToken.getCurrentAccessToken()+"\nABC");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "105742841269149",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                /* handle the result */
                                Log.d("Tface", "C \n"+response.toString());
                            }
                        }
                ).executeAsync();
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception)
            {
                // App code
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

}