package gss130.carmeets;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.Pattern;


class Register_Input_Checks {

    //declaring a constant that hold data about the
    //valid e-mail address' pattern
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    //input check for the username field;
    //if the field is empty or less than 4 symbols, an error will be shown
    boolean username_Check(EditText username, TextView username_error){
        String user = username.getText().toString();
        if(user.length()==0){
            username_error.setText("Empty username field!");
            username_error.setVisibility(View.VISIBLE);
            return false;
        }
        if(username.length()<4 && username.length()>0){
            username_error.setText("Username must be at least 4 symbols long!");
            username_error.setVisibility(View.VISIBLE);
            return false;
        }
        username_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //function checking whether a field is empty
    boolean emptyField_Check(EditText field, TextView error){
        String str_field = field.getText().toString();
        if(str_field.length()==0){
            error.setText("Empty field!");
            error.setVisibility(View.VISIBLE);
            return false;
        }
        error.setVisibility(View.INVISIBLE);
        return true;
    }

    //input check for password field
    //if the password field is empty or less than 4 symbols, an error will be shown
    boolean password_Check(EditText password, TextView password_error){
        String pass = password.getText().toString().trim();
        if(pass.length()==0){
            password_error.setText("Password field empty!");
            password_error.setVisibility(View.VISIBLE);
            return false;
        }
        if(pass.length()>0 && pass.length()<4){
            password_error.setText("Password should be at least 4 symbols");
            password_error.setVisibility(View.VISIBLE);
            return false;
        }
        password_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //input check for the location field
    //if the field is empty, an error will be shown
    boolean location_Check(EditText location, TextView location_error){
        String loc = location.getText().toString();
        if(loc.length()==0){
            location_error.setText("Location field is empty!");
            location_error.setVisibility(View.VISIBLE);
            return false;
        }
        location_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //input check for the age field
    //if the field is empty, non-numeric or less than 1 or more than 120
    //an error will be shown
    boolean age_Check(EditText age, TextView age_error){
        if(age.getText().toString().length()>0) {
            try {
                int a = Integer.parseInt(age.getText().toString());
                if(a>0 && a<=120){
                age_error.setVisibility(View.INVISIBLE);
                return true;}
                else {
                    age_error.setText("Age must be between 1 and 120!");
                    age_error.setVisibility(View.VISIBLE);
                    return false;
                }
            } catch (NumberFormatException e) {
                age_error.setText("Age must be a number!");
                age_error.setVisibility(View.VISIBLE);
                return false;
            }
        } else {
            age_error.setText("Age field is empty!");
            age_error.setVisibility(View.VISIBLE);
            return false;
        }
    }

    //input check for preferences field
    //if the field is empty, an error will be shown
    boolean pref_Check(EditText pref, TextView pref_error){
        if(pref.getText().toString().length()==0){
            pref_error.setText("Preferences field is empty!");
            pref_error.setVisibility(View.VISIBLE);
            return false;
        }
        pref_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //input check for availability field
    //if the field is empty or the data entered is in the wrong format
    //an error will be shown
    boolean avail_Check(EditText avail, TextView avail_error) {
        if (avail.getText().toString().length() == 0) {
            avail_error.setText("Availability field is empty!");
            avail_error.setVisibility(View.VISIBLE);
            return false;
        }
        if(!avail.getText().toString().matches("^([0-9]|0[0-9]|1?[0-9]|2[0-3]):[0-5][0-9]-([0-9]|0[0-9]|1?[0-9]|2[0-3]):[0-5][0-9]$")){
            avail_error.setText("Invalid time format (21:00-23:00)");
            avail_error.setVisibility(View.VISIBLE);
            return false;
        }
        avail_error.setVisibility(View.INVISIBLE);
        return true;
    }

    boolean car_Spinner_Check(Spinner sp1, Spinner sp2, TextView car_mm_error){
        if(sp1.getSelectedItem().toString().length()==0 || sp2.getSelectedItem().toString().length()==0){
            car_mm_error.setText("Select car make and model!");
            car_mm_error.setVisibility(View.VISIBLE);
            return false;
        }
        car_mm_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //input check for the car info field
    //if the field is empty, an error will be shown
    boolean car_Check(EditText car_info, TextView car_error){
        if(car_info.getText().toString().length()==0){
            car_error.setText("Car info field is empty");
            car_error.setVisibility(View.VISIBLE);
            return false;
        }
        car_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //function checking whether the event duration is valid
    boolean duration_Check(EditText duration, TextView duration_error){
        if(duration.getText().toString().length()==0){
            duration_error.setText("Duration field is empty!");
            duration_error.setVisibility(View.VISIBLE);
            return false;
        }
        String check = duration.getText().toString();
        //checking whether the user input is an integer number
        try {
            int num = Integer.parseInt(check);
        } catch (NumberFormatException e) {
            duration_error.setText("Enter integer number!");
            duration_error.setVisibility(View.VISIBLE);
           return false;
        }
        duration_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //checking whether the event starting time
    //entered by the user is correct
    boolean starting_Time(EditText avail, TextView avail_error) {
        if (avail.getText().toString().length() == 0) {
            avail_error.setText("Starting time field is empty!");
            avail_error.setVisibility(View.VISIBLE);
            return false;
        }
        if(!avail.getText().toString().matches("^([0-9]|0[0-9]|1?[0-9]|2[0-3]):[0-5][0-9]$")){
            avail_error.setText("Invalid time format (21:00)");
            avail_error.setVisibility(View.VISIBLE);
            return false;
        }
        avail_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //checking whether the email entered is in the correct format
     boolean checkEmail(EditText email, TextView email_error) {
         if (email.getText().toString().length() == 0) {
             email_error.setText("Email field is empty!");
             email_error.setVisibility(View.VISIBLE);
             return false;
         }
         if(!EMAIL_ADDRESS_PATTERN.matcher(email.getText().toString()).matches()){
             email_error.setText("Wrong email format!");
             email_error.setVisibility(View.VISIBLE);
             return false;
         }
         email_error.setVisibility(View.INVISIBLE);
         return true;
    }

}
