package gss130.carmeets;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class EventInformationActivity extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String res = "";
    String res1 = "";
    int res1l;
    public static String data = "";
    String data1 = "";
    int data1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);

        //declaring a string that will hold data
        //about the users that are attending specific event
        final String[] users = new String[1];

        //initializing the TextViews (labels) that are present
        //in the activity
        final TextView lbl_eventName = (TextView) findViewById(R.id.lbl_eventName);
        final TextView lbl_location = (TextView) findViewById(R.id.lbl_location);
        final TextView lbl_restrictions = (TextView) findViewById(R.id.lbl_restrictions);
        final TextView lbl_startingTIme = (TextView) findViewById(R.id.lbl_startingTime);
        final TextView lbl_eventDuration = (TextView) findViewById(R.id.lbl_eventDuration);
        final TextView lbl_createdBy = (TextView) findViewById(R.id.lbl_createdBy);
        final TextView lbl_createdOn = (TextView) findViewById(R.id.lbl_createdOn);
        final TextView lbl_attending = (TextView) findViewById(R.id.lbl_attending);

        //initializing the Button that is
        //present in the activity (Sign Up)
        final Button btn_signUp = (Button) findViewById(R.id.btn_signUp);

        //setting up a sharedPreferences about the event's name
        final SharedPreferences sp_eventName = EventInformationActivity.this.getSharedPreferences("e_name", 0);
        //setting a sharedPReferences about the user's username
        final SharedPreferences sp_username = EventInformationActivity.this.getSharedPreferences("username", 0);
        //tried to that in a method, but could not make it work
        //getting no data from the database and
        //textviews were null (ASyncTask was not working properly)

        //creating an instance of DB_EventInformation class with constructor data
        new DB_EventInformation("http://carmeets.eu/view_specific_event.php?", sp_eventName.getString("e_name", "No event found!")).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(EventInformationActivity.this);
        pd.setMessage("Please Wait.");
        pd.show();

        //Setting the timer for the loading screen while the connection is established and
        //the event information is received
        final Timer tm = new Timer();
        tm.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //checking if there is a response from the server
                        //no response means the connection hsa failed
                        if (!res.equals("")) {
                            //if there is a response from the server, cancel the progress dialog
                            pd.cancel();
                            res1 = res;
                            res1l = res1.trim().length();
                            //splitting the output from the database by using
                            //the "&" sign as a delimiter
                            String[] data_array = res1.split("&");
                            //settings the text for the various labels
                            lbl_eventName.setText("Event Name: " + data_array[0]);
                            lbl_location.setText("Event Location: " + data_array[1]);
                            lbl_restrictions.setText("Additional Information: " + data_array[2]);
                            lbl_startingTIme.setText("Starting Time: " + data_array[3]);
                            lbl_eventDuration.setText("Duration: " + data_array[4]);
                            lbl_createdBy.setText("Created By: " + data_array[5]);
                            lbl_createdOn.setText("Time of Creation: " + data_array[6]);
                            users[0] = data_array[7];
                            lbl_attending.setText("Users Attending: " + users[0]);
                            res = "";
                            //cancelling the timer
                            tm.cancel();
                            //notifying the user if there is an error exctracting the data
                            if(res1l==0) { Toast.makeText(getApplicationContext(), "Error extracting the data!", Toast.LENGTH_LONG).show(); }
                            res1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);

        //setting up the onClickListener for the Sign Up button
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declaring a string that will have the
                //the current users attending with the addition of
                //the new user that has signed up for the event
                String newAttending;
                //checking whether the logged user is already signed up
                //if he/she is - display a message "You are already signed in!"
                //otherwise append the new users to the old users and
                //call the signup method to update the database
                    if(checkAttending(users[0], sp_username.getString("username", "no username found"))) {
                    newAttending = users[0] + "," + sp_username.getString("username", "username not found");
                    signup(sp_eventName.getString("e_name", "Event not found!"), newAttending);
                } else { Toast.makeText(getApplicationContext(), "You are already signed in!", Toast.LENGTH_SHORT).show(); }
                }
        });
    }

    //method to check whether a users is already signed up for an event
    boolean checkAttending(String users1, String username){
        //splitting the signed up users using "," as a delimiter into an array
        String[] users_array = users1.split(",");
        //looping through the array, if the user
        //is found in the signed up users
        //return false, otherwise return true
        for(int i=0;i<users_array.length;i++){
            if(Objects.equals(users_array[i], username)) {
                return false; }
        }
        return true;
    }

    public void signup(final String eventName, final String usersAttending) {
        //creating an instance of DB_EventSignUp class with constructor data
        new DB_EventSignUp("http://carmeets.eu/event_sign_up.php?", eventName, usersAttending).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(EventInformationActivity.this);
        pd.setMessage("Please Wait.");
        pd.show();

        //Setting the timer for the loading screen while the connection is established and the credentials checked
        final Timer tm = new Timer();
        tm.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //checking if there is a response from the server
                        //no response means the connection hsa failed
                        if (!data.equals("")) {
                            //if there is a response from the server, cancel the progress dialog
                            pd.cancel();
                            data1 = data;
                            data1l = data1.trim().length();
                            data = "";
                            //cancelling the timer
                            tm.cancel();
                            //if the data length is more than one - notify the user that he/she has signed up for the event
                            //and transfer the uer to the profile activity
                            if(data1l!=0){
                                Toast.makeText(getApplicationContext(), "You have successfully signed up! ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EventInformationActivity.this, ProfileActivity.class));
                            }
                            //if the response from the server's length is equal to 0,
                            //notify the users that the sign up has failed
                            if(data1l==0) { Toast.makeText(getApplicationContext(), "Event sign up failed!", Toast.LENGTH_LONG).show(); }
                            data1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);
    }
}
