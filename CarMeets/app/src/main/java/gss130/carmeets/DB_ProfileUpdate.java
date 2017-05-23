package gss130.carmeets;


import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//this class is responsible for
//updating the profile information
//about specific user into the database
public class DB_ProfileUpdate extends AsyncTask {
    //initializing variables that will be used to construct the
    //data that will be used in the POST method of the PHP file
    private String Link = "";
    private String Username = "";
    private String Location = "";
    private String Age = "";
    private String Preferences = "";
    private String Availability = "";
    private String Car_Info = "";
    private String Gender = "";
    private String Email = "";

    //Constructor
    public DB_ProfileUpdate(String link, String username, String location, String age, String preferences, String availability, String car_Info, String gender, String email) {
        //setting the values of the variables
        //to the ones set when the class is called for execution
        Link = link;
        Username = username;
        Location = location;
        Age = age;
        Preferences = preferences;
        Availability = availability;
        Car_Info = car_Info;
        Gender = gender;
        Email = email;
    }

    //Defining the doInBackground function of ASyncTask
    @Override
    protected String doInBackground(Object[] params) {
        try {

            //Initializing the request to the database
            String data = URLEncoder.encode("username", "UTF8") + "=" + URLEncoder.encode(Username, "UTF8");
            data += "&" + URLEncoder.encode("location", "UTF8") + "=" + URLEncoder.encode(Location, "UTF8");
            data += "&" + URLEncoder.encode("age", "UTF8") + "=" + URLEncoder.encode(Age, "UTF8");
            data += "&" + URLEncoder.encode("preferences", "UTF8") + "=" + URLEncoder.encode(Preferences, "UTF8");
            data += "&" + URLEncoder.encode("availability", "UTF8") + "=" + URLEncoder.encode(Availability, "UTF8");
            data += "&" + URLEncoder.encode("car_info", "UTF8") + "=" + URLEncoder.encode(Car_Info, "UTF8");
            data += "&" + URLEncoder.encode("gender", "UTF8") + "=" + URLEncoder.encode(Gender, "UTF8");
            data += "&" + URLEncoder.encode("email", "UTF8") + "=" + URLEncoder.encode(Email, "UTF8");

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
            //variable in ProfileEditActivity activity
            ProfileEditActivity.res = sb.toString();
        } catch (Exception e) {}
        return "";
    }
}