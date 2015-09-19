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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;

public class LoginActivity extends FragmentActivity {

    Activity activity;
    Button showEvents;

    public static final String USER_ID_BUNDLE_KEY = "Login.userId";

    private final Activity mActivity = this;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize Firebase
        Firebase.setAndroidContext(this);

        // initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        activity = this;

        //user is logged in
        if (AccessToken.getCurrentAccessToken() != null) {
            showEvents.setVisibility(View.VISIBLE);
        }

        // find login button and register callback
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //button to show event list is now visible
                if(showEvents != null)
                    showEvents.setVisibility(View.VISIBLE);

                // TODO: request additional permissions if necessary

                //start event list activity
                Intent intent = new Intent(mActivity, MenuActivity.class);
                intent.putExtra(USER_ID_BUNDLE_KEY, loginResult.getAccessToken().getUserId());
                startActivity(intent);
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
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
