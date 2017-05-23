package gss130.carmeets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class ViewEventsActivity extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String res = "";
    public String res1 = "";
    public int res1l;
    public int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        //setting a sharedpreferences object to hold data about
        //the display type, location, preferences and availability
        final SharedPreferences sp_displayType = this.getSharedPreferences("display", 0);
        final String displayType = sp_displayType.getString("display", "Display type error!");
        final SharedPreferences sp_location = this.getSharedPreferences("location", 0);
        final String location = sp_location.getString("location", "Location error!");
        final SharedPreferences sp_preferences = this.getSharedPreferences("preferences", 0);
        final String preferences = sp_preferences.getString("preferences", "Preferences error!");
        final SharedPreferences sp_availability = this.getSharedPreferences("availability", 0);
        final String availability = sp_availability.getString("availability", "Availability error!");

        //creating a switch to differentiate between showing all events
        //and showing the suggested events only
        switch(displayType){
            case "All":
                //calling DB_GetAllEvents constructor
                new DB_GetAllEvents("http://carmeets.eu/view_all_events.php?").execute();
                //creating a progress dialog (the circling animation while the connection with the database is established
                //and all the work with the data is being completed
                final ProgressDialog pd = new ProgressDialog(ViewEventsActivity.this);
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
                                if (!res.equals("")) {
                                    //if there is a response from the server, cancel the progress dialog
                                    pd.cancel();
                                    res1 = res;
                                    res1l = res1.trim().length();
                                    String[] data_array = res1.split("#");
                                    //dynamically creating buttons by calling createButton
                                    //function, based on the response length
                                    for(int i=0;i<data_array.length;i++){
                                        createButton(data_array[i]);
                                    }
                                    res = "";
                                    //cancelling the timer
                                    tm.cancel();
                                    if(res1l==0) { Toast.makeText(getApplicationContext(), "Error extracting the data!", Toast.LENGTH_LONG).show(); }
                                    res1 = "";
                                }
                            }
                        });
                    }
                }, 1, 1000);
                break;

            case "Suggested":
                //calling DB_GetAllEvents constructor
                new DB_GetAllEvents("http://carmeets.eu/view_all_events.php?").execute();
                //creating a progress dialog (the circling animation while the connection with the database is established
                //and all the work with the data is being completed
                final ProgressDialog pd1 = new ProgressDialog(ViewEventsActivity.this);
                pd1.setMessage("Please Wait.");
                pd1.show();

                //Setting the timer for the loading screen while the connection is established and the credentials checked
                final Timer tm1 = new Timer();
                tm1.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //checking if there is a response from the server
                                //no response means the connection hsa failed
                                if (!res.equals("")) {
                                    //if there is a response from the server, cancel the progress dialog
                                    pd1.cancel();
                                    res1 = res;
                                    res1l = res1.trim().length();
                                    String[] data_array = res1.split("#");
                                    //creating the suggested events
                                    //if the location, preferences or availability
                                    //matches the user's ones
                                    //create a button, otherwise don't create anything
                                    for(int i=0;i<data_array.length;i++){
                                        String[] separated_event = data_array[i].split("&");
                                        if(location_check(separated_event[1], location) || preferences_check(separated_event[2], preferences) || availability_check(separated_event[3], availability)){
                                        createButton(data_array[i]);}
                                    }
                                    res = "";
                                    //cancelling the timer
                                    tm1.cancel();
                                    if(res1l==0) { Toast.makeText(getApplicationContext(), "Error extracting the data!", Toast.LENGTH_LONG).show(); }
                                    res1 = "";
                                }
                            }
                        });
                    }
                }, 1, 1000);
                break;
        }
    }

    //function checking whether the locations are identical
    boolean location_check(String event_location, String user_location){
        if(event_location.toLowerCase().equals(user_location.toLowerCase())){
            return true;
        }
        return false;
    }

    //function checking whether preferences match
    boolean preferences_check(String event_restrictions, String user_pref){
        String[] event = event_restrictions.trim().split(",");
        String[] user = user_pref.trim().split(",");
            for(int i=0; i<event.length; i++){
            for(int j=0; j<user.length; j++){
                if(event[i].trim().equalsIgnoreCase(user[j].trim())){
                    return true;
                }
            }
        }
        return false;
    }

    //function checking whether the availabilities are fit
    boolean availability_check(String e_time, String u_time){
        String[] u_timeSplitFirst = u_time.split("-");
        String[] e_timeSplit = e_time.split(":");
        int e_time1 = Integer.parseInt(e_timeSplit[0]);
        int e_time2 = Integer.parseInt(e_timeSplit[1]);
        String[] u_timeSplit1 = u_timeSplitFirst[0].split(":");
        int u_time1 = Integer.parseInt(u_timeSplit1[0]);
        int u_time2 = Integer.parseInt(u_timeSplit1[1]);
        if((e_time1 > u_time1) || ((e_time1 == u_time1) && (e_time2 > u_time2)) || ((e_time1 == u_time1) && (e_time2 == u_time2))){
            return true;
        }
        return false;
    }

    //function to create buttons dynamically
    void createButton(String str){
        String[] str_array = str.split("&");
        final String e_name = str_array[0];
        String e_location = str_array[1];
        String e_restriction = str_array[2];
        String e_time = str_array[3];
        String e_duration = str_array[4];
        String e_createBy = str_array[5];
        String e_creationTime = str_array[6];
        Button myButton = new Button(this);
        myButton.setText("Event Name: " + e_name + "\nEvent Location: "  + e_location + "\nEvent Restrictions: " + e_restriction
        + "\nStarting Time: " + e_time + "\nEvent Duration: " + e_duration + "\nCreated By: " + e_createBy + "\nCreation Date: " + e_creationTime);
        myButton.setId(num);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutID);
        layout.addView(myButton);

        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //initiating the SharedPreferences, in order to store the name of the user
                final SharedPreferences sp_eventName = ViewEventsActivity.this.getSharedPreferences("e_name", 0);
                final SharedPreferences.Editor editor = sp_eventName.edit();
                editor.putString("e_name", e_name);
                editor.apply();
                startActivity(new Intent(ViewEventsActivity.this, EventInformationActivity.class));

            }
        });
        num++;
    }

}



