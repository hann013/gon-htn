package co.gon_htn.gon;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import co.gon_htn.gon.firebase_objects.Event;
import co.gon_htn.gon.firebase_objects.User;

public class MenuActivity extends AppCompatActivity {
    private Firebase mFirebaseRef = new Firebase("https://gon-htn.firebaseio.com/users/");
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // request additional permissions to view user events
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_events"));

        mUserId = getIntent().getExtras().getString(LoginActivity.USER_ID_BUNDLE_KEY);

        listenForDatabaseChanges();

        setContentView(R.layout.activity_menu);
    }

    public void listenForDatabaseChanges() {
        // attach child listener to database reference
        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> ids = (HashMap<String, String>) dataSnapshot.getValue();

                // user doesn't exist
                if (ids == null || !ids.containsKey(mUserId)) {
                    // retrieve name data
                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            "/" + mUserId,
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    try {
                                        String name = response.getJSONObject().getString("name");
                                        createAndSaveNewUser(name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ).executeAsync();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("menu_database_error", "The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public ArrayList<Event> getUserEvents(int userId) {
        return new ArrayList<Event>();
    }

    public void createAndSaveNewUser(String name) {
        Firebase newUserRef = mFirebaseRef.child(mUserId);
        User newUser = new User(name);
        newUserRef.setValue(newUser);
    }
}
