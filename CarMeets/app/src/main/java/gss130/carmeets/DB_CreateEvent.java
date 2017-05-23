package gss130.carmeets;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//This class is responsible for
//sending a request to the database
//to create an event in the database
public class DB_CreateEvent extends AsyncTask {
    //initializing variables that will be used to construct the
    //data that will be used in the POST method of the PHP file
    private String Link = "";
    private String E_Name = "";
    private String E_Location = "";
    private String E_Restrictions = "";
    private String E_Time = "";
    private String E_Duration = "";
    private String E_CreatedBy = "";

    //Constructor
    public DB_CreateEvent(String link, String e_name, String e_location, String e_restrictions, String e_time, String e_duration, String e_createdBy) {
        //setting the values of the variables
        //to the ones set when the class is called for execution
        Link = link;
        E_Name = e_name;
        E_Location = e_location;
        E_Restrictions = e_restrictions;
        E_Time = e_time;
        E_Duration = e_duration;
        E_CreatedBy = e_createdBy;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
        try {
            //Initializing the request to the database
            String data = URLEncoder.encode("e_name", "UTF8") + "=" + URLEncoder.encode(E_Name, "UTF8");
            data += "&" + URLEncoder.encode("e_location", "UTF8") + "=" + URLEncoder.encode(E_Location, "UTF8");
            data += "&" + URLEncoder.encode("e_restrictions", "UTF8") + "=" + URLEncoder.encode(E_Restrictions, "UTF8");
            data += "&" + URLEncoder.encode("e_time", "UTF8") + "=" + URLEncoder.encode(E_Time, "UTF8");
            data += "&" + URLEncoder.encode("e_duration", "UTF8") + "=" + URLEncoder.encode(E_Duration, "UTF8");
            data += "&" + URLEncoder.encode("e_createdBy", "UTF8") + "=" + URLEncoder.encode(E_CreatedBy, "UTF8");

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

            //constructing the output from the response
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            //assigning the output from the php to a
            //variable in CreateEventActivity
            CreateEventActivity.create = sb.toString();
        } catch (Exception e) {}
        return "";
    }
}
