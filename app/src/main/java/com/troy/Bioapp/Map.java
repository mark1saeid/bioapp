package com.troy.Bioapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng masa = new LatLng(30.246743, 31.467949);
        mMap.addMarker(new MarkerOptions().position(masa).title("سنتر الماسة"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(masa));

        LatLng newe = new LatLng(30.256945, 31.467974);
        mMap.addMarker(new MarkerOptions().position(newe).title("سنتر نيو اكسبيرت"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newe));
    }
    public void back(View view) {
        Intent intent = new Intent (Map.this, MainActivity.class);
        startActivity(intent);
    }
}
