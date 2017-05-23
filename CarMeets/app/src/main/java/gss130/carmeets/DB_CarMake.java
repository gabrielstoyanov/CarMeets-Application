package gss130.carmeets;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

//This class is currently not used by the application
//it will be used in future development
//in order to populate spinners with
//car make and car model, similar to
//the way used car websites let
//the user choose make and model
public class DB_CarMake extends AsyncTask {
    private String Link = "";

    //Constructor
    public DB_CarMake(String link) {
        Link = link;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
        try {

            //Initializing the request to the database
            String data = "";

            URL mylink = new URL(Link + data);
            URLConnection connect = mylink.openConnection();

            connect.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Register.mk = sb.toString();
        } catch (Exception e) {}
        return "";
    }
}
