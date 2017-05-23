package gss130.carmeets;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//this class is responsible
//for update the password in the database
//for a specific user
public class DB_PasswordChange extends AsyncTask {
    //initializing variables that will be used to construct the
    //data that will be used in the POST method of the PHP file
    private String Username = "";
    private String Link = "";
    private String Pass = "";

    //Constructor
    public DB_PasswordChange(String link, String username, String pass) {
        //setting the values of the variables
        //to the ones set when the class is called for execution
        Link = link;
        Username = username;
        Pass = pass;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
        try {

            //Initializing the request to the database
            String data = URLEncoder.encode("username", "UTF8") + "=" + URLEncoder.encode(Username, "UTF8");
            data += "&" + URLEncoder.encode("password", "UTF8") + "=" + URLEncoder.encode(Pass, "UTF8");

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
            //variable in ChangePassword activity
            ChangePassword.res = sb.toString();
        } catch (Exception e) {}
        return "";
    }
}