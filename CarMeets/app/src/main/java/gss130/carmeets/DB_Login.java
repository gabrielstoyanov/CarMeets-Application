package gss130.carmeets;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//this class is responsible for
//checking from the database whether the
//login credentials that the user entered are correct
public class DB_Login extends AsyncTask {
    //initializing variables that will be used to construct the
    //data that will be used in the POST method of the PHP file
    private String Link = "";
    private String Username = "";
    private String Password = "";


    //Constructor
    public DB_Login(String link, String username, String password) {
        //setting the values of the variables
        //to the ones set when the class is called for execution
        Link = link;
        Username = username;
        Password = password;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
        try {

            //Initializing the request to the database
            String data = URLEncoder.encode("username", "UTF8") + "=" + URLEncoder.encode(Username, "UTF8");
            data += "&" + URLEncoder.encode("password", "UTF8") + "=" + URLEncoder.encode(Password, "UTF8");

            //appending the data to the link
            URL mylink = new URL(Link + data);
            //opening connection
            URLConnection connect = mylink.openConnection();

            connect.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            //creating a stringbuilder to construct the output from the response
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            //assigning the output from the php to a
            //variable in LoginActivity activity
            LoginActivity.log = sb.toString();
        } catch (Exception e) {}
        return "";
    }
}
