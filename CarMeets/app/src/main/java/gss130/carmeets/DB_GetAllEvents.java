package gss130.carmeets;


import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

//this class is responsible for
//obtaining data about all
//event that are present in the database
public class DB_GetAllEvents extends AsyncTask {
    //initializing variables that will be used to construct the
    //data that will be used in the POST method of the PHP file
    private String Link = "";

    //Constructor
    public DB_GetAllEvents(String link) {
        //setting the values of the variables
        //to the ones set when the class is called for execution
        Link = link;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
        try {

            //appending the data to the link
            URL mylink = new URL(Link);
            //opening connection
            URLConnection connect = mylink.openConnection();

            connect.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            //creating a stringbuilder to construct the output from the response
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            //assigning the output from the php to a
            //variable in ViewEvents activity
            ViewEventsActivity.res = sb.toString();
        } catch (Exception e) {}
        return "";
    }
}