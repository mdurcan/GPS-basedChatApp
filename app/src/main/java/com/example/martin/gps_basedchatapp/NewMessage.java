package com.example.martin.gps_basedchatapp;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewMessage extends AppCompatActivity {

    EditText messagetext;
    Button Submit;
    private LocationManager lm;
    private LocationMessage message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);

        Location location = null;
        messagetext = (EditText) findViewById(R.id.message_text);
        Submit = (Button) findViewById(R.id.Submit_button);

        try {
            location = lm.getLastKnownLocation(provider);
        }
        catch (SecurityException e) {
            Log.e("GPS", "exception occured " + e.getMessage());
        }
        catch (Exception e) {
            Log.e("GPS", "exception occured " + e.getMessage());
        }

        if(location != null && messagetext.getText() != null){
            message = new LocationMessage(location.getLatitude(),location.getLongitude(), messagetext.getText().toString());
            Submit.setVisibility(View.VISIBLE);
        }
    }


    private DatabaseReference mDatabase;
    public void messagesubmit(View view){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Ref = mDatabase.child("locations").push();

        Ref.setValue(message);
    }

    public void mainscreen(View view){
        Intent intent = new Intent(NewMessage.this, Main.class);
        NewMessage.this.startActivity(intent);
    }
}
