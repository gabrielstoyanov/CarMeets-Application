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

public class LoginActivity extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String log = "";
    String log1 = "";
    static int log1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing the Buttons that are present in the activity
        //(Register, Login and Forgotten Password)
        final Button btn_register = (Button) findViewById(R.id.btn_register);
        final Button btn_login = (Button) findViewById(R.id.btn_log);
        final Button btn_forgot = (Button) findViewById(R.id.btn_forgot);
        //initializing the EditTexts that are present in the activity
        //(Username and Password)
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        //initializing the TextView that are present in the activity and used as error labels
        //(Username_Error and Password_Error)
        final TextView username_error = (TextView) findViewById(R.id.username_error);
        final TextView password_error = (TextView) findViewById(R.id.password_error);

        //creating an instance of class Login_Input_Checks
        //in order to access it's methods
        final Login_Input_Checks log = new Login_Input_Checks();

        //creating a string array with 1 element
        //to store the hashed password
        final String[] hash_pass = new String[1];

        //setting the onClickListener for the Login Button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting the value of the hashed password
                //to be equal to the output from the md5 function
                //in the Password_Hashing class
                hash_pass[0] = Password_Hashing.md5(password.getText().toString());
                //checking the username field for errors
                log.username_Check(username, username_error);
                //checking the password field for errors
                log.password_Check(password, password_error);
                //if both checks return true, call the login function
                if(log.username_Check(username, username_error) && log.password_Check(password, password_error)){
                    login(username.getText().toString(), hash_pass[0]);
                }
            }
        });

        //setting the onClickListener for the forgotten password button
        //which transfers the user to the ForgottenPassword activity
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgottenPassword.class));
            }
        });

        //setting the onClickListener for the Register button
        //which transfers the user tot he Register activity
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });
    }

    //the login function, responsible for calling the database connection
    //class and working on the data
    public void login(final String username, final String password) {

        //initiating the SharedPreferences, in order to store the name of the user
        final SharedPreferences sp_username = this.getSharedPreferences("username", 0);
        final SharedPreferences.Editor editor = sp_username.edit();

        //creating an instance of DB_Login class with constructor data
        new DB_Login("http://carmeets.eu/login.php?", username, password).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
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
                        if (!log.equals("")) {
                            //if there is a response from the server, cancel the progress dialog
                            pd.cancel();
                            log1 = log;
                            log1l = log1.trim().length();
                            log = "";
                            //cancelling the timer
                            tm.cancel();
                            //if the length of the response is different from 1
                            //notify the user that the login has been successful
                            //and update the username field in sharedpreferences with
                            //the user's username and transfer the user tot he ProfileActivity
                            if(log1l!=1){
                                Toast.makeText(getApplicationContext(), "You have successfully logged in, " + log1 + "!", Toast.LENGTH_SHORT).show();
                                //saving the user's name in shared preferences
                                editor.putString("username", log1);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            }
                            //if the response length is equal to 1
                            //notify the user that the login failed
                            if(log1l==1) { Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show(); }
                            log1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);
    }
}
