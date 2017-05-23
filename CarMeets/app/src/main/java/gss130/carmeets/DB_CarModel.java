package gss130.carmeets;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DB_CarModel extends AsyncTask {
    private String Link = "";
    private String Make = "";


    //Constructor
    public DB_CarModel(String link, String make) {
        Link = link;
        Make = make;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
            try {

                //Initializing the request to the database
                String data = URLEncoder.encode("Make", "UTF8") + "=" + URLEncoder.encode(Make, "UTF8");

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
                Register.cmodel = sb.toString();
            } catch (Exception e) {

            }

        return "";

    }
}
