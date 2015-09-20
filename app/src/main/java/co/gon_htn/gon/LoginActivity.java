package co.gon_htn.gon;

import android.app.Activity;
import android.app.FragmentManager;
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

import co.gon_htn.gon.firebase_objects.EventsPermissionsDialogFragment;

public class LoginActivity extends FragmentActivity {

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

        // on-click handler for show event list button
        showEvents = (Button)findViewById(R.id.btn_my_events);
        showEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AccessToken.getCurrentAccessToken() != null)
                {
                    Intent menuIntent = new Intent(mActivity, MenuActivity.class);

                    menuIntent.putExtra(LoginActivity.USER_ID_BUNDLE_KEY, AccessToken.getCurrentAccessToken().getUserId());
                    startActivity(menuIntent);
                }
            }
        });

        //user is logged in
        if (AccessToken.getCurrentAccessToken() != null)
        {
            showEvents.setVisibility(View.VISIBLE);
        }

        // find login button and register callback
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                //button to show event list is now visible
                if(showEvents != null) {
                    showEvents.setVisibility(View.VISIBLE);
                }

                EventsPermissionsDialogFragment eventsPermissions = new EventsPermissionsDialogFragment();
                eventsPermissions.show(getFragmentManager(), "events permissions");
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
