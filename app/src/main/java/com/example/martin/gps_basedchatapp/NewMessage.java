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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewMessage extends AppCompatActivity {

    EditText messagetext;
    Button Submit;
    private LocationManager lm;
    private LocationMessage message;
    private Location location;
    private DatabaseReference mDatabase;
    Boolean newlocation = Boolean.TRUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);

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
            message = new LocationMessage(location.getLatitude(),location.getLongitude(), NewMessage.this.messagetext.getText().toString());
            Submit.setVisibility(View.VISIBLE);

        }

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  input = messagetext.getText().toString();
                message = new LocationMessage(location.getLatitude(),location.getLongitude(), input);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference Ref = mDatabase.child("locations").push();
                final DatabaseReference ref = mDatabase.child("locations").getRef();



                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean newlocation = true;

                        for (DataSnapshot messagesSnapshot: dataSnapshot.getChildren()) {
                            LocationMessage oldmessage = messagesSnapshot.getValue(LocationMessage.class);

                            Location locationA = new Location("old message");
                            locationA.setLatitude(oldmessage.latitude);
                            locationA.setLongitude(oldmessage.longitude);

                            float distance = location.distanceTo(locationA);

                            if(distance <10){
                                String editedmessage = oldmessage.Message+"; \n "+message.Message;
                                oldmessage.Message=editedmessage;
                                mDatabase.child("locations").child(messagesSnapshot.getKey()).setValue(oldmessage);
                                newlocation = false;
                            }

                        }
                        if(newlocation==true) {
                            Ref.setValue(message);
                        }
                        ref.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });



                Intent intent = new Intent(NewMessage.this, Main.class);
                NewMessage.this.startActivity(intent);
            }
        });
    }




    public void mainscreen(View view){
        Intent intent = new Intent(NewMessage.this, Main.class);
        NewMessage.this.startActivity(intent);
    }
}
