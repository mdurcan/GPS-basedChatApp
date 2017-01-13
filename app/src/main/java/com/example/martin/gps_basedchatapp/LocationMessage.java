package com.example.martin.gps_basedchatapp;

/**
 * Created by Martin on 13/01/2017.
 */

public class LocationMessage {
    public double longitude;
    public double latitude;
    public String Message;

    public LocationMessage() {

    }

    public LocationMessage(double lat,double longi, String message) {
        longitude = longi;
        latitude = lat;
        Message = message;
    }
}
