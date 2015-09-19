package co.gon_htn.gon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Service;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity
{

    TextView startDate;
    TextView endDate;
    EditText userItem;
    LinearLayout userItemList;
    Activity activity;
    Button addUserItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_add_event);

        activity = this;

        //get root layout
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_layout);
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        SoftKeyboard softKeyboard;
        softKeyboard = new SoftKeyboard(mainLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
        {

            @Override
            public void onSoftKeyboardHide()
            {
                // Code here
            }

            @Override
            public void onSoftKeyboardShow()
            {
                // Code here
            }
        });


        //get linear layout that contains all user inputted items
        userItemList = (LinearLayout)findViewById(R.id.user_items);

        //invisible item that adds another field to user items
        addUserItem = (Button)findViewById(R.id.btn_add_user_item);
        addUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newItem = new EditText(activity);
                View.OnKeyListener newEkl = enterKeyListener();
                newItem.setOnKeyListener(newEkl);
                userItemList.addView(newItem);
                newItem.requestFocus();
            }
        });

        //start date input field
        startDate = (TextView) findViewById(R.id.event_date);
        View.OnClickListener startDatePick = datePickerCalendar(startDate);
        startDate.setOnClickListener(startDatePick);

        //end date input field
        endDate = (TextView) findViewById(R.id.event_date);
        View.OnClickListener endDatePick = datePickerCalendar(endDate);
        startDate.setOnClickListener(endDatePick);


        userItem = (EditText) findViewById(R.id.user_item_1);
        View.OnKeyListener myEkl = enterKeyListener();
        userItem.setOnKeyListener(myEkl);
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
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day)
                            {
                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, month-1, day);
                                myDate.setText(simpledateformat.format(date) + ", " + String.valueOf(month) +
                                        "-" + String.valueOf(day) + "-" + String.valueOf(year));

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
