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

public class ForgottenPassword extends AppCompatActivity {

    //creating variables to store and manipulate
    //the returned database data
    public static String res = "";
    static String res1;
    static int res1l;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        //initializing the TextView present in the activity
        final TextView email = (TextView) findViewById(R.id.username);

        //initializing the Button (Submit) present in the activity
        final Button btn_submit = (Button) findViewById(R.id.btn_submit);

        //setting the onClickListener for the Submit button
        //and calling the mail function with the user entered
        //email as a parameter
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail(email.getText().toString());
            }
        });
    }

    private void mail(final String mail) {

        //initiating the SharedPreferences, in order to store the username
        final SharedPreferences sp_email = this.getSharedPreferences("username", 0);
        final SharedPreferences.Editor editor = sp_email.edit();

        //creating an instance of DB_EmailCheck class with constructor data
        new DB_EmailCheck("http://carmeets.eu/mail_check.php?", mail).execute();
        //creating a progress dialog (the circling animation while the connection with the database is established
        //and all the work with the data is being completed
        final ProgressDialog pd = new ProgressDialog(ForgottenPassword.this);
        pd.setMessage("Please Wait.");
        pd.show();

        //Setting the timer for the loading screen while the connection is established and the email is checked
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
                            //if the response is longer than 1 character, add the username to the saredpreferences
                            //and transfer the user to the ChangePassword activity
                            if(res1l!=1){
                                username= res1;
                                //saving the username in shared preferences
                                editor.putString("username", username);
                                editor.apply();
                                startActivity(new Intent(ForgottenPassword.this, ChangePassword.class));}
                        }
                        //if the response length is equal to 1, notify the user that the email
                        //that he/she inputted is wrong
                        if(res1l==1){ Toast.makeText(getApplicationContext(), "Wrong Email!", Toast.LENGTH_LONG).show();}
                        res1 = "";
                    }
                });
            }
        }, 1, 1000);
    }
}
