package co.gon_htn.gon;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.fasterxml.jackson.annotation.JsonValue;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

                    LinearLayout newBadge = new LinearLayout(context);

                    //Attach event id to the layout
                    newBadge.setOrientation(LinearLayout.VERTICAL);
                    newBadge.setPadding(20, 20, 20, 20);

                    //Register on click listener
                    View.OnClickListener clickToEventDetails = linkToDetailsPage(postSnapshot.getKey());
                    newBadge.setOnClickListener(clickToEventDetails);

                    TextView eventName = new TextView(context);
                    TextView eventStartDate = new TextView(context);

                    newBadge.addView(eventName);
                    newBadge.addView(eventStartDate);

                    eventName.setText(event.getName());
                    eventStartDate.setText(event.getStartDate());

                    eventName.setTextColor(getResources().getColor(R.color.DarkGrey));
                    eventName.setTextSize(20);
                    eventStartDate.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                    eventStartDate.setTextSize(15);

                    if (event.getSource().equals(AddEventActivity.EVENT_SOURCE_USER)) {
                        newBadge.setBackgroundColor(context.getResources().getColor(R.color.LightOrange));
                    } else {
                        newBadge.setBackgroundColor(context.getResources().getColor(R.color.LightBlue));
                    }

                    Space miniSpace = new Space(context);
                    miniSpace.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            50));

                    mEventsLayout.addView(newBadge);
                    mEventsLayout.addView(miniSpace);
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



    private View.OnClickListener linkToDetailsPage(final String eventId)
    {
        View.OnClickListener eventLink  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventDetailsIntent = new Intent(context, AddEventActivity.class);
                eventDetailsIntent.putExtra(AddEventActivity.EVENT_ID_BUNDLE_KEY, eventId);
                startActivity(eventDetailsIntent);
            }
        };
        return eventLink;
    }
}
