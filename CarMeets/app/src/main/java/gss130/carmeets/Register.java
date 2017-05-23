package gss130.carmeets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class Register extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String reg = "";
    String reg1 = "";
    public static String mk;
    String mk1;
    public static String make_final = "";
    public static String cmodel;
    String cmodel1 = "";
    public static String cmodel_final = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing the Buttons, EditTexts and TextViews that are
        //present in the activity
        final Button btn_reg = (Button) findViewById(R.id.btn_reg);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText location = (EditText) findViewById(R.id.location);
        final EditText age = (EditText) findViewById(R.id.age);
        final EditText pref = (EditText) findViewById(R.id.pref);
        final EditText avail = (EditText) findViewById(R.id.avail);
        final EditText car_info = (EditText) findViewById(R.id.car_info);
        final EditText email = (EditText) findViewById(R.id.email);
        final TextView username_error = (TextView) findViewById(R.id.username_error);
        final TextView password_error = (TextView) findViewById(R.id.password_error);
        final TextView location_error = (TextView) findViewById(R.id.location_error);
        final TextView age_error = (TextView) findViewById(R.id.age_error);
        final TextView pref_error = (TextView) findViewById(R.id.pref_error);
        final TextView avail_error = (TextView) findViewById(R.id.avail_error);
        final TextView car_error = (TextView) findViewById(R.id.car_error);
        final TextView email_error = (TextView) findViewById(R.id.email_error);
        final Spinner gender = (Spinner) findViewById(R.id.gender);

        //making an instance of class Register_Input_Checks
        //so that the methods present in the class can be used
        //for user input checks
        final Register_Input_Checks check = new Register_Input_Checks();

        final String[] gend = new String[1];
        final String[] car_make = new String[1];
        final String[] car_model = new String[1];

        final String[] makes = {""};

        final String[] full_car_info = new String[1];

        final String[] hash_pass = new String[1];

        final boolean[] isCorrect = new boolean[1];

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //running the input checks
                //if an input is not correct, isCorrect is set to false, will not let the register button continue with updating the database
                check.username_Check(username, username_error);
                if(!check.username_Check(username, username_error)) isCorrect[0] = false;
                check.password_Check(password, password_error);
                if(!check.password_Check(password, password_error)) isCorrect[0] = false;
                check.location_Check(location, location_error);
                if(!check.location_Check(location, location_error)) isCorrect[0] = false;
                check.age_Check(age, age_error);
                if(!check.age_Check(age, age_error)) isCorrect[0] = false;
                check.pref_Check(pref, pref_error);
                if(!check.pref_Check(pref, pref_error)) isCorrect[0] = false;
                check.avail_Check(avail, avail_error);
                if(!check.avail_Check(avail, avail_error)) isCorrect[0] = false;
                /*check.car_Spinner_Check(make, model, car_mm_error);
                if(!check.car_Spinner_Check(make, model, car_mm_error)) isCorrect[0] = false;*/
                check.car_Check(car_info, car_error);
                if(!check.car_Check(car_info, car_error)) isCorrect[0] = false;
                check.checkEmail(email, email_error);
                if(!check.checkEmail(email, email_error)) isCorrect[0] = false;
                gend[0] = gender.getSelectedItem().toString();
/*                car_make[0] = make.getSelectedItem().toString();
                car_model[0] = model.getSelectedItem().toString();
                full_car_info[0] = car_make[0] + " " + car_model[0];*/
                hash_pass[0] = Password_Hashing.md5(password.getText().toString());
                //if there are no errors with the user input, the register button will update the database, otherwise it will only set the
                //isCorrect to true, in order to properly check the input during the second click of the button
                if(isCorrect[0]){
                    reg(username.getText().toString(), hash_pass[0], location.getText().toString(), age.getText().toString(), pref.getText().toString(), avail.getText().toString(), car_info.getText().toString(), gend[0], email.getText().toString());
                    startActivity(new Intent(Register.this, LoginActivity.class));
                } else { isCorrect[0] = true; }
            }
        });


    }

    public void reg(final String username, final String password, final String location, final String age, final String preferences, final String availability, final String car_info, final String gender, final String email) {
        //creating an instance of DB_Register class with constructor data
        new DB_Register("http://carmeets.eu/reg.php?", username, password, location, age, preferences, availability, car_info, gender, email).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(Register.this);
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
                        if (!reg.equals("")) {
                            //if there is a response from the server, cancel the progress dialog
                            //and notify the user the registration was successful
                            Toast.makeText(getApplicationContext(), "You have registered successfully!", Toast.LENGTH_SHORT).show();
                            //cancelling the timer
                            pd.cancel();
                            reg1=reg;
                            reg = "";
                            //cancelling the timer
                            tm.cancel();
                            reg1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);
    }
}
