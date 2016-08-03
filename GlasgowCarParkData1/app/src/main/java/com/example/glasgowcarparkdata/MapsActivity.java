package com.example.glasgowcarparkdata;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String latitude = "0.0";
    private String longitude = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent mainAct = getIntent();
        latitude = mainAct.getStringExtra("MapLat");
        longitude = mainAct.getStringExtra("MapLong");

    }

    public void onSearch(View v){
        EditText srcLocation = (EditText) findViewById(R.id.etSearch);
        String location = srcLocation.getText().toString();
        List<Address> addressList = null;

        if(location != null || !location.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try{
                addressList = geocoder.getFromLocationName(location , 1);
            }catch (IOException e){
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng coords = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(coords).title("Search Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
        }
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

        // Add a marker in Sydney and move the camera
        LatLng carParkLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        CameraPosition startPoint = new CameraPosition(carParkLocation, 3.0f, 0, 0);
        mMap.addMarker(new MarkerOptions().position(carParkLocation).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(startPoint));

    }
}
