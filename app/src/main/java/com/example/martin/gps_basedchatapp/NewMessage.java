package com.example.martin.gps_basedchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
    }

    public void mainscreen(View view){
        Intent intent = new Intent(NewMessage.this, Main.class);
        NewMessage.this.startActivity(intent);
    }
}
