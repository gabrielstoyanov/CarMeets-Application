package gss130.carmeets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ProfileActivity extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String data = "";
    String data1 = "";
    int data1l;
    String userLocation;
    String userPreferences;
    String userCar;
    String userGender;
    String userRegisterDate;
    String userAge;
    String userAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initializing the TextViews (labels) that are present in the activity
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView location = (TextView) findViewById(R.id.location);
        final TextView interests = (TextView) findViewById(R.id.preferences);
        final TextView car_info = (TextView) findViewById(R.id.car_info);
        final TextView gender = (TextView) findViewById(R.id.gender);
        final TextView member_since = (TextView) findViewById(R.id.register_date);
        final TextView age = (TextView) findViewById(R.id.age);
        final TextView availability = (TextView) findViewById(R.id.availability);

        //Initializing the Buttons that are present in the activity (Create Event, View Event and Edit Profile)
        final Button btn_create = (Button) findViewById(R.id.btn_create_event);
        final Button btn_view = (Button) findViewById(R.id.btn_view_event);
        final Button btn_edit = (Button) findViewById(R.id.btn_edit);

        //setting up a sharedpreferences objects for the user's username
        //user's location, user's preferences and user's availability
        //along with the corresponding editors for each object
        final SharedPreferences sp_username = this.getSharedPreferences("username", 0);
        final SharedPreferences sp_location = this.getSharedPreferences("location", 0);
        final SharedPreferences.Editor location_editor = sp_location.edit();
        final SharedPreferences sp_preferences = this.getSharedPreferences("preferences", 0);
        final SharedPreferences.Editor preferences_editor = sp_preferences.edit();
        final SharedPreferences sp_availability = this.getSharedPreferences("availability", 0);
        final SharedPreferences.Editor availability_editor = sp_availability.edit();

        //initializing a string to hold the user's username
        final String user = sp_username.getString("username", "Username not found!");

        //establishing database connection in order to populate the
        //textviews for the profile information
        new DB_ProfileData("http://carmeets.eu/profile.php?", user).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
        pd.setMessage("Please Wait.");
        pd.show();

        //Setting the timer for the loading screen while the connection is established and the credentials checked
        final Timer tm = new Timer();
        tm.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //checking if there is a response from the server
                        //no response means the connection has failed
                        if (!data.equals("")) {
                            //if there is a response from the server, cancel the progress dialog
                            pd.cancel();
                            data1 = data;
                            data1l = data1.trim().length();
                            String[] data_array = data1.split("&");
                            //setting the values in the textviews
                            //and saving the corresponding data in the sharedpreferences
                            username.setText(user);
                            userLocation = data_array[0];
                            location.setText(userLocation);
                            location_editor.putString("location", userLocation);
                            location_editor.apply();
                            userPreferences = data_array[2];
                            interests.setText(userPreferences);
                            preferences_editor.putString("preferences", userPreferences);
                            preferences_editor.apply();
                            userCar = data_array[3];
                            car_info.setText(userCar);
                            userGender = data_array[4];
                            gender.setText(userGender);
                            userRegisterDate = data_array[5];
                            member_since.setText(convertDate(userRegisterDate));
                            userAge = data_array[6];
                            age.setText(userAge);
                            userAvailability = data_array[7];
                            availability.setText(userAvailability);
                            availability_editor.putString("availability", userAvailability);
                            availability_editor.apply();
                            data = "";
                            //cancelling the timer
                            tm.cancel();
                            //if there was an error extracting the data, notify the user
                            if(data1l==0) { Toast.makeText(getApplicationContext(), "Error extracting the data!", Toast.LENGTH_LONG).show(); }
                            data1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);

        //setting the onClickListener for the Create Event button
        //and transferring the user to the CreateEventActivity
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CreateEventActivity.class));
            }
        });

        //setting the onClickListener for the Profile Edit button
        //and transferring the user
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));
            }
        });

        //setting the onClickListener for the View Events button
        //and transferring the user to the EventsDisplayType activity
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EventsDisplayTypeActivity.class));
            }
        });
    }

    //function to convert the data into an acceptable string format
    String convertDate(String rawDate){
        String[] dateArray;
        String[] dateArray1;
        StringBuilder newString = new StringBuilder();
        String date1;
        dateArray=rawDate.split(" ");
        date1=dateArray[0];
        dateArray1=date1.split("-");
        newString.append(dateArray1[2]);
        newString.append(".");
        newString.append(dateArray1[1]);
        newString.append(".");
        newString.append(dateArray1[0]);
        return newString.toString();
    }
}
