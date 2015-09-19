package co.gon_htn.gon;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends FragmentActivity {

    CallbackManager callbackManager;
    Activity activity;

    Button showEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        activity = this;

        AccessToken at = AccessToken.getCurrentAccessToken();

        //user is logged in
        if(at != null && showEvents != null)
        {
            showEvents.setVisibility(View.VISIBLE);
        }

        // find log in button and register call back
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // request additional permissions if necessary
                if(showEvents != null)
                    showEvents.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error logging in", Toast.LENGTH_SHORT).show();

            }
        });

        showEvents = (Button)findViewById(R.id.btn_my_events);
        showEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken aT = AccessToken.getCurrentAccessToken();
                if(aT != null)
                {
                    Intent menuIntent = new Intent(activity, MenuActivity.class);
                    startActivity(menuIntent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null && showEvents != null)
        {
            showEvents.setVisibility(View.VISIBLE);
        }
    }
}
