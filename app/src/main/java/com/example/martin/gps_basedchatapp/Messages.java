package com.example.martin.gps_basedchatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Messages extends AppCompatActivity {

    ArrayList<String> savedmessages = new ArrayList<String>();
    private DatabaseReference mDatabase;

    public void Data(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference ref = mDatabase.child("locations").getRef();

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange (DataSnapshot dataSnapshot){
                                          for (DataSnapshot locSnapshot : dataSnapshot.getChildren()) {
                                              LocationMessage loc = locSnapshot.getValue(LocationMessage.class);
                                              if (loc != null) {
                                                  String text = loc.Message;
                                                  Log.d("Message", text);
                                                  savedmessages.add(text);
                                                  Log.d("Array", "arr: " + savedmessages);
                                              }
                                          }
                                          ref.removeEventListener(this);
                                      }

                                      @Override
                                      public void onCancelled (DatabaseError databaseError){
                                          System.out.println("The read failed: " + databaseError.getCode());
                                      }
                                  }

        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Data();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, savedmessages);

        ListView listView = (ListView) findViewById(R.id.messagesList);
        listView.setAdapter(adapter);
    }
}
