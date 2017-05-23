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

public class ChangePassword extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String res = "";
    static String res1;
    static int res1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //initializing the EditText
        //and Button controls in the activity
        final EditText pass1 = (EditText) findViewById(R.id.pass1);
        final EditText pass2 = (EditText) findViewById(R.id.pass2);
        final Button btn_pass_change = (Button) findViewById(R.id.btn_change);

        //setting up SharedPreferences, in order to
        //store data about the user's username
        final SharedPreferences username = this.getSharedPreferences("username", 0);
        final SharedPreferences.Editor editor = username.edit();

        //initializing a string that will be
        //used to store the hashed password
        final String[] hash_pass = new String[1];

        //assigning the user's username
        //to a string variable, so that it can
        //be assigned to a TextView
        final String username_info = username.getString("username", "username not found!");
        //removing the user's username from the
        //sharedpreferences
        editor.remove("username");
        editor.apply();

        /*initializing a TextView
        with the user's username and
        setting it's text with the corresponding
        username*/
        TextView user_info = (TextView) findViewById(R.id.user_info);
        user_info.setText("Changing password for user " + username_info);

        //setting an onClickListener for
        //the Password Change button
        btn_pass_change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //checking if both passwords
                //are identical, if so
                //proceed with hashing the password and updating
                //the database, otherwise notify the user the password are not identical
                if(pass1.getText().toString().equals(pass2.getText().toString())) {
                    hash_pass[0] = Password_Hashing.md5(pass1.getText().toString());
                    //calling the passwrd function with the user's username and the hashed password
                    passwrd(username_info, hash_pass[0]);
                    //if the two password were not correct, display an error message in a Toast
                } else {Toast.makeText(getApplicationContext(), "Passwords are not the same!", Toast.LENGTH_SHORT).show();}
            }
        });
    }

    //the passwrd function, responsible for calling the database connection
    //class and working on the data
    private void passwrd(final String username, String pass) {

        //creating an instance of DB_PasswordChange class with constructor data
        new DB_PasswordChange("http://carmeets.eu/password_change.php?", username, pass).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(ChangePassword.this);
        pd.setMessage("Please Wait.");
        pd.show();

        //Setting the timer for the loading screen while the connection is established and the password updated
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
                            res = "";
                            //cancelling the timer
                            tm.cancel();
                            //based on the response, notify the user that his/her password has been changed and transfer
                            //him/her to the login activity or notify the user the password change has failed
                            if(res1l!=0){
                                Toast.makeText(getApplicationContext(), "Your password have been changed!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangePassword.this, LoginActivity.class));
                            }
                            else { Toast.makeText(getApplicationContext(), "Could not change password.", Toast.LENGTH_SHORT).show(); }
                            res1 = "";
                        }
                    }
                });
            }
        }, 1, 1000);
    }
}
