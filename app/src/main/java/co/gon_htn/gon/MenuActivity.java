package co.gon_htn.gon;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;

import java.util.Arrays;

public class MenuActivity extends AppCompatActivity {
    private Firebase mFirebaseRef = new Firebase("https://gon-htn.firebaseio.com/users/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // request additional permissions to view user events
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_events"));

        String userId = getIntent().getExtras().getString(LoginActivity.USER_ID_BUNDLE_KEY);

        Firebase testUser = mFirebaseRef.child(userId);
        testUser.child("name").setValue("testttt");

        setContentView(R.layout.activity_menu);
    }

}
