package co.gon_htn.gon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import co.gon_htn.gon.firebase_objects.Event;

public class AddEventActivity extends AppCompatActivity
{
    public static final String EVENT_SOURCE_FACEBOOK = "Facebook";
    public static final String EVENT_SOURCE_USER = "User";
    public static final String EVENT_ID_BUNDLE_KEY = "EventDetails.eventId";

    private ArrayList<String> recommendedItems = new ArrayList<String>();

    TextView startDate;
    TextView endDate;
    EditText userItem;
    LinearLayout userItemList;
    Activity activity;

    Firebase mFbUsersRef = new Firebase("https://gon-htn.firebaseio.com/users/");
    Firebase mFbRecsRef = new Firebase("https://gon-htn.firebaseio.com/recommendations/");

    Button addUserItem;
    Button submitEvent;

    String mEventId;
    Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_add_event);

        activity = this;

        //get linear layout that contains all user inputted items
        userItemList = (LinearLayout)findViewById(R.id.user_items);


        if(getIntent().getExtras() != null)
        {
            mEventId = getIntent().getExtras().getString(AddEventActivity.EVENT_ID_BUNDLE_KEY);
            Firebase ref = mFbUsersRef.child(AccessToken.getCurrentAccessToken().getUserId()).child("events");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //Check if the snapshot's key equals the id of the event we
                        //wish to view
                        if(postSnapshot.getKey().equals(mEventId))
                        {
                            mEvent = postSnapshot.getValue(Event.class);
                            Log.d("Event", mEvent.toString());

                            if(mEvent.getUserItems() != null)
                            {
                                if(mEvent.getUserItems().size() != 0)
                                {
                                    userItem = (EditText) findViewById(R.id.user_item_1);
                                    userItem.setVisibility(View.GONE);

                                    for(int i = 0; i < mEvent.getUserItems().size(); i++)
                                    {
                                        EditText newItem = new EditText(activity);
                                        newItem.setText(mEvent.getUserItems().get(i));
                                        View.OnKeyListener ekl = enterKeyListener();
                                        newItem.setOnKeyListener(ekl);
                                        userItemList.addView(newItem);
                                    }
                                }
                            }

                            if (mEvent.getRecommendedItems() != null) {
                                if (mEvent.getRecommendedItems().size() > 0) {
                                    findViewById(R.id.rec_items_label).setVisibility(View.VISIBLE);
                                    LinearLayout recItems = (LinearLayout) findViewById(R.id.recommended_items);
                                    recItems.setVisibility(View.VISIBLE);
                                    for (String itemName : mEvent.getRecommendedItems()) {
                                        // append recommended items to view
                                        TextView newItem = new TextView(activity);
                                        recItems.addView(newItem);
                                        newItem.setTextSize(20);
                                        newItem.setPadding(15,0,0,5);
                                        newItem.setText(itemName);
                                    }
                                }
                            }

                            if(mEvent.getName() != null) {
                                if (!mEvent.getName().isEmpty()) {
                                    EditText name = (EditText) findViewById(R.id.event_name);
                                    name.setText(mEvent.getName());
                                }
                            }
                            if(mEvent.getStartDate() != null) {
                                if (!mEvent.getStartDate().isEmpty()) {
                                    TextView start = (TextView) findViewById(R.id.start_date);
                                    start.setText(mEvent.getStartDate());
                                }
                            }
                            if(mEvent.getEndDate() != null) {
                                if (!mEvent.getEndDate().isEmpty()) {
                                    TextView end = (TextView) findViewById(R.id.end_date);
                                    end.setText(mEvent.getStartDate());
                                }
                            }
                            if(mEvent.getLocation() != null) {
                                if (!mEvent.getLocation().isEmpty()) {
                                    EditText loc = (EditText) findViewById(R.id.event_location);
                                    loc.setText(mEvent.getLocation());
                                }
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("Read failed:", firebaseError.getMessage());
                }
            });
        }

        else
        {
            //Very first user item edit text when adding an event
            userItem = (EditText) findViewById(R.id.user_item_1);
            View.OnKeyListener myEkl = enterKeyListener();
            userItem.setOnKeyListener(myEkl);

            //start date input field
            startDate = (TextView) findViewById(R.id.start_date);
            View.OnClickListener startDatePick = datePickerCalendar(startDate);
            startDate.setOnClickListener(startDatePick);

            //end date input field
            endDate = (TextView) findViewById(R.id.end_date);
            View.OnClickListener endDatePick = datePickerCalendar(endDate);
            endDate.setOnClickListener(endDatePick);
        }


        //invisible item that adds another field to user items
        addUserItem = (Button)findViewById(R.id.btn_add_user_item);
        addUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText currEditTxt = (EditText)getWindow().getCurrentFocus();
                String currContent = currEditTxt.getText().toString();
                if(currContent != null && currContent != "" && currContent != " " && !currContent.isEmpty())
                {
                    EditText newItem = new EditText(activity);
                    View.OnKeyListener newEkl = enterKeyListener();
                    newItem.setOnKeyListener(newEkl);
                    userItemList.addView(newItem);
                    newItem.requestFocus();
                }
            }
        });

        // event category spinner
        final Spinner spinner = (Spinner) findViewById(R.id.event_category);

        mFbRecsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> categories = ((HashMap) ((HashMap) dataSnapshot.getValue()).get("activity")).keySet();
                List<String> selectItems = new ArrayList<String>(categories);
                selectItems.add(0, "Event Category");

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_spinner_item, selectItems);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(categoryAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // get recommended items for category
                        final String category = parent.getItemAtPosition(position).toString();

                        // if existing recommended items, delete
                        if (recommendedItems.size() > 0 || category == "Event Category") {
                            ((LinearLayout) findViewById(R.id.recommended_items)).removeAllViews();
                        } else {
                            findViewById(R.id.rec_items_label).setVisibility(View.VISIBLE);
                        }

                        if (category != "Event Category") {
                            mFbRecsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> categories = (ArrayList<String>)
                                            ((HashMap) ((HashMap) dataSnapshot.getValue()).get("activity")).get(category);
                                    recommendedItems = categories;
                                    for (String itemName : categories) {
                                        // append recommended items to view
                                        LinearLayout recItems = (LinearLayout) findViewById(R.id.recommended_items);
                                        TextView newItem = new TextView(activity);
                                        recItems.addView(newItem);
                                        newItem.setTextSize(20);
                                        newItem.setPadding(15,0,0,5);
                                        newItem.setText(itemName);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("menu_database_error", "The read failed: " + firebaseError.getMessage());
            }
        });

        //start date input field
        startDate = (TextView) findViewById(R.id.start_date);
        View.OnClickListener startDatePick = datePickerCalendar(startDate);
        startDate.setOnClickListener(startDatePick);


        //Submit button on click event handler
        submitEvent = (Button)findViewById(R.id.submit_event);
        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                submitEvent.setBackgroundColor(getResources().getColor(R.color.Green));

                //Initialize new Event object

                Event newEvent;
                String eventName = ((EditText)findViewById(R.id.event_name)).getText().toString();
                String category = ((Spinner)findViewById(R.id.event_category)).getSelectedItem().toString();
                String location = ((EditText)findViewById(R.id.event_location)).getText().toString();
                String startDate = ((TextView)findViewById(R.id.start_date)).getText().toString();
                String endDate = ((TextView)findViewById(R.id.end_date)).getText().toString();

                // UserID - id to be used for saving the data
                String id = null;


                //User-inputted items
                ViewGroup items_u = ((ViewGroup)(LinearLayout)
                        findViewById(R.id.user_items));
                ArrayList<String> uItems_array = new ArrayList<String>();

                for(int i = 0; i < items_u.getChildCount(); i++)
                {
                    View view = items_u.getChildAt(i);
                    if(view instanceof EditText)
                        uItems_array.add(((EditText) view).getText().toString());
                }

                //Recommended items
                ViewGroup items_r = ((ViewGroup)(LinearLayout)
                        findViewById(R.id.recommended_items));

                if(items_r.getChildCount() != 0)
                {
                    ArrayList<String> rItems_array = new ArrayList<String>();

                    for (int j = 0; j < items_r.getChildCount(); j++)
                    {
                        View myView = items_r.getChildAt(j);
                        if(myView instanceof TextView)
                            rItems_array.add(((TextView) myView).getText().toString());
                    }

                    //when editing existing file
                    if(mEvent != null)
                    {
                        newEvent = new Event(eventName, mEvent.getSource(), category, location, startDate, endDate,
                                uItems_array, rItems_array);
                        id = mEventId;
                    }
                    else
                    {
                        newEvent = new Event(eventName, EVENT_SOURCE_USER, category, location, startDate, endDate,
                                uItems_array, rItems_array);
                    }
                }
                else
                {
                    //when editing existing file
                    if(mEvent != null)
                    {
                        newEvent = new Event(eventName, mEvent.getSource(), category, location, startDate, endDate,
                                uItems_array);
                        id = mEventId;
                    }
                    else
                    {
                        newEvent = new Event(eventName, EVENT_SOURCE_USER, category, location, startDate, endDate,
                                uItems_array);
                    }
                }

                //Get user facebook id and save event to the database
                String thisUser = AccessToken.getCurrentAccessToken().getUserId();
                Firebase eventRef;
                if(id == null)
                    eventRef = mFbUsersRef.child(thisUser).child("events").child(UUID.randomUUID().toString());
                else
                    eventRef = mFbUsersRef.child(thisUser).child("events").child(id);
                eventRef.setValue(newEvent);

                Toast.makeText(activity, "Event saved!", Toast.LENGTH_SHORT).show();

                //transition to the menu intent to view the events
                Intent menuIntent = new Intent(activity, MenuActivity.class);
                menuIntent.putExtra(LoginActivity.USER_ID_BUNDLE_KEY, AccessToken.getCurrentAccessToken().getUserId());
                startActivity(menuIntent);
            }
        });
    }

    //date picker calendar popup for date textviews on click events.
    private View.OnClickListener datePickerCalendar(final TextView myDate)
    {
        View.OnClickListener dpc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day)
                            {
                                String date = year+"-"+month+"-"+day;
                                myDate.setText(date);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        };
        return dpc;
    }

    //enter key listener for item editviews.
    private View.OnKeyListener enterKeyListener()
    {
        View.OnKeyListener ekl = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    addUserItem.performClick();
                    return true;
                }
                return false;
            }
        };
        return ekl;
    }

}
