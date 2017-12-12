package com.app4mobi.admonition.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.app4mobi.admonition.R;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private Long eventID;
    private Dialog dialog, dialogEvent;
    private TextView mReminderEvent, mMeetingEvent, mCallingEvent, mAttendeeEvent;
    private MaterialEditText mEventTitle, mEventDescription, mEventLocation;
    private Button mEventStartDate, mEventEndDate, mCreateEvent;
    private ImageView mCloseEvent;
    private RadioGroup mEventStatus, mEventAlarm, mEventReminder;
    private Spinner mEventReminderMethods;
    private DatePickerDialog datePickerDialog;
    private int statusType = -1, eventAlarm = -1, eventReminder = -1, eventReminderMethod = -1, mYear, mMonth, mDay;
    private boolean needReminder = true, needMailService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initCouchBase();
    }

    private void initCouchBase() {
        // Create a manager
        Manager manager = null;
        try {
            manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create or open the database named app
        Database database = null;
        try {
            database = manager.getDatabase("app");
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        // The properties that will be saved on the document
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("title", "Couchbase Mobile");
        properties.put("sdk", "Android");
        // Create a new document
        Document document = database.createDocument();
        // Save the document to the database
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        // Log the document ID (generated by the database)
        // and properties
        Log.d("app", String.format("Document ID :: %s", document.getId()));
        Log.d("app", String.format("Learning %s with %s", document.getProperty("title"),
            document.getProperty("sdk")));

        // Create replicators to push & pull changes to & from Sync Gateway.
        /*URL url = null;
        try {
            url = new URL("http://10.0.2.2:4984/hello");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Replication push = database.createPushReplication(url);
        Replication pull = database.createPullReplication(url);
        push.setContinuous(true);
        pull.setContinuous(true);

        // Start replicators
        push.start();
        pull.start();*/
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //fab.setVisibility(View.INVISIBLE);
                eventActionsDialog();
                /*events,meetings,e-contacting,calling*/
                //createAnEvent();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setMessageNotification(String s, String s1) {
        Log.e("**MQTT**",s+" == "+s1);
    }

    private void createAnEvent() {
        //String title = "FirstEvent", desc = "First Event Desc", place = "Hyderabad";
        int status = 1;
        long startDate = new Date().getTime();
        try {
            String eventUriString = getResources().getString(R.string.uri_event);
            // id, We need to choose from our mobile for primary its 1
            ContentValues eventValues = new ContentValues();
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
            eventValues.put(CalendarContract.Events.TITLE, mEventTitle.getText().toString().trim());
            eventValues.put(CalendarContract.Events.DESCRIPTION, mEventDescription.getText().toString().trim());
            eventValues.put(CalendarContract.Events.EVENT_LOCATION, mEventLocation.getText().toString().trim());

            long endDate = startDate + 1000 * 10 * 10; // For next 10min
            eventValues.put(CalendarContract.Events.DTSTART, startDate);
            eventValues.put(CalendarContract.Events.DTEND, endDate);

            //If it is birthday alarm or such kind (which should remind me for whole day) 0 for false, 1 for true
            // values.put("allDay", 1);

            // This information is sufficient for most @status entries tentative (0),confirmed (1) or canceled (2):
            eventValues.put("eventStatus", status);
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC/GMT +5:30");

            // eventValues.put("allDay", 1);

            // visibility to default (0),confidential (1), private (2), or public (3):
            // eventValues.put("visibility", 0);

            // You can control whether an event consumes time opaque(0) or transparent(1).
            // eventValues.put("transparency", 0);

            // 0 for false, 1 for true
            eventValues.put("hasAlarm", 1);

            Uri eventUri = this.getApplicationContext()
                .getContentResolver()
                .insert(Uri.parse(eventUriString), eventValues);
            eventID = Long.parseLong(eventUri.getLastPathSegment());


            if (needReminder) {
                /***************** Event: Reminder(with alert) Adding reminder to event *******************/
                String reminderUriString = getResources().getString(R.string.uri_reminders);
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("event_id", eventID);
                // Default value set time in min which occur before event start
                reminderValues.put("minutes", 5);
                // Alert Methods: Default(0),Alert(1), Email(2),SMS(3)
                reminderValues.put("method", 1);
                Uri reminderUri = this.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString),
                    reminderValues);
            }

            /***************** Event: Meeting(without alert) Adding Attendees to the meeting *******************/

            if (needMailService) {
                String attendeuesesUriString = getResources().getString(R.string.uri_attendees);

                /********
                 * To add multiple attendees need to insert ContentValues
                 * multiple times
                 ***********/
                ContentValues attendeesValues = new ContentValues();
                attendeesValues.put("event_id", eventID);
                attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
                attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee email
                //Relationship_None(0), Relationship_Attendee(1), Organizer(2), Performer(3),Speaker(4)
                attendeesValues.put("attendeeRelationship", 0);
                // None(0), Optional(1),Required(2),Resource(3)
                attendeesValues.put("attendeeType", 0);
                // None(0), Accepted(1),Decline(2),Invited(3),Tentative(4)
                attendeesValues.put("attendeeStatus", 0);

                Uri eventsUri = Uri.parse(getResources().getString(R.string.uri_event));
                Uri url = this.getApplicationContext().getContentResolver().insert(eventsUri, attendeesValues);

                // Uri attendeuesesUri = curActivity.getApplicationContext()
                // .getContentResolver()
                // .insert(Uri.parse(attendeuesesUriString), attendeesValues);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //FIXME : Check above validations and Close the dialog
        dialogEvent.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void eventActionsDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fab_menu);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        dialog.setCanceledOnTouchOutside(false);

        mReminderEvent = dialog.findViewById(R.id.reminderEvent);
        mMeetingEvent = dialog.findViewById(R.id.meetingEvent);
        mCallingEvent = dialog.findViewById(R.id.callingEvent);
        mAttendeeEvent = dialog.findViewById(R.id.attendeeEvent);
        mCloseEvent = dialog.findViewById(R.id.closeEvent);
        /**
         * Handle click events of action menu from Event Actions dialog view
         */
        mReminderEvent.setOnClickListener(this);
        mMeetingEvent.setOnClickListener(this);
        mCallingEvent.setOnClickListener(this);
        mAttendeeEvent.setOnClickListener(this);
        mCloseEvent.setOnClickListener(this);


        dialog.show();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.reminderEvent:
                //TODO : Relevant action
                displayEventDialog(1);
                break;
            case R.id.meetingEvent:
                //TODO : Relevant action
                break;
            case R.id.callingEvent:
                //TODO : Relevant action
                break;
            case R.id.attendeeEvent:
                //TODO : Relevant action
                break;
            case R.id.closeEvent:
                //TODO : Relevant action
                dialog.dismiss();
                //fab.setVisibility(View.VISIBLE);
                break;
        }
        dialog.dismiss();
    }

    /**
     * @param eventType It describes type of an Event
     */
    private void displayEventDialog(final int eventType) {
        dialogEvent = new Dialog(HomeActivity.this);
        dialogEvent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEvent.setContentView(R.layout.dialog_event_view);
        dialogEvent.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialogEvent.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER;
        dialogEvent.setCanceledOnTouchOutside(false);

        mEventTitle = dialogEvent.findViewById(R.id.eventTitle);
        mEventDescription = dialogEvent.findViewById(R.id.eventDescription);
        mEventLocation = dialogEvent.findViewById(R.id.eventLocation);
        mEventStartDate = dialogEvent.findViewById(R.id.eventStartDate);
        mEventEndDate = dialogEvent.findViewById(R.id.eventEndDate);
        mCreateEvent = dialogEvent.findViewById(R.id.createEventButton);
        mEventStatus = dialogEvent.findViewById(R.id.eventStatus);
        mEventAlarm = dialogEvent.findViewById(R.id.eventAlarm);
        mEventReminder = dialogEvent.findViewById(R.id.eventReminder);
        mEventReminderMethods = dialogEvent.findViewById(R.id.eventReminderMethods);

        mEventStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                statusType = mEventStatus.indexOfChild(group.findViewById(checkedId));
                Log.e("Status", "==>" + statusType);
                switch (statusType) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });

        mEventAlarm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                eventAlarm = mEventAlarm.indexOfChild(group.findViewById(checkedId));
                Log.e("eventAlarm", "==>" + eventAlarm);
            }
        });

        mEventReminder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                eventReminder = mEventReminder.indexOfChild(group.findViewById(checkedId));
                Log.e("mEventReminder", "==>" + eventReminder);
                if (eventReminder == 0) {
                    mEventReminderMethods.setVisibility(View.VISIBLE);
                } else {
                    mEventReminderMethods.setVisibility(View.GONE);
                }
            }
        });

        mEventReminderMethods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventReminderMethod = position;
                Log.e("eventReminderMethod", "==>" + eventReminderMethod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(mEventStartDate);
            }
        });

        mEventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(mEventEndDate);
            }
        });

        mCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAnEvent();
            }
        });

        dialogEvent.show();
    }

    private void getDate(final Button targetView) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    targetView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());//To set min & max date
        datePickerDialog.show();
    }

}