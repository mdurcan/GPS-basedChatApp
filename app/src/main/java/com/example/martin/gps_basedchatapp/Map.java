package com.example.martin.gps_basedchatapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Map extends FragmentActivity  implements android.location.LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager lm;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);

        try {
            lm.requestLocationUpdates(provider, 10000, (float) 10, this);
        }
        catch (SecurityException e) {
            Log.e("GPS", "exception occured " + e.getMessage());
        }
        catch (Exception e) {
            Log.e("GPS", "exception occured " + e.getMessage());
        }

        setUpMapIfNeeded();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            setUpMap();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }
    }

    private void setUpMap() {
        final DatabaseReference ref = mDatabase.child("locations").getRef();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locSnapshot: dataSnapshot.getChildren()) {
                    LocationMessage loc = locSnapshot.getValue(LocationMessage.class);
                    if (loc != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(loc.latitude,loc.longitude))
                                .title(loc.Message)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void onLocationChanged(Location arg0) {
        mMap.clear();
        setUpMap();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(arg0.getLatitude(),arg0.getLongitude()))
                .title("Current location"));

        CameraUpdate currentlocation= CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(),arg0.getLongitude()));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(currentlocation);
        mMap.animateCamera(zoom);
    }

    public void onProviderDisabled(String arg0) {
        Log.e("GPS", "provider disabled " + arg0);
    }

    public void onProviderEnabled(String arg0) {
        Log.e("GPS", "provider enabled " + arg0);
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        Log.e("GPS", "status changed to " + arg0 + " [" + arg1 + "]");
    }
}
