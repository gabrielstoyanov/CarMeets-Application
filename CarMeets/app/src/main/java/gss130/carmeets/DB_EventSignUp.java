package gss130.carmeets;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//this class is responsible for
//signing a user up for a
//specific event
public class DB_EventSignUp extends AsyncTask {
    //initializing variables that will be used to construct the
    //data that will be used in the POST method of the PHP file
    private String Link = "";
    private String EventName = "";
    private String Username = "";



    //Constructor
    public DB_EventSignUp(String link, String eName, String username) {
        //setting the values of the variables
        //to the ones set when the class is called for execution
        Link = link;
        EventName = eName;
        Username = username;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
        try {
            //Initializing the request to the database
            String data = URLEncoder.encode("eventName", "UTF8") + "=" + URLEncoder.encode(EventName, "UTF8");
            data += "&" + URLEncoder.encode("users_attending", "UTF8") + "=" + URLEncoder.encode(Username, "UTF8");

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
            //variable in EventInformationActivity activity
            EventInformationActivity.data = sb.toString();
        } catch (Exception e) {}
        return "";
    }
}