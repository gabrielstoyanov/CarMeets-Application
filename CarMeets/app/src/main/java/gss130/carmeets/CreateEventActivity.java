package gss130.carmeets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class CreateEventActivity extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String create = "";
    String create1 = "";
    int create1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //Initializing the Buttons that are present in the activity (Create and Clear)
        final Button btn_create = (Button) findViewById(R.id.btn_create);
        final Button btn_clear = (Button) findViewById(R.id.btn_clear);

        //Initializing the EditText fields that are present in the activity
        final EditText txt_eventName = (EditText) findViewById(R.id.txt_eventName);
        final EditText txt_eventLocation = (EditText) findViewById(R.id.txt_eventLocation);
        final EditText txt_specificInformation = (EditText) findViewById(R.id.txt_specificInformation);
        final EditText txt_eventStartingTime = (EditText) findViewById(R.id.txt_eventStartingTime);
        final EditText txt_eventDuration = (EditText) findViewById(R.id.txt_eventDuration);

        //Initializing the TextViews (labels) that are present in the activity
        //and setting their visible to INVISIBLE, so they will not appear when the
        //activity is started, but only when there is an error
        final TextView lbl_eventNameError = (TextView) findViewById(R.id.lbl_eventNameError);
        lbl_eventNameError.setVisibility(View.INVISIBLE);
        final TextView lbl_eventLocationError = (TextView) findViewById(R.id.lbl_eventLocationError);
        lbl_eventLocationError.setVisibility(View.INVISIBLE);
        final TextView lbl_specificInformationError = (TextView) findViewById(R.id.lbl_specificInformationError);
        lbl_specificInformationError.setVisibility(View.INVISIBLE);
        final TextView lbl_eventStartingTimeError = (TextView) findViewById(R.id.lbl_startingTimeError);
        lbl_eventStartingTimeError.setVisibility(View.INVISIBLE);
        final TextView lbl_eventDurationError = (TextView) findViewById(R.id.lbl_eventDurationError);
        lbl_eventDurationError.setVisibility(View.INVISIBLE);

        //setting up an instance of class Register_Input_Checks
        //that will be used to access the input check methods of that class
        final Register_Input_Checks check = new Register_Input_Checks();

        //setting up a sharedpreferences object for the user's username
        final SharedPreferences sp_username = this.getSharedPreferences("username", 0);

        //initializing a string variable with the user's username
        final String user = sp_username.getString("username", "Username not found!");

        //setting up a boolean variable that keeps track whether there
        //have been errors with the data entered before submitting the
        //data to the database
        final boolean[] isCorrect = new boolean[1];


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the Create button first invokes the methods from Register_Input_Checks class
                //such as emptyField_Check, starting_time, duration_Check, in order to validate
                //the user input, afterwards, if there are not errors in the input
                //the createEvent function is called to update the database and
                //the user is transfered to the Profile activity
                check.emptyField_Check(txt_eventName, lbl_eventNameError);
                if (!check.emptyField_Check(txt_eventName, lbl_eventNameError))
                    isCorrect[0] = false;
                check.emptyField_Check(txt_eventLocation, lbl_eventLocationError);
                if (!check.emptyField_Check(txt_eventName, lbl_eventNameError))
                    isCorrect[0] = false;
                check.emptyField_Check(txt_specificInformation, lbl_specificInformationError);
                if (!check.emptyField_Check(txt_specificInformation, lbl_specificInformationError))
                    isCorrect[0] = false;
                check.starting_Time(txt_eventStartingTime, lbl_eventStartingTimeError);
                if (!check.starting_Time(txt_eventStartingTime, lbl_eventStartingTimeError))
                    isCorrect[0] = false;
                check.duration_Check(txt_eventDuration, lbl_eventDurationError);
                if (!check.duration_Check(txt_eventDuration, lbl_eventDurationError))
                    isCorrect[0] = false;

                //if there are no errors with the user input, the create button will update the database, otherwise it will only set the
                //isCorrect to true, in order to properly check the input during the second click of the button
                if (isCorrect[0]) {
                    createEvent(txt_eventName.getText().toString(), txt_eventLocation.getText().toString(), txt_specificInformation.getText().toString(), txt_eventStartingTime.getText().toString(), txt_eventDuration.getText().toString() + " hours", user);
                    startActivity(new Intent(CreateEventActivity.this, ProfileActivity.class));
                } else {
                    isCorrect[0] = true;
                }
            }
        });

        //the Clear button simply removes all the data that has been
        //entered in the input boxes
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_eventName.setText("");
                lbl_eventNameError.setVisibility(View.INVISIBLE);
                txt_eventLocation.setText("");
                lbl_eventLocationError.setVisibility(View.INVISIBLE);
                txt_specificInformation.setText("");
                lbl_specificInformationError.setVisibility(View.INVISIBLE);
                txt_eventStartingTime.setText("");
                lbl_eventStartingTimeError.setVisibility(View.INVISIBLE);
                txt_eventDuration.setText("");
                lbl_eventDurationError.setVisibility(View.INVISIBLE);
            }
        });
    }

    //the createEvent function, responsible for calling the database connection
    //class and working on the data
    public void createEvent(final String e_name, final String e_location, final String e_restrictions, final String e_time, final String e_duration, final String e_createdBy) {
        //creating an instance of DB_CreateEvent class with constructor data
        new DB_CreateEvent("http://carmeets.eu/create_event.php?", e_name, e_location, e_restrictions, e_time, e_duration, e_createdBy).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(CreateEventActivity.this);
        pd.setMessage("Please Wait.");
        pd.show();

        //Setting the timer for the loading screen while the connection is established and the event is created
        final Timer tm = new Timer();
        tm.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //checking if there is a response from the server
                        //no response means the connection hsa failed
                        if (!create.equals("")) {
                            //if there is a response from the server, cancel the progress dialog
                            pd.cancel();
                            create1=create;
                            create1l = create1.trim().length();
                            //if the event has been created successfully, notify the user
                            Toast.makeText(getApplicationContext(), "Event created successfully!", Toast.LENGTH_SHORT).show();
                            create = "";
                            //cancelling the timer
                            tm.cancel();
                            //if the event was not created successfully, notify the user
                            if(create1l==0) { Toast.makeText(getApplicationContext(), "Error creating event!", Toast.LENGTH_LONG).show(); }
                            create1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);
    }
}
