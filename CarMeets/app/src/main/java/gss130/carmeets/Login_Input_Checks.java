package gss130.carmeets;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//this class is used
//to check the login details
//entered by the user
public class Login_Input_Checks {

    //a method checking whether the username field is empty
    //if it is empty, set an appropriate error label's text
    //and make it visible
    //otherwise set the label to invisible and return true
    boolean username_Check(EditText username, TextView username_error){
        if(username.getText().toString().length()==0){
            username_error.setText("Username field is empty");
            username_error.setVisibility(View.VISIBLE);
            return false;
        }
        username_error.setVisibility(View.INVISIBLE);
        return true;
    }

    //a method checking whether the password field is empty
    //if it is empty, set an appropriate error label's text
    //and make it visible
    //otherwise set the label to invisible and return true
    boolean password_Check(EditText password, TextView password_error){
        if(password.getText().toString().length()==0){
            password_error.setText("Password field is empty!");
            password_error.setVisibility(View.VISIBLE);
            return false;
        }
        password_error.setVisibility(View.INVISIBLE);
        return true;
    }
}
