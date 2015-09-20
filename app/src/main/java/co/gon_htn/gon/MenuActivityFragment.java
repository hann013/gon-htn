package co.gon_htn.gon;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.firebase.client.Firebase;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuActivityFragment extends Fragment {

    Button addEventButton;
    Context context;
    Firebase mFirebaseRef = new Firebase("https://gon-htn.firebaseio.com/users/");

    public MenuActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        context = getActivity().getApplicationContext();
        mFirebaseRef.child(AccessToken.getCurrentAccessToken().getUserId()).child("events");

        View eventsView = inflater.inflate(R.layout.fragment_menu, container, false);
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
}
