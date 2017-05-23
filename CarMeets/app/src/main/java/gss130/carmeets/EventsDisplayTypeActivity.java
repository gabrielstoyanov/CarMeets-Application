package gss130.carmeets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventsDisplayTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_display_type);

        //initializing the buttons that are present
        //in the activity (View All, Recommended)
        Button btn_view_all = (Button) findViewById(R.id.btn_view_all);
        Button btn_view_recommended = (Button) findViewById(R.id.btn_recommended_events);

        //setting up a sharedPreferences object for the display type
        //along with an editor, in order to record
        //the display type into the sharedpreferences
        final SharedPreferences sp_displayType = this.getSharedPreferences("display", 0);
        final SharedPreferences.Editor editor = sp_displayType.edit();

        //setting the onClickListener for the View All Button
        btn_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting the ViewEventsActivity along with a display type "All"
                //meaning all events should be displayed
                startActivity(new Intent(EventsDisplayTypeActivity.this, ViewEventsActivity.class));
                editor.putString("display", "All");
                editor.apply();

            }
        });

        //setting the onClickListener for the Recommended Button
        btn_view_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting the ViewEventsActivity along with a display type "Suggested"
                //meaning only suggested events should be displayed
                startActivity(new Intent(EventsDisplayTypeActivity.this, ViewEventsActivity.class));
                editor.putString("display", "Suggested");
                editor.apply();
            }
        });
    }
}
