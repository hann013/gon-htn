package co.gon_htn.gon;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import co.gon_htn.gon.firebase_objects.Event;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuActivityFragment extends Fragment {

    Button addEventButton;
    Context context;
    LinearLayout mEventsLayout;

    Firebase mUserEventsRef = new Firebase("https://gon-htn.firebaseio.com/users/");

    public MenuActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        context = getActivity().getApplicationContext();

        View eventsView = inflater.inflate(R.layout.fragment_menu, container, false);

        mEventsLayout = (LinearLayout) eventsView.findViewById(R.id.list_events);

        //Initialize reference to user event list
        mUserEventsRef = mUserEventsRef.child(AccessToken.getCurrentAccessToken().getUserId()).child("events");
        mUserEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("Found: ", snapshot.getChildrenCount() + " events");
                for (DataSnapshot postSnapshot : snapshot.getChildren())
                {
                    Event event = postSnapshot.getValue(Event.class);
                    LinearLayout newBadge = createEventBadge(event);
                    mEventsLayout.addView(newBadge);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Read failed:", firebaseError.getMessage());
            }
        });


        addEventButton = (Button)eventsView.findViewById(R.id.btn_add_event);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context.getApplicationContext(), AddEventActivity.class);
                startActivity(myIntent);
            }
        });

        return eventsView;
    }


    private LinearLayout createEventBadge(Event event)
    {
        LinearLayout newBadge = new LinearLayout(context);
        TextView eventName = new TextView(context);
        TextView eventStartDate = new TextView(context);

        eventName.setText(event.getName());
        eventStartDate.setText(event.getStartDate());

        eventName.setPadding(10, 10, 10, 10);
        eventStartDate.setPadding(10,10,10,10);


        if(event.getSource() == AddEventActivity.EVENT_SOURCE_FACEBOOK)
        {
            newBadge.setBackgroundColor(context.getResources().getColor(R.color.com_facebook_blue));
            eventName.setTextColor(context.getResources().getColor(R.color.White));
            eventStartDate.setTextColor(context.getResources().getColor(R.color.White));
        }

        else
        {
            newBadge.setBackgroundColor(context.getResources().getColor(R.color.Cloud));
            eventName.setTextColor(context.getResources().getColor(R.color.DarkGrey));
            eventStartDate.setTextColor(context.getResources().getColor(R.color.DarkGrey));
        }

        newBadge.addView(eventName);
        newBadge.addView(eventStartDate);
        return newBadge;
    }
}
