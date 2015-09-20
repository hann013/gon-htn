package co.gon_htn.gon;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import co.gon_htn.gon.firebase_objects.Event;
import co.gon_htn.gon.firebase_objects.User;

public class MenuActivity extends AppCompatActivity {
    private Firebase mFirebaseRef = new Firebase("https://gon-htn.firebaseio.com/users/");
    private String mUserId;
    private boolean facebookEventsUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
            public void onDataChange(final DataSnapshot dataSnapshot) {
                HashMap<String, String> ids = (HashMap<String, String>) dataSnapshot.getValue();
                facebookDataRequest(ids);
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

    public void facebookDataRequest(final HashMap<String, String> ids) {
        // retrieve name and events data
        GraphRequest facebookDataRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + mUserId,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            String name = response.getJSONObject().getString("name");

                            // if user doesn't exist, save new user
                            if (ids == null || !ids.containsKey(mUserId)) {
                                createAndSaveNewUser(name);
                            }

                            if (LoginActivity.importFacebookEvents && !facebookEventsUpdated) {
                                storeFacebookEvents(response.getJSONObject());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,events");
        facebookDataRequest.setParameters(parameters);
        facebookDataRequest.executeAsync();
    }

    public void storeFacebookEvents(JSONObject graphData) throws JSONException, ParseException {
        // look for events that are upcoming
        JSONArray eventsData = null;
        eventsData = graphData.getJSONObject("events").getJSONArray("data");

        if (eventsData != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            // only add upcoming events to imported list
            for (int i = 0; i < eventsData.length(); i++) {
                    JSONObject event = eventsData.getJSONObject(i);
                Date startDate = formatter.parse(event.getString("start_time").substring(0,10));
                if (startDate.after(new Date())) {
                    JSONObject location = event.getJSONObject("place").getJSONObject("location");

                    String place = event.getJSONObject("place").getString("name") + ", ";

                    if (location.getString("street") != null) {
                        place += location.getString("street") + ", ";
                    }

                    place += location.getString("city") + ", " + location.getString("state");

                    Event eventToSave = new Event(event.getString("name"),
                            AddEventActivity.EVENT_SOURCE_FACEBOOK, place, formatter.format(startDate), null);

                    mFirebaseRef.child(mUserId).child("events").child(event.getString("id")).setValue(eventToSave);
                    facebookEventsUpdated = true;
                }
            }
        }
    }
}
