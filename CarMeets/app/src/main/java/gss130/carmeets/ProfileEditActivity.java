package gss130.carmeets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ProfileEditActivity extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String data = "";
    String data1 = "";
    static int data1l;
    public static String res = "";
    String res1 = "";

    final boolean[] isCorrect = new boolean[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        //making an instance of class Register_Input_Checks
        //in order to use the methods inside for checking the
        //user input
        final Register_Input_Checks check = new Register_Input_Checks();

        //Initializing the Buttons that are present in the activity (Submit)
        Button btn_submit = (Button) findViewById(R.id.btn_submit);

        //initializing the TextViews, EditTexts and Spinners
        //that are present in the activity
        final TextView txt_username = (TextView) findViewById(R.id.txt_username);
        final EditText txt_location = (EditText) findViewById(R.id.txt_location);
        final EditText txt_age = (EditText) findViewById(R.id.txt_age);
        final EditText txt_preferences = (EditText) findViewById(R.id.txt_preferences);
        final EditText txt_carInfo = (EditText) findViewById(R.id.txt_carInfo);
        final EditText txt_email = (EditText) findViewById(R.id.txt_email);
        final EditText txt_availability = (EditText) findViewById(R.id.txt_availability);
        final Spinner sp_gender = (Spinner) findViewById(R.id.sp_gender);
        final TextView lbl_locationError = (TextView) findViewById(R.id.lbl_locationError);
        final TextView lbl_ageError = (TextView) findViewById(R.id.lbl_ageError);
        final TextView lbl_preferencesError = (TextView) findViewById(R.id.lbl_preferencesError);
        final TextView lbl_carInfoError = (TextView) findViewById(R.id.lbl_carInfoError);
        final TextView lbl_emailError = (TextView) findViewById(R.id.lbl_emailError);
        final TextView lbl_availabilityError = (TextView) findViewById(R.id.lbl_availabilityError);

        final String[] gend = new String[1];

        //setting up a sharedpreferences object for the user's username
        final SharedPreferences sp_username = this.getSharedPreferences("username", 0);
        final String user = sp_username.getString("username", "Username not found!");

        //establishing database connection in order to populate the
        //textviews for the profile information
        new DB_GetProfileInfo("http://carmeets.eu/get_profile_data.php?", user).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(ProfileEditActivity.this);
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
                            String[] data_array = data1.split("&");
                            //populating the EditTexts with the user's profile
                            //information
                            txt_username.setText(user);
                            txt_location.setText(data_array[0]);
                            txt_age.setText(data_array[1]);
                            txt_preferences.setText(data_array[2]);
                            txt_availability.setText(data_array[3]);
                            txt_carInfo.setText(data_array[4]);
                            gend[0] =data_array[5];
                            txt_email.setText(data_array[6]);
                            if(Objects.equals(gend[0], "Male")){
                                sp_gender.setSelection(0);
                            } else {sp_gender.setSelection(1);}
                            data = "";
                            //cancelling the timer
                            tm.cancel();
                            //if there is a problem extracting the data, notify the user
                            if(data1l==0) { Toast.makeText(getApplicationContext(), "Error extracting the data!", Toast.LENGTH_LONG).show(); }
                            data1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //running the input checks
                //if an input is not correct, isCorrect is set to false, will not let the register button continue with updating the database
                isCorrect[0] = true;
                check.location_Check(txt_location, lbl_locationError);
                if(!check.location_Check(txt_location, lbl_locationError)) isCorrect[0] = false;
                check.age_Check(txt_age, lbl_ageError);
                if(!check.age_Check(txt_age, lbl_ageError)) isCorrect[0] = false;
                check.pref_Check(txt_preferences, lbl_preferencesError);
                if(!check.pref_Check(txt_preferences, lbl_preferencesError)) isCorrect[0] = false;
                check.avail_Check(txt_availability, lbl_availabilityError);
                if(!check.avail_Check(txt_availability, lbl_availabilityError)) isCorrect[0] = false;
                check.car_Check(txt_carInfo, lbl_carInfoError);
                if(!check.car_Check(txt_carInfo, lbl_carInfoError)) isCorrect[0] = false;
                check.checkEmail(txt_email, lbl_emailError);
                if(!check.checkEmail(txt_email, lbl_emailError)) isCorrect[0] = false;
                //if there are no errors with the user input, the register button will update the database, otherwise it will only set the
                //isCorrect to true, in order to properly check the input during the second click of the button
                if(isCorrect[0]){
                    reg(user, txt_location.getText().toString(), txt_age.getText().toString(), txt_preferences.getText().toString(), txt_availability.getText().toString(), txt_carInfo.getText().toString(), gend[0], txt_email.getText().toString());
                    startActivity(new Intent(ProfileEditActivity.this, ProfileActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "There is error in the data!", Toast.LENGTH_SHORT).show();
                    isCorrect[0] = true; }
            }
        });
    }

    //function, responsible for updating the user's profile information
    public void reg(final String username, final String location, final String age, final String preferences, final String availability, final String car_info, final String gender, final String email) {
        new DB_ProfileUpdate("http://carmeets.eu/update_profile.php?", username, location, age, preferences, availability, car_info, gender, email).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(ProfileEditActivity.this);
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
                            //and notify the user that the update was successful
                            Toast.makeText(getApplicationContext(), "You have updated your profile successfully!", Toast.LENGTH_SHORT).show();
                            pd.cancel();
                            res1=res;
                            res = "";
                            //cancelling the timer
                            tm.cancel();
                            res1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);
    }
}
